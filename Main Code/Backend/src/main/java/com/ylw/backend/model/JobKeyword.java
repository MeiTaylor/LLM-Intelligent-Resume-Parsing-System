package com.ylw.backend.model;

import jakarta.persistence.*;

@Entity
public class JobKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "job_position_id", insertable = false, updatable = false)
    private int jobPositionId;

    private String keyword;

    @ManyToOne
    @JoinColumn(name = "job_position_id")
    private JobPosition jobPosition;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobPositionId() {
        return jobPositionId;
    }

    public void setJobPositionId(int jobPositionId) {
        this.jobPositionId = jobPositionId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }
}
