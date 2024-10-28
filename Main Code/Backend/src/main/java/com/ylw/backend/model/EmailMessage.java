package com.ylw.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(int userEmailId) {
        this.userEmailId = userEmailId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public UserEmail getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(UserEmail userEmail) {
        this.userEmail = userEmail;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }
}
