package com.ylw.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraduationSchoolsLevel {
    private int _985;
    private int _211;
    private int ordinaryFirstClass;
    private int secondClassOrBelow;
}
