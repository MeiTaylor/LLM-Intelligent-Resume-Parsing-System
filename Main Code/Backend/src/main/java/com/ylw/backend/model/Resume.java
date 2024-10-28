package com.ylw.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "applicant_id", insertable = false, updatable = false)
    private int applicantId;

    @Column(name = "job_position_id", insertable = false, updatable = false)
    private int jobPositionId;

    @Column(name = "company_id", insertable = false, updatable = false)
    private int companyId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String filePath;

    private String imagePath;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "job_position_id")
    private JobPosition jobPosition;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

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

    public int getJobPositionId() {
        return jobPositionId;
    }

    public void setJobPositionId(int jobPositionId) {
        this.jobPositionId = jobPositionId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
