package com.ylw.backend.service;

import com.ylw.backend.dto.*;
import com.ylw.backend.model.JobPosition;

import java.util.List;

public interface JobPositionServiceInterface {
    List<DepartmentJobInfo> getJobInfo(int userId);
    JobPosition addJobPosition(JobPositionDTO jobPosition);
    JobAddReturnMsg addJobPositionByJobAddDTO(JobAddDTO jobAddDTO);
    List<JobResumeCount> getJobResumeCounts(int companyId);

    List<JobNameIdDTO> getAllJobNamesAndIds(int userId);
}
