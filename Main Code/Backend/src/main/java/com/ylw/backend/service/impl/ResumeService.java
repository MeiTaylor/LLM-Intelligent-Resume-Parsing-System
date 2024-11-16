package com.ylw.backend.service.impl;

import com.ylw.backend.config.PythonConfig;
import com.ylw.backend.dto.ApplicantDTO;
import com.ylw.backend.dto.BriefHomeResumeInfo;
import com.ylw.backend.model.Applicant;
import com.ylw.backend.model.JobPosition;
import com.ylw.backend.model.Resume;
import com.ylw.backend.repository.ApplicantRepository;
import com.ylw.backend.repository.CompanyRepository;
import com.ylw.backend.repository.JobPositionRepository;
import com.ylw.backend.repository.ResumeRepository;
import com.ylw.backend.service.ResumeServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PreDestroy;  // 添加这行导入

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ResumeService implements ResumeServiceInterface {

    private final String pythonInterpreterPath;
    private final String pythonScriptPath;

    // 创建线程池
    private final ExecutorService executorService;

    // 用于生成线程名称
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final JobPositionRepository jobPositionRepository;
    private final ApplicantService applicantService;
    private final ApplicantRepository applicantRepository;
    private final ResumeRepository resumeRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public ResumeService(PythonConfig pythonConfig, JobPositionRepository jobPositionRepository, ApplicantService applicantService, ApplicantRepository applicantRepository, ResumeRepository resumeRepository, CompanyRepository companyRepository) {
        this.pythonInterpreterPath = pythonConfig.pythonInterpreterPath();
        this.pythonScriptPath = pythonConfig.pythonScriptPath();

        this.jobPositionRepository = jobPositionRepository;
        this.applicantService = applicantService;
        this.applicantRepository = applicantRepository;
        this.resumeRepository = resumeRepository;

        // 创建自定义线程池
        this.executorService = new ThreadPoolExecutor(
                2,  // 核心线程数
                4,  // 最大线程数
                60L, // 空闲线程存活时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingQueue<>(10), // 任务队列
                r -> new Thread(r, "ResumeParser-" + threadNumber.getAndIncrement()), // 线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
        this.companyRepository = companyRepository;
    }

    @Override
    public Applicant parseResume(String resumePath) {
        try {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> runPythonScript(resumePath), executorService)
                    .exceptionally(throwable -> {
                        System.err.println("简历解析发生异常: " + throwable.getMessage());
                        throwable.printStackTrace();
                        return null;
                    });

            future.get(); // 这里会等待 Python 脚本执行完成

            // 根据Python脚本中的规则推导出生成的所有JSON文件路径
            Map<String, String> jsonFilePaths = deriveJsonFilePaths(resumePath);
            // 读取json文件内容，存数据库
            Applicant applicant = applicantService.createApplicantFromJsonFile(jsonFilePaths.get("Basic_Resume_Analysis"));
            applicantService.parseCharacteristicsJson(applicant.getApplicantProfile(), jsonFilePaths.get("GPT_Talent_Portraits"));
            applicantService.parseJobMatchingJson(applicant.getApplicantProfile(), jsonFilePaths.get("GPT_Job_Matching"));
            applicantRepository.save(applicant);

            return applicant;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("简历解析过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("简历解析过程中发生异常", e);
        }
    }

    /**
     * 创建并保存 Resume 对象
     *
     * @param resumePath 文件路径
     * @param applicant  关联的申请人
     * @return 保存后的 Resume 对象
     */
    @Override
    public Resume createAndSaveResume(String resumePath, Applicant applicant, int jobId, int userId) {
        Resume resume = new Resume();
        resume.setFilePath(resumePath);
            resume.setApplicant(applicant);
        //resume.setCreatedDate(LocalDateTime.now());
        resume.setJobPosition(jobPositionRepository.findById(jobId).orElse(null));
        resume.setCompany(companyRepository.findByUsers_Id(userId).orElse(null));
        // 保存 Resume 到数据库
        applicant.setResume(resume);
        return resumeRepository.save(resume);
    }


    @Override
    public Applicant parseResumeTest(String resumePath) {
        // 根据Python脚本中的规则推导出生成的所有JSON文件路径
        Map<String, String> jsonFilePaths = deriveJsonFilePaths(resumePath);
        // 读取json文件内容，存数据库
        Applicant applicant = applicantService.createApplicantFromJsonFile(jsonFilePaths.get("Basic_Resume_Analysis"));
        applicantService.parseCharacteristicsJson(applicant.getApplicantProfile(), jsonFilePaths.get("GPT_Talent_Portraits"));
        applicantService.parseJobMatchingJson(applicant.getApplicantProfile(), jsonFilePaths.get("GPT_Job_Matching"));
        applicantRepository.save(applicant);
        return applicant;
    }

    @Override
    public Resource getResumeImage(int resumeId) {
        try {
            // 查找 Resume
            Resume resume = resumeRepository.findById(resumeId)
                    .orElseThrow(() -> new RuntimeException("Resume not found with ID: " + resumeId));

            // 获取简历存储路径
            String resumepath = resume.getFilePath();

            // 根据简历路径解析出对应的图片路径
            Path resumeFilePath = Paths.get(resumepath);
            Path baseDir = resumeFilePath.getParent().getParent().resolve("Conversions/Image_Conversions");
            String fileNameWithoutExtension = resumeFilePath.getFileName().toString().replaceFirst("\\.[^.]+$", "");

            // 构建图片路径
            Path imagePath = baseDir.resolve(fileNameWithoutExtension + ".jpg");

            // 创建文件资源
            Resource fileResource = new UrlResource(imagePath.toUri());
            if (!fileResource.exists() || !fileResource.isReadable()) {
                throw new RuntimeException("Image not found or not readable: " + imagePath);
            }

            return fileResource;

        } catch (Exception e) {
            System.err.println("获取文件发生异常: " + e.getMessage());
            throw new RuntimeException("获取文件失败", e);
        }
    }



    @Override
    public Applicant updateApplicant(ApplicantDTO updatedApplicant) {
        // 查找现有的 Applicant
        Optional<Applicant> optionalApplicant = applicantRepository.findById(updatedApplicant.getId());

        if (optionalApplicant.isEmpty()) {
            throw new RuntimeException("Applicant with ID " + updatedApplicant.getId() + " not found.");
        }

        Applicant existingApplicant = optionalApplicant.get();

        // 更新 Applicant 的基本信息
        existingApplicant.setName(updatedApplicant.getName());
        existingApplicant.setEmail(updatedApplicant.getEmail());
        existingApplicant.setPhoneNumber(updatedApplicant.getPhoneNumber());
        existingApplicant.setAge(updatedApplicant.getAge());
        existingApplicant.setGender(updatedApplicant.getGender());
        existingApplicant.setJobIntention(updatedApplicant.getJobIntention());
        existingApplicant.setHighestEducation(updatedApplicant.getHighestEducation());
        existingApplicant.setMajor(updatedApplicant.getMajor());
        existingApplicant.setGraduatedFrom(updatedApplicant.getGraduatedFrom());
        existingApplicant.setGraduatedFromLevel(updatedApplicant.getGraduatedFromLevel());
        existingApplicant.setSelfEvaluation(updatedApplicant.getSelfEvaluation());
        existingApplicant.setTotalWorkYears(updatedApplicant.getTotalWorkYears());
        existingApplicant.setWorkStability(updatedApplicant.getWorkStability());
        existingApplicant.setWorkStabilityReason(updatedApplicant.getWorkStabilityReason());

        // 更新 Awards 集合
        if (updatedApplicant.getAwards() != null) {
            existingApplicant.getAwards().clear();
            updatedApplicant.getAwards().forEach(award -> {
                award.setApplicant(existingApplicant);  // 确保关联正确设置
                existingApplicant.getAwards().add(award);
            });
        }

        // 更新 WorkExperience 集合
        if (updatedApplicant.getWorkExperiences() != null) {
            existingApplicant.getWorkExperiences().clear();
            updatedApplicant.getWorkExperiences().forEach(workExperience -> {
                workExperience.setApplicant(existingApplicant);  // 确保关联正确设置
                existingApplicant.getWorkExperiences().add(workExperience);
            });
        }

        // 更新 SkillCertificates 集合
        if (updatedApplicant.getSkillCertificates() != null) {
            existingApplicant.getSkillCertificates().clear();
            updatedApplicant.getSkillCertificates().forEach(skillCertificate -> {
                skillCertificate.setApplicant(existingApplicant);  // 确保关联正确设置
                existingApplicant.getSkillCertificates().add(skillCertificate);
            });
        }

        // 保存更新后的 Applicant
        return applicantRepository.save(existingApplicant);
    }

    /**
     * 更新集合内容
     * @param existingList 已存在的集合
     * @param newList 新的集合
     */
    private <T> void updateCollection(List<T> existingList, List<T> newList) {
        // 删除已不再包含的项
        existingList.removeIf(existingItem -> !newList.contains(existingItem));

        // 添加新的项
        for (T newItem : newList) {
            if (!existingList.contains(newItem)) {
                existingList.add(newItem);
            }
        }
    }


    /**
     * 内部方法：运行Python脚本并传递参数
     * @param args 传递给Python脚本的参数
     */
    private void runPythonScript(String... args) {
        String[] command = new String[2 + args.length];
        command[0] = pythonInterpreterPath;
        command[1] = pythonScriptPath;
        System.arraycopy(args, 0, command, 2, args.length);

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            final Process finalProcess = process;

            // 创建两个独立的线程来读取输出流和错误流
            CompletableFuture<Void> outputFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(finalProcess.getInputStream(), "UTF-8"))) {
                    String s;
                    while ((s = stdInput.readLine()) != null) {
                        System.out.println("输出: " + s);
                    }
                } catch (IOException e) {
                    System.err.println("读取输出流时发生错误: " + e.getMessage());
                }
            }, executorService);

            CompletableFuture<Void> errorFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader stdError = new BufferedReader(new InputStreamReader(finalProcess.getErrorStream(), "UTF-8"))) {
                    String s;
                    while ((s = stdError.readLine()) != null) {
                        System.err.println("错误: " + s);
                    }
                } catch (IOException e) {
                    System.err.println("读取错误流时发生错误: " + e.getMessage());
                }
            }, executorService);

            // 等待两个流都读取完成
            CompletableFuture.allOf(outputFuture, errorFuture).join();

            // 等待进程完成
            int exitCode = finalProcess.waitFor();
            System.out.println("Python脚本退出码: " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.err.println("执行Python脚本时发生异常: " + e.getMessage());
            e.printStackTrace();
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    // 在Spring容器销毁时关闭线程池
    @PreDestroy
    public void destroy() {
        try {
            System.out.println("正在关闭简历解析服务的线程池...");
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public List<BriefHomeResumeInfo> getBriefHomeResumeInfo(int companyId) {
        // 从数据库获取包含 resumes 的 JobPosition 列表
        List<JobPosition> jobPositions = jobPositionRepository.findByCompanyId(companyId);

        // 将数据转换为 BriefHomeResumeInfo
        List<BriefHomeResumeInfo> briefHomeResumeInfoList = new ArrayList<>();

        for (JobPosition jobPosition : jobPositions) {
            for (Resume resume : jobPosition.getResumes()) {
                BriefHomeResumeInfo briefInfo = new BriefHomeResumeInfo(
                        resume.getId(),
                        resume.getApplicant().getName(),
                        jobPosition.getTitle(),
                        resume.getCreatedDate().toString(),
                        resume.getApplicant().getHighestEducation()
                );
                briefHomeResumeInfoList.add(briefInfo);
            }
        }

        return briefHomeResumeInfoList;
    }

    private Map<String, String> deriveJsonFilePaths(String resumePath) {
        // 根据Python脚本逻辑，推导生成所有JSON文件的路径
        Map<String, String> jsonFilePaths = new HashMap<>();

        // 向上跳两级目录获取 baseDir
        File resumeFile = new File(resumePath);
        File baseDir = resumeFile.getParentFile().getParentFile();

        // 分别处理三种输出目录类型
        String[] outputJsonDirs = {"Basic_Resume_Analysis", "GPT_Talent_Portraits", "GPT_Job_Matching"};

        for (String outputJsonDir : outputJsonDirs) {
            // 构建输出 JSON 文件的目录
            File jsonOutputDir = new File(baseDir, "Analysis_Results/" + outputJsonDir);
            if (!jsonOutputDir.exists()) {
                System.err.println("输出目录不存在：" + jsonOutputDir.getPath());
                jsonFilePaths.put(outputJsonDir, null);
                continue;
            }

            // 根据输入的简历文件名，生成对应的JSON文件名
            String jsonFileName = resumeFile.getName().replaceAll("\\.docx$", "") + ".json";
            String jsonFilePath = new File(jsonOutputDir, jsonFileName).getPath();
            jsonFilePaths.put(outputJsonDir, jsonFilePath);
        }

        return jsonFilePaths;
    }
}