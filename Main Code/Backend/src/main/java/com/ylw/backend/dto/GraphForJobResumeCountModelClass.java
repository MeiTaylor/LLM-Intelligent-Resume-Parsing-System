package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphForJobResumeCountModelClass {
    public int totalResumeCount;

    public List<JobResumeCount> jobResumeCounts;
}
