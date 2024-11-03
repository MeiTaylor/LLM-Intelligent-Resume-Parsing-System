//package com.ylw.backend.service;
//
//import com.ylw.backend.dto.*;
//import com.ylw.backend.model.Company;
//import com.ylw.backend.model.JobPosition;
//
//import java.util.List;
//
//public interface CompanyServiceInterface {
//
//    Company getCompanyById(int id);
//
//    List<JobPosition> getJobPositionsByCompanyId(int companyId);
//
//    LoginModelClass isLogin(String account, String password);
//
//    RegisterModelClass createNewAccount(RegisterSentModel register);
//
//    InfoForHomeModelClass getInfoForHome(int userId);
//
//    CreateUserResultClass createUserByClass(CreateUserSentClass newUserInfo);
//
//    DeleteUserResultClass deleteUser(DeleteUserSentClass deleteUserSent);
//
//    AllUserResultClass forAllUsers(int userId);
//
//    HomeToUploadResume uploadJobInfo(int userId);
//}