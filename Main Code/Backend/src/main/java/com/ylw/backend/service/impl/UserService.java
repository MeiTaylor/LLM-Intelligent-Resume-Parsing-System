package com.ylw.backend.service.impl;

import com.ylw.backend.dto.UserDTO;
import com.ylw.backend.model.Company;
import com.ylw.backend.model.User;
import com.ylw.backend.repository.CompanyRepository;
import com.ylw.backend.repository.UserRepository;
import com.ylw.backend.dto.LoginModelClass;
import com.ylw.backend.dto.RegisterModelClass;
import com.ylw.backend.dto.RegisterSentModel;
import com.ylw.backend.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public UserService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public LoginModelClass isLogin(String account, String password) {
        Optional<User> userOptional = userRepository.findByAccount(account);

        LoginModelClass model = new LoginModelClass();
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            User user = userOptional.get();
            model.setCode(20000);
            model.setUserId(user.getId());
            model.setData(user.getRole());
        } else {
            model.setCode(60204); // 用户不存在或密码错误
        }
        return model;
    }

    @Override
    public RegisterModelClass createNewAccount(RegisterSentModel register) {
        RegisterModelClass model = new RegisterModelClass();
        Optional<Company> existingCompany = companyRepository.findByName(register.getName());
        Optional<User> existingUser = userRepository.findByAccount(register.getAccount());

        if (existingCompany.isPresent()) {
            model.setIsSuccess(false);
            model.setMsg("Company already exists.");
        } else if (existingUser.isPresent()) {
            model.setIsSuccess(false);
            model.setMsg("Account already exists.");
        } else {
            Company company = new Company();
            company.setName(register.getName());
            company.setEmail(register.getEmail());
            companyRepository.save(company);

            User user = new User();
            user.setCompanyId(company.getId());
            user.setAccount(register.getAccount());
            user.setPassword(register.getPassword());
            user.setEmail(register.getEmail());
            user.setRole("admin");
            user.setCompany(company);
            userRepository.save(user);

            model.setIsSuccess(true);
            model.setMsg("Registration successful.");
        }
        return model;
    }

    @Override
    public RegisterModelClass createNewAccountByAdmin(RegisterSentModel register, int currentUserId) {
        RegisterModelClass model = new RegisterModelClass();
        Optional<User> existingUser = userRepository.findByAccount(register.getAccount());

        if (existingUser.isPresent()) {
            model.setIsSuccess(false);
            model.setMsg("Account already exists.");
        } else {
            Company company = userRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("Current user not found")).getCompany();

            User user = new User();
            user.setCompanyId(company.getId());
            user.setAccount(register.getAccount());
            user.setPassword(register.getPassword());
            user.setEmail(register.getEmail());
            user.setRole("normal");
            user.setCompany(company);
            userRepository.save(user);

            model.setIsSuccess(true);
            model.setMsg("Registration successful.");
        }
        return model;
    }

    @Override
    public boolean isUserAdmin(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        return "admin".equalsIgnoreCase(user.getRole());
    }

    @Override
    public void promoteToAdmin(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!"admin".equals(user.getRole())) {
                user.setRole("admin");
                userRepository.save(user);
            } else {
                throw new RuntimeException("User is already an admin.");
            }
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    @Override
    public void deleteUser(int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    //找到用户所属公司的所有用户
    @Override
    public List<UserDTO> findUsersByCompanyId(int companyId) {
        List<User> users = userRepository.findByCompanyId(companyId);
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setAccount(user.getAccount());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }
}
