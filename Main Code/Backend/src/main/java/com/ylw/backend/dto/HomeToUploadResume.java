package com.ylw.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeToUploadResume {
    public List<JobInfoForUpload> uploadNeedJobsInfo;
}

