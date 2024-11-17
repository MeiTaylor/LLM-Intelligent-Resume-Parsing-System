package com.ylw.backend.controller;

import com.ylw.backend.dto.*;
import com.ylw.backend.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestParam int currentUserId, @RequestBody RegisterSentModel registerSentModel) {
        try {
            // 检查当前用户是否有权限
            if (!userService.isUserAdmin(currentUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied: You do not have the rights to add a user.");
            }

            RegisterModelClass registerModelClass = userService.createNewAccountByAdmin(registerSentModel, currentUserId);
            if (!registerModelClass.IsSuccess) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add user: " + registerModelClass.msg);
            }
            return ResponseEntity.ok("User added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PatchMapping("/promoteToAdmin")
    public ResponseEntity<String> promoteToAdmin(@RequestParam int userId) {
        try {
            userService.promoteToAdmin(userId);
            return ResponseEntity.ok("User has been promoted to admin successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam int currentUserId, @RequestParam int userIdToDelete) {
        try {
            // 检查当前用户是否有权限
            if (!userService.isUserAdmin(currentUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied: You do not have the rights to delete a user.");
            }

            userService.deleteUser(userIdToDelete);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    //返回用户所属公司的所有用户列表
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam int userId) {
        List<UserDTO> users = userService.findUsersByCompanyId(userId);
        return ResponseEntity.ok(users);
    }

    //根据传入的userDto,修改用户信息
    @PatchMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            return ResponseEntity.ok("User information updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
