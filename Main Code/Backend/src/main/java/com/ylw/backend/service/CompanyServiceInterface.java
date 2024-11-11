package com.ylw.backend.service;

import com.ylw.backend.dto.*;

public interface CompanyServiceInterface {
    InfoForHomeModelClass getInfoForHome(int userId);

    EducationInfoForGraphClass ForEducationInfo(int userId);

    AgeGroups ageInfoForGraphClass(int userId);

    WorkStability workStabilityInfoForGraphClass(int userId);

    JobMatchScores getJobMatchScoresForGraph(int userId);
}
