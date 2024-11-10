package com.ylw.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighestEducation {
    private int highSchoolOrLess;
    private int juniorCollege;
    private int bachelor;
    private int master;
    private int doctor;
}
