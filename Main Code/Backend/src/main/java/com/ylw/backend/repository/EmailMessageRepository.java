package com.ylw.backend.repository;

import com.ylw.backend.model.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, Integer> {

    @Query("SELECT DATE(em.receivedDate), COUNT(em) " +
            "FROM EmailMessage em " +
            "JOIN UserEmail ue ON ue.id = em.userEmail.id " +
            "WHERE ue.user.id = :userId " +
            "AND em.receivedDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(em.receivedDate)")
    List<Object[]> getEmailCountsByUserAndDateRange(int userId, LocalDateTime startDate, LocalDateTime endDate);
}
