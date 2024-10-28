package com.ylw.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email; // 个人邮箱

    private String phoneNumber; // 手机号

    private int age;

    private String gender;

    private String jobIntention; // 求职意向岗位

    private String highestEducation; // 最高学历

    private String major; // 专业

    private String graduatedFrom; // 毕业院校

    private String graduatedFromLevel; // 毕业院校等级

    private String selfEvaluation; // 自我评价

    private int totalWorkYears; // 工作总时间

    // 关系映射 - 简历
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;

    // 关系映射 - 个人资料
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant_profile_id", referencedColumnName = "id")
    private ApplicantProfile applicantProfile;

    // 关系映射 - 获奖荣誉
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<Award> awards;

    // 关系映射 - 工作经历
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<WorkExperience> workExperiences;

    // 关系映射 - 技能证书
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<SkillCertificate> skillCertificates;

    // 关系映射 - 教育背景
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<EducationBackground> educationBackgrounds;

    // Getters and Setters
    // 注意：可以使用 Lombok 来简化代码
    // 例如 @Getter, @Setter 等注解来自动生成以下代码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJobIntention() {
        return jobIntention;
    }

    public void setJobIntention(String jobIntention) {
        this.jobIntention = jobIntention;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGraduatedFrom() {
        return graduatedFrom;
    }

    public void setGraduatedFrom(String graduatedFrom) {
        this.graduatedFrom = graduatedFrom;
    }

    public String getGraduatedFromLevel() {
        return graduatedFromLevel;
    }

    public void setGraduatedFromLevel(String graduatedFromLevel) {
        this.graduatedFromLevel = graduatedFromLevel;
    }

    public String getSelfEvaluation() {
        return selfEvaluation;
    }

    public void setSelfEvaluation(String selfEvaluation) {
        this.selfEvaluation = selfEvaluation;
    }

    public int getTotalWorkYears() {
        return totalWorkYears;
    }

    public void setTotalWorkYears(int totalWorkYears) {
        this.totalWorkYears = totalWorkYears;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public List<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(List<WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public List<SkillCertificate> getSkillCertificates() {
        return skillCertificates;
    }

    public void setSkillCertificates(List<SkillCertificate> skillCertificates) {
        this.skillCertificates = skillCertificates;
    }

    public List<EducationBackground> getEducationBackgrounds() {
        return educationBackgrounds;
    }

    public void setEducationBackgrounds(List<EducationBackground> educationBackgrounds) {
        this.educationBackgrounds = educationBackgrounds;
    }
}
