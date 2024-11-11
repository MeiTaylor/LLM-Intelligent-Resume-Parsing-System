package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeBasicInfo {
    private int id;
    private String name;
    private int age;
    private String highestEducation;
    private String phoneNumber;
    private String jobIntention;
    private String gender;
    private int maxMatchingScore;
}