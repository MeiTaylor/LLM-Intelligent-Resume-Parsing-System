package com.ylw.backend.service.impl;

import com.ylw.backend.model.Company;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.model.User;
import com.ylw.backend.repository.CompanyRepository;
import com.ylw.backend.repository.JobPositionRepository;
import com.ylw.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements CompanyServiceInterface {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobPositionRepository jobPositionRepository;

    @Autowired
    public CompanyService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                          CompanyRepository companyRepository, JobPositionRepository jobPositionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.jobPositionRepository = jobPositionRepository;
    }

    @Override
    public Company getCompanyById(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public List<JobPosition> getJobPositionsByCompanyId(int companyId) {
        return jobPositionRepository.findByCompanyId(companyId);
    }

}
