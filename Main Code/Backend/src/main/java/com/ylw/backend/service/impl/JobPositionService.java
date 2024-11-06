package com.ylw.backend.service.impl;

import com.ylw.backend.dto.HomeToUploadResume;
import com.ylw.backend.dto.JobInfoForUpload;
import com.ylw.backend.dto.JobPositionDTO;
import com.ylw.backend.model.Company;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.repository.CompanyRepository;
import com.ylw.backend.repository.JobPositionRepository;
import com.ylw.backend.service.JobPositionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPositionService implements JobPositionServiceInterface {

    private final JobPositionRepository jobPositionRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public JobPositionService (JobPositionRepository jobPositionRepository, CompanyRepository companyRepository) {
        this.jobPositionRepository = jobPositionRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public HomeToUploadResume getJobInfo(int userId) {
        List<JobPosition> jobPositions = jobPositionRepository.findByCompany_Users_Id(userId);

        if (jobPositions == null) {
            // 如果找不到对应的岗位，返回空的岗位信息
            return new HomeToUploadResume();
        }

        // 获取该公司下所有岗位的信息
        List<JobInfoForUpload> jobsInfo = jobPositions.stream()
                .map(jobPosition -> new JobInfoForUpload(jobPosition.getTitle(), jobPosition.getId()))
                .collect(Collectors.toList());

        return new HomeToUploadResume(jobsInfo);
    }

    @Override
    public JobPosition addJobPosition(JobPositionDTO jobPositionDTO) {
        Optional<Company> company = companyRepository.findById(jobPositionDTO.getCompanyId());
        if (company.isEmpty()) {
            throw new IllegalArgumentException("Company not found with ID: " + jobPositionDTO.getCompanyId());
        }

        JobPosition jobPosition = new JobPosition();
        jobPosition.setTitle(jobPositionDTO.getTitle());
        jobPosition.setDescription(jobPositionDTO.getDescription());
        jobPosition.setMinimumWorkYears(jobPositionDTO.getMinimumWorkYears());
        jobPosition.setMinimumEducationLevel(jobPositionDTO.getMinimumEducationLevel());
        jobPosition.setCreatedDate(LocalDateTime.now());
        jobPosition.setCompany(company.get());

        return jobPositionRepository.save(jobPosition);
    }
}
