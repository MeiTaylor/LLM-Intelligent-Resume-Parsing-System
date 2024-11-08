package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "applicant")
public class SkillCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String skillName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

}
