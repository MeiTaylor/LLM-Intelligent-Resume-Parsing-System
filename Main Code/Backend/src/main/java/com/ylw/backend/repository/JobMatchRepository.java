package com.ylw.backend.repository;

import com.ylw.backend.model.JobMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobMatchRepository extends JpaRepository<JobMatch, Integer> {
    List<JobMatch> findByApplicantProfileId(int applicantProfileId);
}
