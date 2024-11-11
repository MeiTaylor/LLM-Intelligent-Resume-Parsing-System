package com.ylw.backend.service;

import com.ylw.backend.dto.*;
import com.ylw.backend.model.JobPosition;

import java.util.List;

public interface JobPositionServiceInterface {
    List<DepartmentJobInfo> getJobInfo(int userId);
    JobPosition addJobPosition(JobPositionDTO jobPosition);

    GraphForJobResumeCountModelClass ForJobResumeCount(int userId);

    JobAddReturnMsg addJobPositionByJobAddDTO(JobAddDTO jobAddDTO);
    List<JobResumeCount> getJobResumeCounts(int companyId);

    List<JobNameIdDTO> getAllJobNamesAndIds(int userId);

    List<AllResumeWithJobInfo> getAllResumeWithJobInfo(int userId);

    List<ResumeBasicInfo> getAllResumeBasicInfo(int userId);
}
