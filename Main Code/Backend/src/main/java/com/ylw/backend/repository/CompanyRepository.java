package com.ylw.backend.repository;

import com.ylw.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    // 根据公司名称获取公司
    Optional<Company> findByName(String name);

    // 根据用户ID获取公司信息
    Optional<Company> findByUsers_Id(int userId);
}
