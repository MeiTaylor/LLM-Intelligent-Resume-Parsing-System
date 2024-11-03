package com.ylw.backend.repository;

import com.ylw.backend.dto.BriefHomeResumeInfo;
import com.ylw.backend.model.Resume;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    List<Resume> findByCompanyId(int companyId);

    //这个是用来返回首页的简历信息内容
    default List<BriefHomeResumeInfo> findResumeHistoryByCompanyId(int companyId) {
        List<Resume> resumes = findByCompanyId(companyId);
        return resumes.stream()
                .map(r -> new BriefHomeResumeInfo(
                        r.getId(),
                        r.getApplicant().getName(),
                        r.getJobPosition().getTitle(),
                        r.getCreatedDate().toString(), // 这里的并没有用STRING类型，而是用的Date类型
                        r.getApplicant().getHighestEducation()
                ))
                .collect(Collectors.toList());
    }
}