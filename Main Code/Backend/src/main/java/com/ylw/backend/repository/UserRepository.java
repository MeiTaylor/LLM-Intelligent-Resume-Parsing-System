package com.ylw.backend.repository;

import com.ylw.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 根据账号查找用户
    Optional<User> findByAccount(String account);

    // 可以根据需求定义更多查询方法
    // 根据用户ID查找公司ID
    @Query("SELECT u.company.id FROM User u WHERE u.id = :userId")
    Optional<Integer> findCompanyIdById(int userId);
}
