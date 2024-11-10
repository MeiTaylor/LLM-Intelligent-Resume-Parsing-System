package com.ylw.backend.repository;

import com.ylw.backend.dto.EmailMoniterInfo;
import com.ylw.backend.dto.EmailReceiveResumeInfo;
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

    // 查询邮箱接收简历信息的四表联查
    @Query("SELECT new com.ylw.backend.dto.EmailReceiveResumeInfo(u.emailAddress,emsg.sender,emsg.subject   ,a.name,a.jobIntention,emsg.receivedDate) FROM UserEmail u, EmailMessage emsg, Applicant a WHERE u.userId = :userId AND u.id = emsg.userEmailId AND emsg.resumeId = a.id")
    List<EmailReceiveResumeInfo> findEmailReceiveInfoByUserId(@Param("userId") int userId);

    //查看所有简可以正确监听信息的简历
    @Query("SELECT new com.ylw.backend.dto.EmailMoniterInfo(u.id,u.userId,u.emailAddress,u.emailPassword) FROM UserEmail u WHERE u.isSyncEnabled = :syncEnabled")
    List<EmailMoniterInfo> findEmailBySyncEnabled(Boolean syncEnabled);
}
