//package com.ylw.backend.controller;
//
//import com.ylw.backend.dto.*;
//import com.ylw.backend.services.CompanyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/role")
//public class RoleController {
//
//    private final CompanyService companyService;
//
//    @Autowired
//    public RoleController(CompanyService companyService) {
//        this.companyService = companyService;
//    }
//
//    /**
//     * 这个接口用于创建新的用户。接口需要接受的数据包括：
//     * - `username`：用户名，字符串类型。
//     * - `password`：用户密码，字符串类型。
//     * - `role`：用户角色，字符串类型
//     */
//    @PostMapping("/createUser")
//    public CreateUserResultClass createUser(@RequestBody CreateUserSentClass newUserInfo) {
//        // 将该新的用户存入数据库，并且注意公司的号
//        return companyService.createUserByClass(newUserInfo);
//    }
//
//    /**
//     * 该接口是用来删除对应的ID的用户的
//     */
//    @PostMapping("/deleteUser")
//    public DeleteUserResultClass deleteUser(@RequestBody DeleteUserSentClass deleteUserSent) {
//        // 需要将该ID对应的用户删除
//        return companyService.deleteUser(deleteUserSent);
//    }
//
//    /**
//     * 返回该管理ID所对应的所有的用户账户的信息
//     */
//    @PostMapping("/allUsers")
//    public AllUserResultClass forAllUsers(@RequestBody WebSentUserId webSentUserId) {
//        int userId = webSentUserId.getId();
//        // 返回该公司ID所管理的所有简历的信息
//        return companyService.forAllUsers(userId);
//    }
//}