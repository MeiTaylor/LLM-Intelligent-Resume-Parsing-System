package com.ylw.backend.service.impl;
import com.ylw.backend.dto.HomeJobCount;
import com.ylw.backend.dto.HomeResumeCount;
import com.ylw.backend.dto.HomeWeeklyState;
import com.ylw.backend.model.Company;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.model.Resume;
import com.ylw.backend.repository.CompanyRepository;
import com.ylw.backend.repository.JobPositionRepository;
import com.ylw.backend.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class WeeklyStateService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    public WeeklyStateService(CompanyRepository companyRepository, JobPositionRepository jobPositionRepository, ResumeRepository resumeRepository) {
        this.companyRepository = companyRepository;
        this.jobPositionRepository = jobPositionRepository;
        this.resumeRepository = resumeRepository;
    }

    //private ResumeRepository resumeRepository;
    public HomeWeeklyState getWeeklyStates(int companyId) {

        // 获取过去七天的日期列表（包含今天）
        List<String> dateList = IntStream.range(0, 7)
                .mapToObj(days -> LocalDate.now().minusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .toList();

        // 获取公司下的所有岗位和简历
        List<JobPosition> allJobPositions = jobPositionRepository.findByCompanyId(companyId);
        List<Resume> allResumes = resumeRepository.findByCompanyId(companyId);

        // 创建每周状态对象
        HomeWeeklyState weeklyStates = new HomeWeeklyState();

        // 计算 JobCounts
        weeklyStates.jobCountList = dateList.stream()
                .map(date1 -> {
                    long count1 = allJobPositions.stream()
                            .filter(jp -> jp.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(date1))
                            .count();
                    return new HomeJobCount(date1, (int) count1);
                })
                .collect(Collectors.toList());

        // 计算 resumeCounts
        weeklyStates.resumeCountList = dateList.stream()
                .map(date -> {
                    long count = allResumes.stream()
                            .filter(r -> r.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(date))
                            .count();
                    return new HomeResumeCount(date, (int) count);
                })
                .collect(Collectors.toList());

        return weeklyStates;
    }
}
