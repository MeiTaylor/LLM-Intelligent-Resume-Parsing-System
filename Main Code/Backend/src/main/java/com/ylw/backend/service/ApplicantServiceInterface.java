package com.ylw.backend.service;

import com.ylw.backend.model.Applicant;

public interface ApplicantServiceInterface {
    Applicant createApplicantFromJsonFile(String jsonFilePath);
}