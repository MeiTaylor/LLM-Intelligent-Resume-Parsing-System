package com.ylw.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
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

}
