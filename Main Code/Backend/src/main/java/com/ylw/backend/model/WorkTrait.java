package com.ylw.backend.model;

import jakarta.persistence.*;

@Entity
public class WorkTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "applicant_profile_id", insertable = false, updatable = false)
    private int applicantProfileId;

    private String trait;

    @ManyToOne
    @JoinColumn(name = "applicant_profile_id")
    private ApplicantProfile applicantProfile;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicantProfileId() {
        return applicantProfileId;
    }

    public void setApplicantProfileId(int applicantProfileId) {
        this.applicantProfileId = applicantProfileId;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }
}
