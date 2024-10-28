package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
}