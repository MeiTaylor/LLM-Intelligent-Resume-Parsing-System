package com.ylw.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeWeeklyState {
    public List<HomeJobCount> jobCountList;
    public List<HomeResumeCount> resumeCountList;
}
