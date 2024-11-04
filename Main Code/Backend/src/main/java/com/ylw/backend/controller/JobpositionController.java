package com.ylw.backend.controller;

import com.ylw.backend.dto.HomeToUploadResume;
import com.ylw.backend.dto.JobPositionDTO;
import com.ylw.backend.dto.WebSentUserId;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.service.JobPositionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/jobposition")
public class JobpositionController {

    private final JobPositionServiceInterface jobPositionService;

    @Autowired
    public JobpositionController(JobPositionServiceInterface jobPositionService) {
        this.jobPositionService = jobPositionService;
    }

    @PostMapping("/first")
    public HomeToUploadResume uploadJobInfo(@RequestBody WebSentUserId webSentUserId) {
        int userId = webSentUserId.getUserId(); // 用户ID
        // 查数据库返回所有岗位的名称以及ID；
        return jobPositionService.getJobInfo(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<JobPosition> addJobPosition(@RequestBody JobPositionDTO jobPositionDTO) {
        JobPosition jobPosition = jobPositionService.addJobPosition(jobPositionDTO);
        return ResponseEntity.ok(jobPosition);
    }
}