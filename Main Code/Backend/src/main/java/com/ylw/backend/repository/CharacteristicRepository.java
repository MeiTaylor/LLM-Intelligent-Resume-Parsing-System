package com.ylw.backend.repository;

import com.ylw.backend.model.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Integer> {
    List<Characteristic> findByApplicantProfileId(int applicantProfileId);
}
