package com.ylw.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


@Data
@Entity
@ToString(exclude = "applicant")
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String awardName;

    // 导航属性 - 申请人
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

}
