package com.ylw.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylw.backend.dto.CharacteristicDTO;
import com.ylw.backend.dto.JobMatchDTO;
import com.ylw.backend.model.*;
import com.ylw.backend.repository.ApplicantRepository;
import com.ylw.backend.repository.CharacteristicRepository;
import com.ylw.backend.repository.JobMatchRepository;
import com.ylw.backend.service.ApplicantServiceInterface;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.io.FileNotFoundException;


@Service
public class ApplicantService implements ApplicantServiceInterface {

    private final JobMatchRepository jobMatchRepository;
    private final CharacteristicRepository characteristicRepository;
    private final ApplicantRepository applicantRepository;

    public ApplicantService(JobMatchRepository jobMatchRepository,
                            CharacteristicRepository characteristicRepository,
                            ApplicantRepository applicantRepository) {
        this.jobMatchRepository = jobMatchRepository;
        this.characteristicRepository = characteristicRepository;
        this.applicantRepository = applicantRepository;
    }

    @Override
    public Applicant createApplicantFromJsonFile(String jsonFilePath) {
        try {
            // 检查文件名是否合法
            Path path = Paths.get(jsonFilePath);
            String fileName = path.getFileName().toString();

            // 如果文件名包含其他扩展名（如.pdf.json）
            if (fileName.contains(".")) {
                // 获取最后一个点之前的所有内容
                String baseName = fileName.substring(0, fileName.indexOf("."));
                // 构建新的文件名，只保留基础名称和.json扩展名
                String newFileName = baseName + ".json";
                // 获取父目录路径
                String parentPath = path.getParent().toString();
                // 构建新的完整路径
                String newPath = parentPath + File.separator + newFileName;
                path = Paths.get(newPath);
            }

            // 检查文件是否存在
            if (!Files.exists(path)) {
                throw new FileNotFoundException("JSON file not found: " + path);
            }

            // 读取JSON文件内容
            String jsonContent = new String(Files.readAllBytes(path));

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(jsonContent, Map.class);

            Applicant applicant = new Applicant();

            // 设置基本信息
            Map<String, String> personalInfo = (Map<String, String>) jsonMap.get("个人信息");
            applicant.setName(personalInfo.get("姓名"));
            applicant.setEmail(personalInfo.get("电子邮箱"));
            applicant.setPhoneNumber(personalInfo.get("电话"));
            Object ageValue = personalInfo.get("年龄");

            if (ageValue instanceof Integer) {
                applicant.setAge((Integer) ageValue); // 如果是 Integer 类型，直接设置
            } else if (ageValue instanceof String) {
                applicant.setAge(Integer.parseInt((String) ageValue)); // 如果是 String 类型，转换为 Integer
            } else {
                throw new IllegalArgumentException("年龄字段的类型不正确: " + ageValue);
            }

            applicant.setGender(personalInfo.get("性别"));

            // 设置求职意向
            applicant.setJobIntention((String) jsonMap.get("求职意向"));

            Map<String, String> educationInfo = (Map<String, String>) jsonMap.get("教育背景");
            applicant.setHighestEducation(educationInfo.get("最高学历"));
            applicant.setMajor(educationInfo.get("专业"));
            applicant.setGraduatedFrom(educationInfo.get("毕业院校"));
            applicant.setGraduatedFromLevel(educationInfo.get("院校等级"));
            applicant.setSelfEvaluation((String) jsonMap.get("自我评价"));


//            // 创建教育背景
//            Map<String, String> educationInfo = (Map<String, String>) jsonMap.get("教育背景");
//            EducationBackground educationBackground = new EducationBackground();
//            educationBackground.setSchool(educationInfo.get("毕业院校"));
//            educationBackground.setMajor(educationInfo.get("专业"));
//            educationBackground.setTime(""); // 需要根据实际数据格式设置
//            educationBackground.setApplicant(applicant);
//
//            List<EducationBackground> educationBackgrounds = new ArrayList<>();
//            educationBackgrounds.add(educationBackground);
//            applicant.setEducationBackgrounds(educationBackgrounds);

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

            Object totalWorkYearsValue = jsonMap.get("工作总时间");
            if (totalWorkYearsValue instanceof Integer) {
                applicant.setTotalWorkYears((Integer) totalWorkYearsValue);
            } else {
                try {
                    applicant.setTotalWorkYears(Integer.parseInt((String) totalWorkYearsValue));
                } catch (NumberFormatException e) {
                    applicant.setTotalWorkYears(0);
                }
            }

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

            Map<String, String> stability = (Map<String, String>) jsonMap.get("工作稳定性");
            applicant.setWorkStability(stability.get("等级"));
            applicant.setWorkStabilityReason(stability.get("原因"));

            // 设置个人资料（ApplicantProfile）
            ApplicantProfile profile = new ApplicantProfile();
            //profile.setApplicant(applicant); //之前没加这个

            // 设置工作特征
            List<String> workTraits = (List<String>) jsonMap.get("工作特性标签");
            List<WorkTrait> workTraitList = new ArrayList<>();
            for (String trait : workTraits) {
                WorkTrait workTrait = new WorkTrait();
                workTrait.setTrait(trait);
                workTrait.setApplicantProfile(profile);
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
            // 打印详细堆栈信息
            e.printStackTrace();

            // 抛出包含更多信息的运行时异常
            String errorMessage = String.format("Failed to read or parse JSON file: %s. Error: %s", jsonFilePath, e.getMessage());
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public Applicant getApplicantDetailedInfo(int applicantId) {
        // 从数据库获取指定申请人的信息
        Applicant applicant = applicantRepository.findById(applicantId).orElse(null);

        if (applicant == null) {
            throw new IllegalArgumentException("Applicant not found for ID: " + applicantId);
        }

        return applicant;
    }

    @Override
    public void parseCharacteristicsJson(ApplicantProfile applicantProfile, String jsonFilePath) {
        try {
            Path path = Paths.get(jsonFilePath);
            String fileName = path.getFileName().toString();

            if (fileName.contains(".")) {
                String baseName = fileName.substring(0, fileName.indexOf("."));
                String newFileName = baseName + ".json";
                String parentPath = path.getParent().toString();
                String newPath = parentPath + File.separator + newFileName;
                path = Paths.get(newPath);
            }

            if (!Files.exists(path)) {
                throw new FileNotFoundException("JSON file not found: " + path);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(path.toFile());

            // 遍历每个大类（例如 "个人特性", "技能和经验", "成就和亮点评价"）
            Iterator<String> mainCategories = rootNode.fieldNames();
            List<Characteristic> characteristics = new ArrayList<>();

            while (mainCategories.hasNext()) {
                String categoryName = mainCategories.next(); // 大类名称，例如 "个人特性"
                JsonNode categoryNode = rootNode.get(categoryName);

                if (categoryNode == null || !categoryNode.isObject()) {
                    System.err.println("Warning: Category node is null or not an object for category name: " + categoryName);
                    continue;
                }

                // 遍历每个大类中的小类（例如 "自我驱动性", "适应能力"）
                Iterator<String> subCategories = categoryNode.fieldNames();
                while (subCategories.hasNext()) {
                    String subCategoryName = subCategories.next(); // 小类名称，例如 "自我驱动性"
                    JsonNode subCategoryNode = categoryNode.get(subCategoryName);

                    if (subCategoryNode == null || !subCategoryNode.isObject()) {
                        System.err.println("Warning: Sub-category node is null or not an object for sub-category name: " + subCategoryName);
                        continue;
                    }

                    try {
                        JsonNode scoreNode = subCategoryNode.get("分数");
                        JsonNode reasonNode = subCategoryNode.get("原因");

                        if (scoreNode == null || reasonNode == null) {
                            System.err.println("Warning: Missing '分数' or '原因' in sub-category: " + subCategoryName);
                            continue;
                        }

                        Characteristic characteristic = new Characteristic();
                        characteristic.setCatagory(categoryName); // 设置大类名称
                        characteristic.setName(subCategoryName); // 设置小类名称
                        characteristic.setScore(scoreNode.asInt());
                        characteristic.setReason(reasonNode.asText());
                        characteristic.setApplicantProfile(applicantProfile);

                        characteristics.add(characteristic);
                    } catch (Exception e) {
                        System.err.println("Error processing sub-category: " + subCategoryName + " in category: " + categoryName + ". Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            applicantProfile.setCharacteristics(characteristics);

        } catch (IOException e) {
            System.err.println("Failed to read or parse JSON file: " + jsonFilePath + ". Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to read or parse JSON file: " + jsonFilePath, e);
        }
    }

    @Override
    public void parseJobMatchingJson(ApplicantProfile applicantProfile, String jsonFilePath) {
        try {
            Path path = Paths.get(jsonFilePath);
            String fileName = path.getFileName().toString();

            if (fileName.contains(".")) {
                String baseName = fileName.substring(0, fileName.indexOf("."));
                String newFileName = baseName + ".json";
                String parentPath = path.getParent().toString();
                String newPath = parentPath + File.separator + newFileName;
                path = Paths.get(newPath);
            }

            if (!Files.exists(path)) {
                throw new FileNotFoundException("JSON file not found: " + path);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(path.toFile());

            // 从 JSON 文件中提取数据并创建 JobMatch 对象
            List<JobMatch> jobMatches = new ArrayList<>();
            Iterator<String> fieldNames = rootNode.fieldNames();
            while (fieldNames.hasNext()) {
                String jobTitle = fieldNames.next();
                JsonNode jobNode = rootNode.get(jobTitle);

                if (jobNode == null) {
                    System.err.println("Warning: Job node is null for job title: " + jobTitle);
                    continue;
                }

                try {
                    JsonNode scoreNode = jobNode.get("人岗匹配程度分数");
                    JsonNode reasonNode = jobNode.get("人岗匹配的理由");

                    if (scoreNode == null || reasonNode == null) {
                        System.err.println("Warning: Missing '人岗匹配程度分数' or '人岗匹配的理由' for job title: " + jobTitle);
                        continue;
                    }

                    JobMatch jobMatch = new JobMatch();
                    jobMatch.setJobTitle(jobTitle);
                    jobMatch.setScore(scoreNode.asInt());
                    jobMatch.setReason(reasonNode.asText());
                    jobMatch.setApplicantProfile(applicantProfile);

                    jobMatches.add(jobMatch);
                } catch (Exception e) {
                    System.err.println("Error processing job title: " + jobTitle + ". Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            applicantProfile.setJobMatches(jobMatches);

        } catch (IOException e) {
            System.err.println("Failed to read or parse JSON file: " + jsonFilePath + ". Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to read or parse JSON file: " + jsonFilePath, e);
        }
    }

    @Override
    public List<JobMatchDTO> getPersonalJobMatchScore(int applicantId) {
        // 从数据库获取指定申请人的信息
        List<JobMatch> jobMatches = jobMatchRepository.findByApplicantProfileId(applicantId);
        List<JobMatchDTO> jobMatchDTOs = new ArrayList<>();

        for (JobMatch jobMatch : jobMatches) {
            JobMatchDTO jobMatchDTO = new JobMatchDTO();
            jobMatchDTO.setId(jobMatch.getId());
            jobMatchDTO.setJobTitle(jobMatch.getJobTitle());
            jobMatchDTO.setScore(jobMatch.getScore());
            jobMatchDTO.setReason(jobMatch.getReason());
            jobMatchDTOs.add(jobMatchDTO);
        }

        return jobMatchDTOs;
    }

    @Override
    public List<CharacteristicDTO> getPersonalCharacteristics(int applicantId) {
        // 从数据库获取指定申请人的信息
        List<Characteristic> characteristics = characteristicRepository.findByApplicantProfileId(applicantId);

        // 筛选出 catagory 为 "个人特性" 的 Characteristic
        List<CharacteristicDTO> characteristicDTOs = characteristics.stream()
                .filter(characteristic -> "个人特性".equals(characteristic.getCatagory()))
                .map(characteristic -> new CharacteristicDTO(
                        characteristic.getName(),
                        characteristic.getScore(),
                        characteristic.getReason()
                ))
                .collect(Collectors.toList());

        return characteristicDTOs;
    }

    @Override
    public List<CharacteristicDTO> getSkillsAndExperiences(int applicantId) {
        // 从数据库获取指定申请人的信息
        List<Characteristic> characteristics = characteristicRepository.findByApplicantProfileId(applicantId);

        // 筛选出 catagory 为 "技能和经验" 的 Characteristic
        List<CharacteristicDTO> characteristicDTOs = characteristics.stream()
                .filter(characteristic -> "技能和经验".equals(characteristic.getCatagory()))
                .map(characteristic -> new CharacteristicDTO(
                        characteristic.getName(),
                        characteristic.getScore(),
                        characteristic.getReason()
                ))
                .collect(Collectors.toList());

        return characteristicDTOs;
    }

    @Override
    public List<CharacteristicDTO> getAchievementsAndHighlights(int applicantId) {
        // 从数据库获取指定申请人的信息
        List<Characteristic> characteristics = characteristicRepository.findByApplicantProfileId(applicantId);

        // 筛选出 catagory 为 "成就和亮点评价" 的 Characteristic
        List<CharacteristicDTO> characteristicDTOs = characteristics.stream()
                .filter(characteristic -> "成就和亮点评价".equals(characteristic.getCatagory()))
                .map(characteristic -> new CharacteristicDTO(
                        characteristic.getName(),
                        characteristic.getScore(),
                        characteristic.getReason()
                ))
                .collect(Collectors.toList());

        return characteristicDTOs;
    }
}