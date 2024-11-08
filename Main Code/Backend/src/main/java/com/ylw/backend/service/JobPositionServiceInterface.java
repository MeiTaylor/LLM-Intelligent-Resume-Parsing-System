package com.ylw.backend.service;

import com.ylw.backend.dto.DepartmentJobInfo;
import com.ylw.backend.dto.JobResumeCount;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.dto.JobPositionDTO;

import java.util.List;

public interface JobPositionServiceInterface {
    List<DepartmentJobInfo> getJobInfo(int userId);
    JobPosition addJobPosition(JobPositionDTO jobPosition);

    List<JobResumeCount> getJobResumeCounts(int companyId);
}
