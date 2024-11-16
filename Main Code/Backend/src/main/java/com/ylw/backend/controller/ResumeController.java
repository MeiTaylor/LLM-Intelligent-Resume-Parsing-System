package com.ylw.backend.controller;

import com.ylw.backend.dto.ApplicantDTO;
import com.ylw.backend.dto.JobMatchDTO;
import com.ylw.backend.model.Applicant;
import com.ylw.backend.model.Resume;
import com.ylw.backend.service.ResumeServiceInterface;
import com.ylw.backend.service.impl.EmailMoniterSercive;
import com.ylw.backend.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeServiceInterface resumeService;
    private final EmailMoniterSercive emailMoniterSercive;
    @Value("${resume.file.root}")
    private String staticFileRoot;

    @Autowired
    public ResumeController(ResumeServiceInterface resumeService, EmailMoniterSercive emailMoniterSercive) {
        this.resumeService = resumeService;
        this.emailMoniterSercive = emailMoniterSercive;
    }

    /**
     * 简历上传接口
     * @param file 上传的简历文件
     * @return ResponseEntity<String>
     */
    @PostMapping("/uploadResume")
    public ResponseEntity<?> parseResume(@RequestParam("file") MultipartFile file, int jobId, int userId) {
        if (file == null || file.isEmpty()) {
            // 如果文件为空，返回错误代码和空申请人信息
            return ResponseEntity.badRequest().body("文件为空，无法上传");
        }

        try {
            // 获取文件名
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new IllegalArgumentException("无法获取文件名");
            }

            // 设置文件存储根目录
            //String staticFileRoot = "D:\\大学本科学习资料\\大四上\\大型软件设计\\resume";
            //String staticFileRoot = staticFileRoot;

            // 创建基于当前日期的文件存储路径
            LocalDate today = LocalDate.now();
            String fileUrlWithoutFileName = String.format("%d/%d/%d", today.getYear(), today.getMonthValue(), today.getDayOfMonth());

            // 创建完整的目录路径
            Path directoryPath = Paths.get(staticFileRoot, fileUrlWithoutFileName);
            Files.createDirectories(directoryPath);

            // 计算文件的 MD5 哈希值
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md5.digest(file.getBytes());
            String hashedFileName = bytesToHex(hashBytes);

            // 创建新的文件名，避免重复
            String fileExtension = getFileExtension(originalFileName);
            String newFileName = hashedFileName + (fileExtension.isEmpty() ? "" : "." + fileExtension);

            // 创建完整的文件路径
            Path filePath = directoryPath.resolve(newFileName);

            // 将文件保存到指定位置
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 你可以在这里调用简历解析服务，例如：
            Applicant applicant = resumeService.parseResume(filePath.toString());
            resumeService.createAndSaveResume(filePath.toString(), applicant, jobId, userId);
            // 返回成功的响应
            return ResponseEntity.ok(applicant);
        } catch (Exception e) {
            // 返回错误信息
            System.err.println("简历存储失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getGraph")
    public ResponseEntity<Resource> getFile(@RequestParam int resumeId) {
        try {
            Resource fileResource = resumeService.getResumeImage(resumeId);

            // 获取 MIME 类型
            String mimeType = Files.probeContentType(fileResource.getFile().toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(fileResource);

        } catch (Exception e) {
            System.err.println("获取文件发生异常: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/updateApplicant")
    public ResponseEntity<String> updateApplicant(@RequestBody ApplicantDTO updatedApplicant) {
        try {
            Applicant applicant = resumeService.updateApplicant(updatedApplicant);
            return ResponseEntity.ok("更新成功");
        } catch (Exception e) {
            System.err.println("更新简历信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    public String test() {
//        try {
//            Applicant applicant = resumeService.parseResumeTest("D:\\JavaProject\\ResumeSystem\\Resume\\1.docx");
//            return ResponseEntity.ok(applicant);
//        } catch (Exception e) {
//            // 返回错误信息
//            System.err.println("简历解析失败: " + e.getMessage());
//            return ResponseEntity.badRequest().body(null);
//        }
//        emailMoniterSercive.moniterEmail();

        return "test";
    }

    // 将字节数组转换为十六进制字符串
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 获取文件扩展名
    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1);
    }


}