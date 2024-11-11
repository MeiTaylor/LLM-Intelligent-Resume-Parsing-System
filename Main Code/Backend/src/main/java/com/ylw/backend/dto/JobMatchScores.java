package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobMatchScores {
    private int below60;
    private int range60_70;
    private int range70_80;
    private int range80_90;
    private int range90_100;
}
