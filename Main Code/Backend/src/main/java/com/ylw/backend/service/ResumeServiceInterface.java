// ResumeServiceInterface.java
package com.ylw.backend.service;

import com.ylw.backend.model.Applicant;

public interface ResumeServiceInterface {
    /**
     * 解析简历文件
     * @param resumePath 简历文件路径
     * @return void
     */
    Applicant parseResume(String resumePath);

    //创建一个测试读取json文件并存入数据库的方法
    Applicant parseResumeTest(String resumePath);
}