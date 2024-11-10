package com.ylw.backend.service.impl;

import com.ylw.backend.dto.*;
import com.ylw.backend.model.Company;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.repository.CompanyRepository;
import com.ylw.backend.repository.JobPositionRepository;
import com.ylw.backend.repository.UserRepository;
import com.ylw.backend.service.JobPositionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPositionService implements JobPositionServiceInterface {

    private final JobPositionRepository jobPositionRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Autowired
    public JobPositionService (JobPositionRepository jobPositionRepository, CompanyRepository companyRepository, UserRepository userRepository) {
        this.jobPositionRepository = jobPositionRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<DepartmentJobInfo> getJobInfo(int userId) {
        int companyId = userRepository.findCompanyIdById(userId).orElse(-1);
        List<JobPosition> jobPositions = jobPositionRepository.findByCompanyId(companyId);

        if (jobPositions == null || jobPositions.isEmpty()) {
            // 如果找不到对应的岗位，返回空的部门列表
            return Collections.emptyList();
        }

        // 将岗位信息按照部门进行分组
        Map<String, List<JobPosition>> departmentGroups = jobPositions.stream()
                .collect(Collectors.groupingBy(JobPosition::getDepartment));

        // 构建每个部门的 DepartmentJobInfo 对象
        List<DepartmentJobInfo> departmentJobInfos = departmentGroups.entrySet().stream()
                .map(entry -> {
                    String departmentName = entry.getKey();
                    List<JobInfoForUpload> jobInfos = entry.getValue().stream()
                            .map(jobPosition -> {
                                int weekJobResumes = (int) jobPosition.getResumes().stream()
                                        .filter(resume -> resume.getCreatedDate().isAfter(LocalDateTime.now().minusWeeks(1)))
                                        .count();
                                int allJobResume = jobPosition.getResumes().size();

                                return new JobInfoForUpload(jobPosition.getId(), jobPosition.getTitle(), weekJobResumes, allJobResume);
                            })
                            .collect(Collectors.toList());

                    return new DepartmentJobInfo(departmentName, jobInfos.size(), jobInfos);
                })
                .collect(Collectors.toList());

        return departmentJobInfos;
    }

    @Override
    public GraphForJobResumeCountModelClass ForJobResumeCount(int userId) {
        int companyId = userRepository.findCompanyIdById(userId).orElse(-1);
        List<JobPosition> jobPositions = jobPositionRepository.findByCompanyId(companyId);

        if (jobPositions == null || jobPositions.isEmpty()) {
            return new GraphForJobResumeCountModelClass();
        }

        int totalResumes = jobPositions.stream()
                .mapToInt(jobPosition -> jobPosition.getResumes().size())
                .sum();

        List<JobResumeCount> jobResumeCounts = jobPositions.stream()
                .map(jobPosition -> new JobResumeCount(jobPosition.getTitle(), jobPosition.getResumes().size()))
                .collect(Collectors.toList());

        return new GraphForJobResumeCountModelClass(totalResumes, jobResumeCounts);

    }


    @Override
    public JobAddReturnMsg addJobPositionByJobAddDTO(JobAddDTO jobAddDTO) {

        Integer companyId = userRepository.findCompanyIdById(jobAddDTO.getUserId()).orElse(-1);

        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isEmpty()) {
            throw new IllegalArgumentException("Company not found with ID: " + companyId);
        }
        JobPosition jobPosition = new JobPosition();
        jobPosition.setTitle(jobAddDTO.getTitle());
        jobPosition.setDepartment(jobAddDTO.getDepartment());
        jobPosition.setDescription(jobAddDTO.getDescription());
        jobPosition.setMinimumWorkYears(jobAddDTO.getMinimumWorkYears());
        jobPosition.setMinimumEducationLevel(jobAddDTO.getMinimumEducationLevel());
        jobPosition.setCreatedDate(LocalDateTime.now());
        jobPosition.setCompany(company.get());
        jobPositionRepository.save(jobPosition);
        return new JobAddReturnMsg(20000,"创建岗位成功");
    }

    @Override
    public JobPosition addJobPosition(JobPositionDTO jobPositionDTO) {
        Optional<Company> company = companyRepository.findById(jobPositionDTO.getCompanyId());
        if (company.isEmpty()) {
            throw new IllegalArgumentException("Company not found with ID: " + jobPositionDTO.getCompanyId());
        }

        JobPosition jobPosition = new JobPosition();
        jobPosition.setTitle(jobPositionDTO.getTitle());
        jobPosition.setDepartment(jobPositionDTO.getDepartment());
        jobPosition.setDescription(jobPositionDTO.getDescription());
        jobPosition.setMinimumWorkYears(jobPositionDTO.getMinimumWorkYears());
        jobPosition.setMinimumEducationLevel(jobPositionDTO.getMinimumEducationLevel());
        jobPosition.setCreatedDate(LocalDateTime.now());
        jobPosition.setCompany(company.get());

        return jobPositionRepository.save(jobPosition);
    }

    @Override
    public List<JobResumeCount> getJobResumeCounts(int companyId) {
        List<JobPosition> jobPositions = jobPositionRepository.findWithResumesByCompany_Id(companyId);

        return jobPositions.stream()
                .map(jp -> new JobResumeCount(jp.getTitle(), jp.getResumes().size()))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobNameIdDTO> getAllJobNamesAndIds(int userId) {
        int companyId = userRepository.findCompanyIdById(userId).orElse(-1);
        List<JobPosition> jobPositions = jobPositionRepository.findByCompanyId(companyId);

        return jobPositions.stream()
                .map(jp -> new JobNameIdDTO(jp.getId(), jp.getTitle()))
                .collect(Collectors.toList());
    }
}
