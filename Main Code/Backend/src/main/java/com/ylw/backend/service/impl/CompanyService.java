//package com.ylw.backend.services;
//
//import com.ylw.backend.model.*;
//import com.ylw.backend.dto.*;
//import com.ylw.backend.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import javax.transaction.Transactional;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class CompanyService {
//
//    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
//    private final CompanyRepository companyRepository;
//    private final JobPositionRepository jobPositionRepository;
//    private final ResumeRepository resumeRepository;
//
//    @Autowired
//    public CompanyService(PasswordEncoder passwordEncoder, UserRepository userRepository,
//                          CompanyRepository companyRepository, JobPositionRepository jobPositionRepository,
//                          ResumeRepository resumeRepository) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//        this.companyRepository = companyRepository;
//        this.jobPositionRepository = jobPositionRepository;
//        this.resumeRepository = resumeRepository;
//    }
//
//    public Optional<Company> getCompanyById(int id) {
//        return companyRepository.findById(id);
//    }
//
//    public List<JobPosition> getJobPositionsByCompanyId(int companyId) {
//        return jobPositionRepository.findByCompanyId(companyId);
//    }
//
//    @Transactional
//    public LoginModelClass isLogin(String account, String password) {
//        User user = userRepository.findByAccount(account);
//        LoginModelClass model = new LoginModelClass();
//
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            model.setCode(20000);
//            model.setUserId(user.getId());
//            model.setData(user.getRole());
//            model.setCompanyId(user.getCompanyId());
//        } else {
//            model.setCode(60204);
//        }
//        return model;
//    }
//
//    public List<ResumeDTO> searchByCompanyId(int companyId) {
//        return resumeRepository.findByJobPositionCompanyId(companyId).stream()
//                .map(r -> new ResumeDTO(r.getId(), r.getApplicant().getName(),
//                        r.getJobPosition().getTitle(),
//                        r.getApplicant().getApplicantProfile().getMatchingScore()))
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public RegisterModelClass createNewAccount(RegisterSentModel register) {
//        RegisterModelClass model = new RegisterModelClass();
//        Optional<Company> existingCompany = companyRepository.findByName(register.getName());
//        Optional<User> existingUser = userRepository.findByAccount(register.getAccount());
//
//        if (existingCompany.isPresent()) {
//            model.setIsSuccess(false);
//            model.setMsg("Company already exists.");
//        } else if (existingUser.isPresent()) {
//            model.setIsSuccess(false);
//            model.setMsg("Account already exists.");
//        } else {
//            Company company = new Company(register.getName());
//            companyRepository.save(company);
//
//            User user = new User(company.getId(), register.getAccount(),
//                    register.getEmail(), passwordEncoder.encode(register.getPassword()), "admin");
//            user.setCompany(company);
//            userRepository.save(user);
//
//            model.setIsSuccess(true);
//            model.setMsg("Registration successful.");
//        }
//        return model;
//    }
//
//    public InfoForHomeModelClass getInfoForHome(int userId) {
//        Optional<Company> company = companyRepository.findByUserId(userId);
//
//        if (company.isEmpty()) {
//            return new InfoForHomeModelClass();
//        }
//
//        InfoForHomeModelClass infoForHome = new InfoForHomeModelClass();
//        infoForHome.setTotalResumes(company.get().getResumes().size());
//        infoForHome.setTotalJobs(company.get().getJobPositions().size());
//        infoForHome.setWeeklyStates(getWeeklyStates(userId));
//        infoForHome.setResumeHistory(getResumeHistory(company.get()));
//        infoForHome.setJobResumeCounts(getJobResumeCounts(company.get()));
//
//        return infoForHome;
//    }
//
//    private HomeWeeklyState getWeeklyStates(int userId) {
//        Optional<User> user = userRepository.findById(userId);
//        int companyId = user.map(User::getCompanyId).orElse(0);
//        List<String> dateList = jobPositionRepository.getPastSevenDays();
//
//        List<HomeJobCount> jobCounts = dateList.stream()
//                .map(date -> new HomeJobCount(date, jobPositionRepository.countByCreatedDateAndCompanyId(date, companyId)))
//                .collect(Collectors.toList());
//
//        List<HomeResumeCount> resumeCounts = dateList.stream()
//                .map(date -> new HomeResumeCount(date, resumeRepository.countByCreatedDateAndCompanyId(date, companyId)))
//                .collect(Collectors.toList());
//
//        return new HomeWeeklyState(jobCounts, resumeCounts);
//    }
//
//    private List<BriefHomeResumeInfo> getResumeHistory(Company company) {
//        return resumeRepository.findByCompanyId(company.getId()).stream()
//                .map(r -> new BriefHomeResumeInfo(r.getId(), r.getApplicant().getName(),
//                        r.getJobPosition().getTitle(), r.getCreatedDate().toString()))
//                .collect(Collectors.toList());
//    }
//
//    private List<JobResumeCount> getJobResumeCounts(Company company) {
//        return company.getJobPositions().stream()
//                .map(jp -> new JobResumeCount(jp.getTitle(),
//                        resumeRepository.countByJobPositionId(jp.getId())))
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public CreateUserResultClass createUserByClass(CreateUserSentClass newUserInfo) {
//        CreateUserResultClass result = new CreateUserResultClass();
//        Optional<Company> company = companyRepository.findById(newUserInfo.getCompanyId());
//
//        if (company.isEmpty()) {
//            result.setCode(400);
//            result.setMessage("Company does not exist.");
//            return result;
//        }
//
//        Optional<User> existingUser = userRepository.findByAccount(newUserInfo.getAccount());
//        if (existingUser.isPresent()) {
//            result.setCode(400);
//            result.setMessage("Account already exists.");
//            return result;
//        }
//
//        User user = new User(newUserInfo.getCompanyId(), newUserInfo.getAccount(),
//                newUserInfo.getEmail(), passwordEncoder.encode(newUserInfo.getPassword()), newUserInfo.getRole());
//        userRepository.save(user);
//
//        result.setCode(200);
//        result.setMessage("User created successfully.");
//        return result;
//    }
//
//    @Transactional
//    public DeleteUserResultClass deleteUser(DeleteUserSentClass deleteUserSent) {
//        DeleteUserResultClass result = new DeleteUserResultClass();
//        Optional<User> user = userRepository.findById(deleteUserSent.getId());
//
//        if (user.isEmpty()) {
//            result.setCode(400);
//            result.setMessage("User not found.");
//            return result;
//        }
//
//        userRepository.delete(user.get());
//        result.setCode(200);
//        result.setMessage("User deleted successfully.");
//        return result;
//    }
//
//    public AllUserResultClass forAllUsers(int userId) {
//        Optional<Company> company = companyRepository.findByUserId(userId);
//
//        if (company.isEmpty()) {
//            return new AllUserResultClass(List.of());
//        }
//
//        List<BriefUser> users = company.get().getUsers().stream()
//                .map(u -> new BriefUser(u.getId(), u.getAccount(), u.getRole()))
//                .collect(Collectors.toList());
//
//        return new AllUserResultClass(users);
//    }
//
//    public HomeToUploadResume uploadJobInfo(int userId) {
//        Optional<Company> company = companyRepository.findByUserId(userId);
//
//        if (company.isEmpty()) {
//            return new HomeToUploadResume(List.of());
//        }
//
//        List<JobInfoForUpload> jobsInfo = company.get().getJobPositions().stream()
//                .map(jp -> new JobInfoForUpload(jp.getId(), jp.getTitle()))
//                .collect(Collectors.toList());
//
//        return new HomeToUploadResume(jobsInfo);
//    }
//}
