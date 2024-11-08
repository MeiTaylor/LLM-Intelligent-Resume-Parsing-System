package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class EmailMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_email_id", insertable = false, updatable = false)
    private int userEmailId;

    @Column(name = "resume_id", insertable = false, updatable = false)
    private int resumeId;

    private String sender;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String attachment;

    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    @ManyToOne
    @JoinColumn(name = "user_email_id")
    private UserEmail userEmail;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

}
