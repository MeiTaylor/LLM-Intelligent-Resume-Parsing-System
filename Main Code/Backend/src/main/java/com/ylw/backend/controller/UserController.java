package com.ylw.backend.controller;

import com.ylw.backend.service.UserServiceInterface;
import com.ylw.backend.dto.LoginModelClass;
import com.ylw.backend.dto.LoginSentModel;
import com.ylw.backend.dto.RegisterModelClass;
import com.ylw.backend.dto.RegisterSentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceInterface userService;

    @Autowired
    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginModelClass login(@RequestBody LoginSentModel loginSentModel) {
        String userName = loginSentModel.getUserName();
        String password = loginSentModel.getPassword();

        // 查找数据库，判断用户是否存在，并返回对应的信息
        return userService.isLogin(userName, password);
    }

    @PostMapping("/register")
    public RegisterModelClass register(@RequestBody RegisterSentModel registerSentModel) {
        return userService.createNewAccount(registerSentModel);
    }
}
