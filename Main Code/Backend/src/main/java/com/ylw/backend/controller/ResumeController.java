package com.ylw.backend.controller;

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
    @PostMapping("/parse")
    public ResponseEntity<String> parseResume(@RequestBody String resumePath) {
        try {
            resumeService.parseResume(resumePath);
            return ResponseEntity.ok("简历解析成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("简历解析失败: " + e.getMessage());
        }
    }
}