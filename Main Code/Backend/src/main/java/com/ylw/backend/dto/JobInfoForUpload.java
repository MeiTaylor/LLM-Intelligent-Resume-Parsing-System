package com.ylw.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobInfoForUpload {
    int jobId;
    public String jobName;
    public int weekJobResumes;
    public int allJobResume;
}