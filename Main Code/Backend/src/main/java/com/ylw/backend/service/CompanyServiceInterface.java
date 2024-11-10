package com.ylw.backend.service;

import com.ylw.backend.dto.AgeGroups;
import com.ylw.backend.dto.EducationInfoForGraphClass;
import com.ylw.backend.dto.InfoForHomeModelClass;
import com.ylw.backend.dto.WorkStability;

public interface CompanyServiceInterface {
    InfoForHomeModelClass getInfoForHome(int userId);

    EducationInfoForGraphClass ForEducationInfo(int userId);

    AgeGroups ageInfoForGraphClass(int userId);

    WorkStability workStabilityInfoForGraphClass(int userId);
}
