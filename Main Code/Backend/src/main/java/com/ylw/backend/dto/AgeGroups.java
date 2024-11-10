package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgeGroups {
    private int age18_22;
    private int age22_25;
    private int age25_30;
    private int age30_35;
    private int age35AndAbove;
}
