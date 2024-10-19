package com.ylw.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class AchievementsAndHighlights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "achievementsAndHighlights", cascade = CascadeType.ALL)
    private List<Characteristic> characteristics;

    @Column(name = "applicant_profile_id", insertable = false, updatable = false)
    private int applicantProfileId;

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

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public int getApplicantProfileId() {
        return applicantProfileId;
    }

    public void setApplicantProfileId(int applicantProfileId) {
        this.applicantProfileId = applicantProfileId;
    }

    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }
}
