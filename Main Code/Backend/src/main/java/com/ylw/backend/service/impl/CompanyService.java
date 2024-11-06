package com.ylw.backend.service.impl;

import com.ylw.backend.model.*;
import com.ylw.backend.dto.*;
import com.ylw.backend.repository.*;
import com.ylw.backend.repository.CompanyRepository;
import com.ylw.backend.service.CompanyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService implements CompanyServiceInterface {

    private final CompanyRepository companyRepository;
    private final JobPositionRepository jobPositionRepository;
    private final UserRepository userRepository;
    private final JobPositionService jobPositionService;
    private final WeeklyStateService weeklyStateService;
    private final ResumeRepository resumeRepository;
    private final ResumeService resumeService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, JobPositionRepository jobPositionRepository, UserRepository userRepository, JobPositionService jobPositionService, WeeklyStateService weeklyStateService, ResumeRepository resumeRepository, ResumeService resumeService) {
        this.companyRepository = companyRepository;
        this.jobPositionRepository = jobPositionRepository;
        this.userRepository = userRepository;
        this.jobPositionService = jobPositionService;
        this.weeklyStateService = weeklyStateService;
        this.resumeRepository = resumeRepository;
        this.resumeService = resumeService;
    }

    @Override
    public InfoForHomeModelClass getInfoForHome(int userId) {
        Company company = companyRepository.findByUsers_Id(userId).orElse(null);
        InfoForHomeModelClass infoForHomeModelClass = new InfoForHomeModelClass();
        if (company == null) {
            return infoForHomeModelClass;
        }
        int companyId = company.getId();
        List<JobPosition> jobPositions = jobPositionRepository.findByCompany_Users_Id(userId);
        List<Resume> resumes = resumeRepository.findByCompanyId(userId);
        infoForHomeModelClass.totalResumes = resumes.size();
        infoForHomeModelClass.totalJobs = jobPositions.size();
        infoForHomeModelClass.weeklyStates = weeklyStateService.getWeeklyStates(companyId);
        infoForHomeModelClass.resumeHistory = resumeService.getBriefHomeResumeInfo(companyId);
        infoForHomeModelClass.jobResumeCounts = jobPositionService.getJobResumeCounts(companyId);
        return infoForHomeModelClass;
    }

}
