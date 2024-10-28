package com.ylw.backend.service;

import com.ylw.backend.dto.HomeToUploadResume;

public interface IJobPositionService {
    HomeToUploadResume uploadJobInfo(int userId);
}
