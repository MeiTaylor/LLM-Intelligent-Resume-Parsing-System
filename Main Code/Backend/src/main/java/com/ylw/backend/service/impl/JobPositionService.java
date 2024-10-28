package com.ylw.backend.service.impl;

import com.ylw.backend.dto.HomeToUploadResume;
import com.ylw.backend.dto.JobInfoForUpload;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.repository.JobPositionRepository;
import com.ylw.backend.service.IJobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPositionService implements IJobPositionService {

    private final JobPositionRepository jobPositionRepository;

    @Autowired
    public JobPositionService(JobPositionRepository jobPositionRepository) {
        this.jobPositionRepository = jobPositionRepository;
    }

    @Override
    public HomeToUploadResume uploadJobInfo(int userId) {
        List<JobPosition> jobPositions = jobPositionRepository.findByCompany_Users_Id(userId);

        if (jobPositions == null) {
            // 如果找不到对应的岗位，返回空的岗位信息
            return new HomeToUploadResume(List.<JobInfoForUpload>of());
        }

        // 获取该公司下所有岗位的信息
        List<JobInfoForUpload> jobsInfo = jobPositions.stream()
                .map(jobPosition -> new JobInfoForUpload(jobPosition.getTitle(), jobPosition.getId()))
                .collect(Collectors.toList());

        return new HomeToUploadResume(jobsInfo);
    }
}
