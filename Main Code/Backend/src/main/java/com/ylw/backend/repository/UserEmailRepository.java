package com.ylw.backend.repository;

import com.ylw.backend.model.UserEmail;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail,Integer> {
    // 更具userId查看所有简历信息
    List<UserEmail> findByUserId(Integer userId);

    //根据id查出对应的状态
    Optional<UserEmail> findById(int id);

}
