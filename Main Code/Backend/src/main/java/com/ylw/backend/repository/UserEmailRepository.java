package com.ylw.backend.repository;

import com.ylw.backend.model.UserEmail;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<UserEmail,Integer> {
    // 更具userId查看所有简历信息
    List<UserEmail> findByUserId(int userId);

}
