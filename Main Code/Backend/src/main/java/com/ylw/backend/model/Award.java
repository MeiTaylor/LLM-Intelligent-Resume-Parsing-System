package com.ylw.backend.model;

import jakarta.persistence.*;

@Entity
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "applicant_id")
    private int applicantId;

    private String awardName;

    // 导航属性 - 申请人
    @ManyToOne
    @JoinColumn(name = "applicant_id", insertable = false, updatable = false)
    private Applicant applicant;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
