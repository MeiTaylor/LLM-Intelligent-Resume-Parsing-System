package com.ylw.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ylw.backend.model.ApplicantProfile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class JobMatchDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("职位名称")
    private String jobTitle;

    @JsonProperty("人岗匹配程度分数")
    private int score;

    @JsonProperty("人岗匹配的理由")
    private String reason;

}