package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
