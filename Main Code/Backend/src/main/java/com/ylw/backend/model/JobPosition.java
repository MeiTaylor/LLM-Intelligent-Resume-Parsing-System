package com.ylw.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "company_id", insertable = false, updatable = false)
    private int companyId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    private int minimumWorkYears;

    private String minimumEducationLevel;

    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL)
    private List<JobKeyword> jobKeywords;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany
    @JoinTable(
            name = "job_position_resume",
            joinColumns = @JoinColumn(name = "job_position_id"),
            inverseJoinColumns = @JoinColumn(name = "resume_id")
    )
    private List<Resume> resumes;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getMinimumWorkYears() {
        return minimumWorkYears;
    }

    public void setMinimumWorkYears(int minimumWorkYears) {
        this.minimumWorkYears = minimumWorkYears;
    }

    public String getMinimumEducationLevel() {
        return minimumEducationLevel;
    }

    public void setMinimumEducationLevel(String minimumEducationLevel) {
        this.minimumEducationLevel = minimumEducationLevel;
    }

    public List<JobKeyword> getJobKeywords() {
        return jobKeywords;
    }

    public void setJobKeywords(List<JobKeyword> jobKeywords) {
        this.jobKeywords = jobKeywords;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes = resumes;
    }
}
