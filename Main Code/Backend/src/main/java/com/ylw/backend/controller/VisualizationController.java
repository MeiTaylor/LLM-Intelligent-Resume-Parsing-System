package com.ylw.backend.controller;

import com.ylw.backend.dto.AgeGroups;
import com.ylw.backend.dto.EducationInfoForGraphClass;
import com.ylw.backend.dto.GraphForJobResumeCountModelClass;
import com.ylw.backend.dto.WorkStability;
import com.ylw.backend.service.CompanyServiceInterface;
import com.ylw.backend.service.JobPositionServiceInterface;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visualization")
public class VisualizationController {
    private final JobPositionServiceInterface jobPositionService;
    private final CompanyServiceInterface companyService;

    public VisualizationController(JobPositionServiceInterface jobPositionService, CompanyServiceInterface companyService) {
        this.jobPositionService = jobPositionService;
        this.companyService = companyService;
    }

    @GetMapping("/total")
    public GraphForJobResumeCountModelClass ForJobResumeCount(int userId) {
        return jobPositionService.ForJobResumeCount(userId);
    }

    @GetMapping("/education")
    public EducationInfoForGraphClass ForEducationInfo(int userId) {
        return companyService.ForEducationInfo(userId);
    }

    @GetMapping("/age")
    public AgeGroups ageInfoForGraphClass(int userId) {
        return companyService.ageInfoForGraphClass(userId);
    }

    @GetMapping("/workStability")
    public WorkStability workStabilityInfoForGraphClass(int userId) {
        return companyService.workStabilityInfoForGraphClass(userId);
    }







}
