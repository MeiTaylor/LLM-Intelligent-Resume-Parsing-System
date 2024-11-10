package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationInfoForGraphClass {
    public HighestEducation highestEducation;
    public GraduationSchoolsLevel graduationSchoolsLevel;
}
