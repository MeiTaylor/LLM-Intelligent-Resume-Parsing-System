package com.ylw.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class InfoForHomeModelClass {
    public int totalResumes; // 该公司对应的所有简历数量
    public int totalJobs; // 所有岗位数量
    public HomeWeeklyState weeklyStates; // 每天新增的岗位以及简历数
    public List<BriefHomeResumeInfo> resumeHistory; // 显示在该主页的所需的简历历史信息
    public List<JobResumeCount> jobResumeCounts; // 岗位对应的简历数量的数组
}