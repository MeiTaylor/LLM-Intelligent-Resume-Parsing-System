package com.ylw.backend.controller;

import com.ylw.backend.model.Applicant;
import com.ylw.backend.service.ResumeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param resumePath 简历文件路径
     * @return ResponseEntity<String>
     */
    @PostMapping("/uploadResume")
    public ResponseEntity<Applicant> parseResume(@RequestBody String resumePath) {
        try {
            Applicant applicant = resumeService.parseResume(resumePath);
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
}