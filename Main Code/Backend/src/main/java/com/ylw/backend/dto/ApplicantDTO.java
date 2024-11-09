package com.ylw.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ylw.backend.model.Award;
import com.ylw.backend.model.SkillCertificate;
import com.ylw.backend.model.WorkExperience;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
public class ApplicantDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email; // 个人邮箱

    private String phoneNumber; // 手机号

    private int age;

    private String gender;

    private String jobIntention; // 求职意向岗位

    private String highestEducation; // 最高学历

    private String major; // 专业

    private String graduatedFrom; // 毕业院校

    private String graduatedFromLevel; // 毕业院校等级

    private String selfEvaluation; // 自我评价

    private int totalWorkYears; // 工作总时间

    private String workStability; // 工作稳定性

    public String workStabilityReason; // 工作稳定性原因

    private List<Award> awards;

    private List<WorkExperience> workExperiences;

    private List<SkillCertificate> skillCertificates;
}
