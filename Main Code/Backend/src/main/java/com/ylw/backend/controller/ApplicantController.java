package com.ylw.backend.controller;

import com.ylw.backend.dto.CharacteristicDTO;
import com.ylw.backend.dto.JobMatchDTO;
import com.ylw.backend.model.Applicant;
import com.ylw.backend.service.ApplicantServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applicant")
public class ApplicantController {

    private final ApplicantServiceInterface applicantService;

    @Autowired
    public ApplicantController(ApplicantServiceInterface applicantService) {
        this.applicantService = applicantService;
    }

    /**
     * 从JSON文件创建申请人信息
     * @param jsonFilePath JSON文件的完整路径
     * @return ResponseEntity<?>
     */
    @PostMapping("/create-from-json")
    public ResponseEntity<?> createFromJsonFile(@RequestBody String jsonFilePath) {
        try {
            // 处理可能的引号包裹
            jsonFilePath = jsonFilePath.replace("\"", "");
            Applicant applicant = applicantService.createApplicantFromJsonFile(jsonFilePath);
            return ResponseEntity.ok(applicant);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Failed to create applicant from JSON file: " + e.getMessage());
        }
    }

    @PostMapping("/detailedInfo")
    public ResponseEntity<Applicant> getApplicantDetailedInfo(@RequestBody int applicantId) {
        Applicant applicant = applicantService.getApplicantDetailedInfo(applicantId);
        return ResponseEntity.ok(applicant);
    }

    @PostMapping("/graph/PersonalJobMatchScore")
    public ResponseEntity<List<JobMatchDTO>> getPersonalJobMatchScore(@RequestBody int applicantId) {
        List<JobMatchDTO> jobMatchDTOList = applicantService.getPersonalJobMatchScore(applicantId);
        return ResponseEntity.ok(jobMatchDTOList);
    }

    @PostMapping("/graph/PersonalCharacteristics")
    public ResponseEntity<List<CharacteristicDTO>> getPersonalCharacteristics(@RequestBody int applicantId) {
        List<CharacteristicDTO> characteristicDTOList = applicantService.getPersonalCharacteristics(applicantId);
        return ResponseEntity.ok(characteristicDTOList);
    }

    @PostMapping("/graph/SkillsAndExperiences")
    public ResponseEntity<List<CharacteristicDTO>> getSkillsAndExperiences(@RequestBody int applicantId) {
        List<CharacteristicDTO> characteristicDTOList = applicantService.getSkillsAndExperiences(applicantId);
        return ResponseEntity.ok(characteristicDTOList);
    }

    @PostMapping("/graph/AchievementsAndHighlights")
    public ResponseEntity<List<CharacteristicDTO>> getAchievementsAndHighlights(@RequestBody int applicantId) {
        List<CharacteristicDTO> characteristicDTOList = applicantService.getAchievementsAndHighlights(applicantId);
        return ResponseEntity.ok(characteristicDTOList);
    }

}
