// ResumeServiceInterface.java
package com.ylw.backend.service;

import com.ylw.backend.dto.ApplicantDTO;
import com.ylw.backend.model.Applicant;
import org.springframework.core.io.Resource;

public interface ResumeServiceInterface {
    /**
     * 解析简历文件
     * @param resumePath 简历文件路径
     * @return void
     */
    Applicant parseResume(String resumePath);

    //创建一个测试读取json文件并存入数据库的方法
    Applicant parseResumeTest(String resumePath);

    void createAndSaveResume(String resumePath, Applicant applicant, int jobId, int userId);

    Resource getResumeImage(int resumeId);

    Applicant updateApplicant(ApplicantDTO updatedApplicant);

}