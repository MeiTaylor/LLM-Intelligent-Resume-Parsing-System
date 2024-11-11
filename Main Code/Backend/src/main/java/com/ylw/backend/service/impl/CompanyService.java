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
        List<JobPosition> jobPositions = jobPositionRepository.findByCompanyId(companyId);
        List<Resume> resumes = resumeRepository.findByCompanyId(userId);
        infoForHomeModelClass.totalResumes = resumes.size();
        infoForHomeModelClass.totalJobs = jobPositions.size();
        infoForHomeModelClass.weeklyStates = weeklyStateService.getWeeklyStates(companyId);
        infoForHomeModelClass.resumeHistory = resumeService.getBriefHomeResumeInfo(companyId);
        infoForHomeModelClass.jobResumeCounts = jobPositionService.getJobResumeCounts(companyId);
        return infoForHomeModelClass;
    }

    @Override
    public EducationInfoForGraphClass ForEducationInfo(int userId) {
        Company company = companyRepository.findByUsers_Id(userId).orElse(null);
        EducationInfoForGraphClass educationInfoForGraphClass = new EducationInfoForGraphClass();
        if (company == null) {
            return educationInfoForGraphClass;
        }
        int companyId = company.getId();
        List<Resume> resumes = resumeRepository.findByCompanyId(companyId);

        // 初始化计数器
        HighestEducation highestEducationCounts = new HighestEducation();
        GraduationSchoolsLevel graduationSchoolsLevelCounts = new GraduationSchoolsLevel();

        // 遍历所有简历
        for (Resume resume : resumes) {
            Applicant applicant = resume.getApplicant();
            if (applicant == null) {
                continue;
            }
            // 统计最高学历
            String highestEducation = applicant.getHighestEducation();
            if (highestEducation == null) {
                highestEducation = "未知"; // 提供默认值
            }

            // 统计最高学历
            switch (highestEducation) {
                case "无":
                case "小学":
                case "初中":
                case "高中":
                case "中专":
                    highestEducationCounts.setHighSchoolOrLess(highestEducationCounts.getHighSchoolOrLess() + 1);
                    break;
                case "大专":
                    highestEducationCounts.setJuniorCollege(highestEducationCounts.getJuniorCollege() + 1);
                    break;
                case "本科":
                    highestEducationCounts.setBachelor(highestEducationCounts.getBachelor() + 1);
                    break;
                case "硕士":
                    highestEducationCounts.setMaster(highestEducationCounts.getMaster() + 1);
                    break;
                case "博士":
                    highestEducationCounts.setDoctor(highestEducationCounts.getDoctor() + 1);
                    break;
                default:
                    highestEducationCounts.setHighSchoolOrLess(highestEducationCounts.getHighSchoolOrLess() + 1);
                    break;
            }

            // 统计毕业院校等级
            String graduatedFromLevel = applicant.getGraduatedFromLevel();
            if (graduatedFromLevel == null) {
                graduatedFromLevel = "未知"; // 提供默认值
            }
            switch (graduatedFromLevel) {
                case "985":
                    graduationSchoolsLevelCounts.set_985(graduationSchoolsLevelCounts.get_985() + 1);
                    break;
                case "211":
                    graduationSchoolsLevelCounts.set_211(graduationSchoolsLevelCounts.get_211() + 1);
                    break;
                case "普通一本":
                    graduationSchoolsLevelCounts.setOrdinaryFirstClass(graduationSchoolsLevelCounts.getOrdinaryFirstClass() + 1);
                    break;
                case "一本以下":
                default:
                    graduationSchoolsLevelCounts.setSecondClassOrBelow(graduationSchoolsLevelCounts.getSecondClassOrBelow() + 1);
                    break;
            }
        }

        // 设置结果
        educationInfoForGraphClass.setHighestEducation(highestEducationCounts);
        educationInfoForGraphClass.setGraduationSchoolsLevel(graduationSchoolsLevelCounts);

        return educationInfoForGraphClass;
    }

    @Override
    public AgeGroups ageInfoForGraphClass(int userId) {
        Company company = companyRepository.findByUsers_Id(userId).orElse(null);
        AgeGroups ageGroups = new AgeGroups();
        if (company == null) {
            return ageGroups;
        }
        int companyId = company.getId();
        List<Resume> resumes = resumeRepository.findByCompanyId(companyId);

        // 遍历所有简历
        for (Resume resume : resumes) {
            Applicant applicant = resume.getApplicant();
            if (applicant == null) {
                continue;
            }

            int age = applicant.getAge();
            if (age >= 18 && age < 22) {
                ageGroups.setAge18_22(ageGroups.getAge18_22() + 1);
            } else if (age >= 22 && age < 25) {
                ageGroups.setAge22_25(ageGroups.getAge22_25() + 1);
            } else if (age >= 25 && age < 30) {
                ageGroups.setAge25_30(ageGroups.getAge25_30() + 1);
            } else if (age >= 30 && age < 35) {
                ageGroups.setAge30_35(ageGroups.getAge30_35() + 1);
            } else if (age >= 35) {
                ageGroups.setAge35AndAbove(ageGroups.getAge35AndAbove() + 1);
            }
        }
        return ageGroups;
    }

    @Override
    public WorkStability workStabilityInfoForGraphClass(int userId) {
        Company company = companyRepository.findByUsers_Id(userId).orElse(null);
        WorkStability workStability = new WorkStability();
        if (company == null) {
            return workStability;
        }
        int companyId = company.getId();
        List<Resume> resumes = resumeRepository.findByCompanyId(companyId);

        // 初始化计数器
        WorkStability workStabilityCounts = new WorkStability();

        // 遍历所有简历
        for (Resume resume : resumes) {
            Applicant applicant = resume.getApplicant();
            if (applicant == null) {
                continue;
            }

            // 统计工作稳定性
            switch (applicant.getWorkStability()) {
                case "高":
                    workStabilityCounts.setHigh(workStabilityCounts.getHigh() + 1);
                    break;
                case "中上":
                    workStabilityCounts.setMediumHigh(workStabilityCounts.getMediumHigh() + 1);
                    break;
                case "中":
                    workStabilityCounts.setMedium(workStabilityCounts.getMedium() + 1);
                    break;
                case "中下":
                    workStabilityCounts.setMediumLow(workStabilityCounts.getMediumLow() + 1);
                    break;
                case "低":
                    workStabilityCounts.setLow(workStabilityCounts.getLow() + 1);
                    break;
            }
        }

        return workStability;
    }

    @Override
    public JobMatchScores getJobMatchScoresForGraph(int userId) {
        Company company = companyRepository.findByUsers_Id(userId).orElse(null);
        JobMatchScores jobMatchScores = new JobMatchScores();
        if (company == null) {
            return jobMatchScores;
        }
        int companyId = company.getId();
        List<Resume> resumes = resumeRepository.findByCompanyId(companyId);

        // 遍历所有简历
        for (Resume resume : resumes) {
            ApplicantProfile applicantProfile = resume.getApplicant().getApplicantProfile();
            if (applicantProfile == null || applicantProfile.getJobMatches() == null || applicantProfile.getJobMatches().isEmpty()) {
                continue;
            }

            // 找到每个 JobMatches 集合中的最大 Score 并添加到列表中
            int maxScore = applicantProfile.getJobMatches().stream()
                    .mapToInt(JobMatch::getScore)
                    .max()
                    .orElse(0);

            if (maxScore < 60) {
                jobMatchScores.setBelow60(jobMatchScores.getBelow60() + 1);
            } else if (maxScore < 70) {
                jobMatchScores.setRange60_70(jobMatchScores.getRange60_70() + 1);
            } else if (maxScore < 80) {
                jobMatchScores.setRange70_80(jobMatchScores.getRange70_80() + 1);
            } else if (maxScore < 90) {
                jobMatchScores.setRange80_90(jobMatchScores.getRange80_90() + 1);
            } else {
                jobMatchScores.setRange90_100(jobMatchScores.getRange90_100() + 1);
            }
        }

        return jobMatchScores;
    }



}
