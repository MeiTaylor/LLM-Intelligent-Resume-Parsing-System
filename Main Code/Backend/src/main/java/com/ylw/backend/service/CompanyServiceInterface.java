package com.ylw.backend.service;

import com.ylw.backend.model.Company;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.dto.LoginModelClass;
import java.util.List;

public interface CompanyServiceInterface {

    Company getCompanyById(int id);

    List<JobPosition> getJobPositionsByCompanyId(int companyId);
}
