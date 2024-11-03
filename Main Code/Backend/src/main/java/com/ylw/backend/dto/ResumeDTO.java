package com.ylw.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResumeDTO {
    public int resumeId;
    public String name;
    public String gender;
    public int age;
    public int workExperience;
    public String education;
    public String major;
    public String score;
    public String jobIntention;
    public String matchJob;
    public String school;
    public List<String> characteristics;
}