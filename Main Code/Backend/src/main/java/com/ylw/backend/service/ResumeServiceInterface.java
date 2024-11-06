// ResumeServiceInterface.java
package com.ylw.backend.service;

public interface ResumeServiceInterface {
    /**
     * 解析简历文件
     * @param resumePath 简历文件路径
     * @return void
     */
    void parseResume(String resumePath);
}