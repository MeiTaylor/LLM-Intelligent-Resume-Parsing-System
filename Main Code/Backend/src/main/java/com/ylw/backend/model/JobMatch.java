package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
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

    @ManyToOne
    @JoinColumn(name = "applicant_profile_id")
    private ApplicantProfile applicantProfile;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getApplicantProfileId() {
        return applicantProfileId;
    }

    public void setApplicantProfileId(int applicantProfileId) {
        this.applicantProfileId = applicantProfileId;
    }

    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }
}
