package com.ylw.backend.service;

import com.ylw.backend.dto.LoginModelClass;
import com.ylw.backend.dto.RegisterModelClass;
import com.ylw.backend.dto.RegisterSentModel;

public interface UserServiceInterface {
    LoginModelClass isLogin(String account, String password);
    RegisterModelClass createNewAccount(RegisterSentModel register);

    RegisterModelClass createNewAccountByAdmin(RegisterSentModel register);

    boolean isUserAdmin(int userId);

    void promoteToAdmin(int userId);

    void deleteUser(int userId);
}
