package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "applicantProfile")
public class JobMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("职位名称")
    private String jobTitle;

    @JsonProperty("人岗匹配程度分数")
    private int score;

    @JsonProperty("人岗匹配的理由")
    private String reason;

    @Column(name = "applicant_profile_id", insertable = false, updatable = false)
    private int applicantProfileId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "applicant_profile_id")
    private ApplicantProfile applicantProfile;

}
