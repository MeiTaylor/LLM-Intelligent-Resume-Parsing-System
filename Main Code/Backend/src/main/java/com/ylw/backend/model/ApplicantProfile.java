package com.ylw.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ApplicantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "applicant_id")
    private int applicantId;

    private String matchingReason; // 给出此工作稳定性判断的原因

    private int matchingScore; // 人岗匹配程度分数

    private String workStability; // 工作稳定性的程度

    private String stabilityReason; // 工作稳定性判断的原因

    // 导航属性 - 求职匹配
    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL)
    private List<JobMatch> jobMatches;

    // 导航属性 - 工作特征
    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL)
    private List<WorkTrait> workTraits;

    // 不映射的属性
    @Transient
    private List<String> workTraitList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_characteristics_id", referencedColumnName = "id")
    private PersonalCharacteristics personalCharacteristics;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "skills_and_experiences_id", referencedColumnName = "id")
    private SkillsAndExperiences skillsAndExperiences;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "achievements_and_highlights_id", referencedColumnName = "id")
    private AchievementsAndHighlights achievementsAndHighlights;

    // 导航属性 - 申请人
    @ManyToOne
    @JoinColumn(name = "applicant_id", insertable = false, updatable = false)
    private Applicant applicant;

    // Getters and Setters
    // Lombok 可以用来简化这些样板代码
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public String getMatchingReason() {
        return matchingReason;
    }

    public void setMatchingReason(String matchingReason) {
        this.matchingReason = matchingReason;
    }

    public int getMatchingScore() {
        return matchingScore;
    }

    public void setMatchingScore(int matchingScore) {
        this.matchingScore = matchingScore;
    }

    public String getWorkStability() {
        return workStability;
    }

    public void setWorkStability(String workStability) {
        this.workStability = workStability;
    }

    public String getStabilityReason() {
        return stabilityReason;
    }

    public void setStabilityReason(String stabilityReason) {
        this.stabilityReason = stabilityReason;
    }

    public List<JobMatch> getJobMatches() {
        return jobMatches;
    }

    public void setJobMatches(List<JobMatch> jobMatches) {
        this.jobMatches = jobMatches;
    }

    public List<WorkTrait> getWorkTraits() {
        return workTraits;
    }

    public void setWorkTraits(List<WorkTrait> workTraits) {
        this.workTraits = workTraits;
    }

    public List<String> getWorkTraitList() {
        return workTraits != null ? workTraits.stream().map(WorkTrait::getTrait).toList() : null;
    }

    public PersonalCharacteristics getPersonalCharacteristics() {
        return personalCharacteristics;
    }

    public void setPersonalCharacteristics(PersonalCharacteristics personalCharacteristics) {
        this.personalCharacteristics = personalCharacteristics;
    }

    public SkillsAndExperiences getSkillsAndExperiences() {
        return skillsAndExperiences;
    }

    public void setSkillsAndExperiences(SkillsAndExperiences skillsAndExperiences) {
        this.skillsAndExperiences = skillsAndExperiences;
    }

    public AchievementsAndHighlights getAchievementsAndHighlights() {
        return achievementsAndHighlights;
    }

    public void setAchievementsAndHighlights(AchievementsAndHighlights achievementsAndHighlights) {
        this.achievementsAndHighlights = achievementsAndHighlights;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
