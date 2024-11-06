package com.ylw.backend.service;

import com.ylw.backend.dto.HomeToUploadResume;
import com.ylw.backend.dto.JobResumeCount;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.dto.JobPositionDTO;

import java.util.List;

public interface JobPositionServiceInterface {
    HomeToUploadResume getJobInfo(int userId);
    JobPosition addJobPosition(JobPositionDTO jobPosition);

    List<JobResumeCount> getJobResumeCounts(int companyId);
}
