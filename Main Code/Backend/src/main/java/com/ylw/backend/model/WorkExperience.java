package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "applicant")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "applicant_id", insertable = false, updatable = false)
    private int applicantId;

    @JsonProperty("地点")
    private String companyName;

    @JsonProperty("职位")
    private String position;

    @JsonProperty("时间")
    private String time;

    @JsonProperty("任务")
    private String task;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

}
