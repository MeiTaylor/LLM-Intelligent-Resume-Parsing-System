package com.ylw.backend.repository;

import com.ylw.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 根据账号查找用户
    Optional<User> findByAccount(String account);

    // 可以根据需求定义更多查询方法
}
