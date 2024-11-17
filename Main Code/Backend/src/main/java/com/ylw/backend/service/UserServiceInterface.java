package com.ylw.backend.service;

import com.ylw.backend.dto.LoginModelClass;
import com.ylw.backend.dto.RegisterModelClass;
import com.ylw.backend.dto.RegisterSentModel;
import com.ylw.backend.dto.UserDTO;

import java.util.List;

public interface UserServiceInterface {
    LoginModelClass isLogin(String account, String password);
    RegisterModelClass createNewAccount(RegisterSentModel register);

    RegisterModelClass createNewAccountByAdmin(RegisterSentModel register, int currentUserId);

    boolean isUserAdmin(int userId);

    void promoteToAdmin(int userId);

    void deleteUser(int userId);

    //写一个新方法，恢复禁用的账号
    void recoverUser(int userId);

    //找到用户所属公司的所有用户
    List<UserDTO> findUsersByCompanyId(int companyId);

    //根据传入的userdto,修改用户信息
    void updateUser(UserDTO userDTO);
}
