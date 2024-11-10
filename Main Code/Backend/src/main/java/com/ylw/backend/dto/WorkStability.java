package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkStability {
    private int low;
    private int mediumLow;
    private int medium;
    private int mediumHigh;
    private int high;
}
