package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@ToString(exclude = "company")
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String department;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    private int minimumWorkYears;

    private String minimumEducationLevel;

    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL)
    private List<JobKeyword> jobKeywords;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id")
    @EqualsAndHashCode.Exclude  // 排除这个字段，避免递归
    private Company company;

    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL)
    private List<Resume> resumes;
}