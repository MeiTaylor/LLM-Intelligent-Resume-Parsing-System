package com.ylw.backend.controller;

import com.ylw.backend.model.Applicant;
import com.ylw.backend.service.ResumeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeServiceInterface resumeService;

    @Autowired
    public ResumeController(ResumeServiceInterface resumeService) {
        this.resumeService = resumeService;
    }

    /**
     * 简历上传接口
     * @param file 上传的简历文件
     * @return ResponseEntity<String>
     */
    @PostMapping("/uploadResume")
    public ResponseEntity<?> parseResume(@RequestParam("file") MultipartFile file) {
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
            String staticFileRoot = "D:\\JavaProject\\ResumeSystem\\Resume";

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
            resumeService.createAndSaveResume(filePath.toString(), applicant, 1, 1);
            // 返回成功的响应
            return ResponseEntity.ok(applicant);
        } catch (Exception e) {
            // 返回错误信息
            System.err.println("简历存储失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/test")
    public ResponseEntity<Applicant> test() {
        try {
            Applicant applicant = resumeService.parseResumeTest("D:\\JavaProject\\ResumeSystem\\Resume\\1.docx");
            return ResponseEntity.ok(applicant);
        } catch (Exception e) {
            // 返回错误信息
            System.err.println("简历解析失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
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