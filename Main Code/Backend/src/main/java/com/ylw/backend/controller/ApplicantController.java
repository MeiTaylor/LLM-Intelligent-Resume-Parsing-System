package com.ylw.backend.controller;

import com.ylw.backend.model.Applicant;
import com.ylw.backend.service.ApplicantServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}


//package com.ylw.backend.controller;
//
//public class ApplicantController {
//    //简历上传二次修改接口
//
//    //获取申请人分数信息接口
//}
