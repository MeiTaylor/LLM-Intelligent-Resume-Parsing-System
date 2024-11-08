package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobAddDTO {
    private String title;
    private String department;
    private String description;
    private int minimumWorkYears;
    private String minimumEducationLevel;
    private int userId;
}