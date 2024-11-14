package com.ylw.backend.controller;

import com.ylw.backend.dto.*;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.service.JobPositionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/jobposition")
public class JobpositionController {

    private final JobPositionServiceInterface jobPositionService;

    @Autowired
    public JobpositionController(JobPositionServiceInterface jobPositionService) {
        this.jobPositionService = jobPositionService;
    }

    @PostMapping("/departments/jobs")
    public List<DepartmentJobInfo> getDepartmentJobInfo(@RequestBody WebSentUserId webSentUserId) {
        int userId = webSentUserId.getUserId(); // 用户ID
        // 查数据库返回所有岗位的名称以及ID；
        return jobPositionService.getJobInfo(userId);
    }

    @GetMapping("/allJobNamesAndIds")
    public ResponseEntity<List<JobNameIdDTO>> getAllJobNamesAndIds(@RequestParam int userId) {
        List<JobNameIdDTO> jobNamesAndIds = jobPositionService.getAllJobNamesAndIds(userId);
        return ResponseEntity.ok(jobNamesAndIds);
    }

    @PostMapping("/add")
    public JobAddReturnMsg addJobPosition(@RequestParam JobAddDTO jobAddDTO) {
        return jobPositionService.addJobPositionByJobAddDTO(jobAddDTO);
    }

    @PostMapping("/add-bulk")
    public ResponseEntity<String> addBulkJobPositions() {
        List<JobPositionDTO> jobPositionDTOs = Arrays.asList(
                new JobPositionDTO("产品/电商运营", "市场部",
                        "要求至少2年的运营经验，电商背景优先。必须具备数据分析和项目管理能力。需要有自我驱动力，逻辑思维清晰，以及强沟通能力。",
                        2, "本科", 1), // 假设公司ID为1
                new JobPositionDTO("平面设计师", "设计部",
                        "至少大专学历，1-2年相关工作经验。需要熟练掌握设计软件和视频剪辑。需要具备创新思维，良好的沟通能力和责任感。",
                        1, "大专", 1),
                new JobPositionDTO("产品经理", "产品部",
                        "负责产品需求分析和规划，跨部门沟通协调，确保产品上线进度和质量。",
                        3, "本科", 1),
                new JobPositionDTO("UI/UX设计师", "设计部",
                        "负责产品的用户界面和用户体验设计，提升用户的操作体验。",
                        2, "本科", 1),
                new JobPositionDTO("财务/风控专员", "财务部",
                        "本科及以上学历，金融或相关专业。必须了解财务、会计、税收政策知识，具备风控经验。需要具有数据分析能力和EXCEL的熟练操作。",
                        0, "本科", 1),
                new JobPositionDTO("市场营销", "市场部",
                        "本科及以上学历，至少3年相关经验。熟练使用办公软件，能够管理客户关系。需要有市场策划和拓展经验，以及良好的执行力。",
                        3, "本科", 1),
                new JobPositionDTO("开发工程师", "技术部",
                        "本科及以上学历，计算机相关专业。至少3年的软件开发经验。需要熟练使用JAVA，有APP开发经验。",
                        3, "本科", 1),
                new JobPositionDTO("网络安全工程师", "技术部",
                        "负责公司网络和系统的安全防护，识别和防范潜在的网络攻击。",
                        3, "本科", 1),
                new JobPositionDTO("人力资源管理", "行政部",
                        "大专及以上学历，至少1年相关经验。熟练使用办公软件，具备日常管理和档案处理能力。需要了解人力资源流程和法规，具有全面的沟通能力。",
                        1, "大专", 1),
                new JobPositionDTO("行政主管", "行政部",
                        "负责公司日常行政事务的统筹管理，监督和执行公司政策。",
                        3, "本科", 1)
        );

        List<JobPosition> addedPositions = new ArrayList<>();
        for (JobPositionDTO dto : jobPositionDTOs) {
            addedPositions.add(jobPositionService.addJobPosition(dto));
        }

        return ResponseEntity.ok("批量添加成功");
    }

    // 人岗匹配
    @GetMapping("/allResumeWithJobInfo")
    public ResponseEntity<List<AllResumeWithJobInfo>> getAllResumeWithJobInfo(@RequestParam int userId) {
        List<AllResumeWithJobInfo> allResumeWithJobInfo = jobPositionService.getAllResumeWithJobInfo(userId);
        return ResponseEntity.ok(allResumeWithJobInfo);
    }

    //所有简历基本信息
    @GetMapping("/allResumeBasicInfo")
    public ResponseEntity<List<ResumeWithJobInfo>> getAllResumeBasicInfo(@RequestParam int userId) {
        List<ResumeWithJobInfo> resumeBasicInfos = jobPositionService.getAllResumeBasicInfo(userId);
        return ResponseEntity.ok(resumeBasicInfos);
    }

}