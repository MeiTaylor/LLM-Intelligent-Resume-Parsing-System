package com.ylw.backend.service;

import com.ylw.backend.model.Applicant;
import com.ylw.backend.model.ApplicantProfile;

public interface ApplicantServiceInterface {
    Applicant createApplicantFromJsonFile(String jsonFilePath);

    void parseCharacteristicsJson(ApplicantProfile applicantProfile, String talentJsonPath);

    void parseJobMatchingJson(ApplicantProfile applicantProfile, String jsonFilePath);
}