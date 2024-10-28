package com.ylw.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeToUploadResume {
    public List<JobInfoForUpload> uploadNeedJobsInfo;
}

