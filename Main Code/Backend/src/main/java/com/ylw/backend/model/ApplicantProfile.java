package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class ApplicantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "applicant_id")
    private int applicantId;

    private String matchingReason; // 给出此工作稳定性判断的原因

    private int matchingScore; // 人岗匹配程度分数

    private String workStability; // 工作稳定性的程度

    private String stabilityReason; // 工作稳定性判断的原因

    // 导航属性 - 求职匹配
    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL)
    private List<JobMatch> jobMatches;

    // 导航属性 - 工作特征
    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL)
    private List<WorkTrait> workTraits;

    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL)
    private List<Characteristic> characteristics;

    // 导航属性 - 申请人
    @ManyToOne
    @JoinColumn(name = "applicant_id", insertable = false, updatable = false)
    private Applicant applicant;
}