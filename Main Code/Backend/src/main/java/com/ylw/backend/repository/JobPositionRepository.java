package com.ylw.backend.repository;

import com.ylw.backend.model.JobPosition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Integer> {

    // 根据公司 ID 查找所有职位
    List<JobPosition> findByCompanyId(int companyId);

    List<JobPosition> findByCompany_Users_Id(int userId);

    // 加载 JobPosition 时自动加载 resumes
    @EntityGraph(value = "JobPosition.resumes")
    List<JobPosition> findWithResumesByCompany_Id(int companyId);

}
