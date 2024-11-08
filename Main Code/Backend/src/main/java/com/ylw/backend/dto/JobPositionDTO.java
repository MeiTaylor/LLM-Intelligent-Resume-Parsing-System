package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JobPositionDTO {
    private String title;
    private String department;
    private String description;
    private int minimumWorkYears;
    private String minimumEducationLevel;
    private int companyId; // 用于关联公司
}
