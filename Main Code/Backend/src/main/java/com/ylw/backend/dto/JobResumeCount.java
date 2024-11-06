package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobResumeCount {
    private String jobName; // 岗位名称
    private int resumecount; // 简历数量
}
