package com.ylw.backend.controller;

import com.ylw.backend.dto.HomeToUploadResume;
import com.ylw.backend.dto.WebSentUserId;
import com.ylw.backend.service.IJobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/jobposition")
public class JobpositionController {

    private final IJobPositionService jobPositionService;

    @Autowired
    public JobpositionController(IJobPositionService jobPositionService) {
        this.jobPositionService = jobPositionService;
    }

    @PostMapping("/first")
    public HomeToUploadResume uploadJobInfo(@RequestBody WebSentUserId webSentUserId) {
        int userId = webSentUserId.getUserId(); // 用户ID
        // 查数据库返回所有岗位的名称以及ID；
        return jobPositionService.uploadJobInfo(userId);
    }
}