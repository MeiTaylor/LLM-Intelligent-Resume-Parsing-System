package com.ylw.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BriefHomeResumeInfo {
    private int rId; // 简历ID
    private String resumeName; // 求职者名字
    private String jobIntention; // 求职者的意向岗位
    private String uploadDate; // 该简历上传的时间
    private String highestEducation; // 最高学历
}
