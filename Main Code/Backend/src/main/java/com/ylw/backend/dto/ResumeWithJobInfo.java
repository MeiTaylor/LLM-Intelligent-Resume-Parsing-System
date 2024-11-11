package com.ylw.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResumeWithJobInfo {
    private String resumeId;
    private String name;
    private String gender;
    private int age;
    private int workExperience;
    private String education;
    private String major;
    private int score;
    private String jobIntention;
    private String matchJob;
    private String school;
    private List<String> characteristics;
}