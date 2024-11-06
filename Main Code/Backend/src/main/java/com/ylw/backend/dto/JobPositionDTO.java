package com.ylw.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobPositionDTO {
    private String title;
    private String description;
    private int minimumWorkYears;
    private String minimumEducationLevel;
    private int companyId; // 用于关联公司
}
