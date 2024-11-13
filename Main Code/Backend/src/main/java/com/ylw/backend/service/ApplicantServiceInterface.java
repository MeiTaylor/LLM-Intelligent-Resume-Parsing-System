package com.ylw.backend.service;

import com.ylw.backend.dto.CharacteristicDTO;
import com.ylw.backend.dto.JobMatchDTO;
import com.ylw.backend.model.Applicant;
import com.ylw.backend.model.ApplicantProfile;

import java.util.List;

public interface ApplicantServiceInterface {
    Applicant createApplicantFromJsonFile(String jsonFilePath);

    Applicant getApplicantDetailedInfo(int applicantId);

    void parseCharacteristicsJson(ApplicantProfile applicantProfile, String talentJsonPath);

    void parseJobMatchingJson(ApplicantProfile applicantProfile, String jsonFilePath);

    List<JobMatchDTO> getPersonalJobMatchScore(int applicantId);

    List<CharacteristicDTO> getPersonalCharacteristics(int applicantId);

    List<CharacteristicDTO> getSkillsAndExperiences(int applicantId);

    List<CharacteristicDTO> getAchievementsAndHighlights(int applicantId);
}