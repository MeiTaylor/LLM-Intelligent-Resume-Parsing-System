package com.ylw.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AllResumeWithJobInfo {
    private String jobName;
    private List<ResumeWithJobInfo> resumeWithJobInfos;
}
