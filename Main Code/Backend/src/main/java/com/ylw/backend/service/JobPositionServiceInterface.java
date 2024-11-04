package com.ylw.backend.service;

import com.ylw.backend.dto.HomeToUploadResume;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.dto.JobPositionDTO;

public interface JobPositionServiceInterface {
    HomeToUploadResume getJobInfo(int userId);
    JobPosition addJobPosition(JobPositionDTO jobPosition);
}
