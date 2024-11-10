package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = {"company", "jobPosition"})
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@Column(columnDefinition = "TEXT")
    //private String content;
    @Column(name = "file_path")
    private String filePath;

    //private String imagePath;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "applicant_id")
    @EqualsAndHashCode.Exclude  // 排除这个字段，避免递归
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "job_position_id")
    @EqualsAndHashCode.Exclude  // 排除这个字段，避免递归
    private JobPosition jobPosition;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id")
    @EqualsAndHashCode.Exclude  // 排除这个字段，避免递归
    private Company company;

    /**
     * 在保存实体之前自动设置创建日期
     */
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
