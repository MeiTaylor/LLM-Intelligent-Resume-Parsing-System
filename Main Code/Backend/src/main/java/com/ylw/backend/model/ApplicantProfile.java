package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "applicant")
public class ApplicantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 导航属性 - 求职匹配
    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL)
    private List<JobMatch> jobMatches;

    // 导航属性 - 工作特征
    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL)
    private List<WorkTrait> workTraits;

    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL)
    private List<Characteristic> characteristics;

    // 导航属性 - 申请人
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "applicant_id")
    @EqualsAndHashCode.Exclude  // 排除这个字段，避免递归
    private Applicant applicant;
}