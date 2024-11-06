package com.ylw.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylw.backend.model.*;
import com.ylw.backend.service.ApplicantServiceInterface;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApplicantService implements ApplicantServiceInterface {

    @Override
    public Applicant createApplicantFromJsonFile(String jsonFilePath) {
        try {
            // 读取JSON文件内容
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(jsonContent, Map.class);

            Applicant applicant = new Applicant();

            // 设置基本信息
            Map<String, String> personalInfo = (Map<String, String>) jsonMap.get("个人信息");
            applicant.setName(personalInfo.get("姓名"));
            applicant.setEmail(personalInfo.get("电子邮箱"));
            applicant.setPhoneNumber(personalInfo.get("电话"));
            applicant.setAge(Integer.parseInt(personalInfo.get("年龄")));
            applicant.setGender(personalInfo.get("性别"));

            // 设置求职意向
            applicant.setJobIntention((String) jsonMap.get("求职意向"));

            // 创建教育背景
            Map<String, String> educationInfo = (Map<String, String>) jsonMap.get("教育背景");
            EducationBackground educationBackground = new EducationBackground();
            educationBackground.setSchool(educationInfo.get("毕业院校"));
            educationBackground.setMajor(educationInfo.get("专业"));
            educationBackground.setTime(""); // 需要根据实际数据格式设置
            educationBackground.setApplicant(applicant);

            List<EducationBackground> educationBackgrounds = new ArrayList<>();
            educationBackgrounds.add(educationBackground);
            applicant.setEducationBackgrounds(educationBackgrounds);

            // 设置工作经历
            List<Map<String, String>> workExperienceList = (List<Map<String, String>>) jsonMap.get("工作经历");
            List<WorkExperience> workExperiences = new ArrayList<>();

            for (Map<String, String> exp : workExperienceList) {
                WorkExperience workExperience = new WorkExperience();
                workExperience.setPosition(exp.get("职位"));
                workExperience.setTask(exp.get("职责"));
                workExperience.setTime(exp.get("开始时间") + " - " + exp.get("结束时间"));
                workExperience.setCompanyName(""); // 需要根据实际数据设置
                workExperience.setApplicant(applicant);
                workExperiences.add(workExperience);
            }
            applicant.setWorkExperiences(workExperiences);

            // 设置技能证书
            List<String> skillsAndCerts = (List<String>) jsonMap.get("技能和证书");
            List<SkillCertificate> skillCertificates = new ArrayList<>();
            for (String skill : skillsAndCerts) {
                SkillCertificate skillCertificate = new SkillCertificate();
                skillCertificate.setSkillName(skill);
                skillCertificate.setApplicant(applicant);
                skillCertificates.add(skillCertificate);
            }
            applicant.setSkillCertificates(skillCertificates);

            // 设置获奖荣誉
            List<String> awards = (List<String>) jsonMap.get("获奖和荣誉");
            List<Award> awardList = new ArrayList<>();
            for (String awardName : awards) {
                Award award = new Award();
                award.setAwardName(awardName);
                award.setApplicant(applicant);
                awardList.add(award);
            }
            applicant.setAwards(awardList);

            // 设置个人资料（ApplicantProfile）
            ApplicantProfile profile = new ApplicantProfile();
            Map<String, String> stability = (Map<String, String>) jsonMap.get("工作稳定性");
            profile.setWorkStability(stability.get("等级"));
            profile.setStabilityReason(stability.get("原因"));

            // 设置工作特征
            List<String> workTraits = (List<String>) jsonMap.get("工作特性标签");
            List<WorkTrait> workTraitList = new ArrayList<>();
            for (String trait : workTraits) {
                WorkTrait workTrait = new WorkTrait();
                // 注意：WorkTrait的具体属性需要根据实际类定义设置
                workTraitList.add(workTrait);
            }
            profile.setWorkTraits(workTraitList);

            // 初始化其他集合
            profile.setJobMatches(new ArrayList<>());
            profile.setCharacteristics(new ArrayList<>());

            profile.setApplicant(applicant);
            applicant.setApplicantProfile(profile);

            return applicant;

        } catch (Exception e) {
            throw new RuntimeException("Failed to read or parse JSON file: " + jsonFilePath, e);
        }
    }
}