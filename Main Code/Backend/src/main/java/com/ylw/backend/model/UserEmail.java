package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class UserEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private int userId;

    @Column(nullable = false, unique = true)
    private String emailAddress;

    @Column(nullable = false)
    private String emailPassword;  // 应当安全存储，例如加密处理

    @Column(nullable = false)
    private boolean isSyncEnabled = true;  // 默认值为 true

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userEmail", cascade = CascadeType.ALL)
    private List<EmailMessage> emailMessages;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public boolean isSyncEnabled() {
        return isSyncEnabled;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        isSyncEnabled = syncEnabled;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<EmailMessage> getEmailMessages() {
        return emailMessages;
    }

    public void setEmailMessages(List<EmailMessage> emailMessages) {
        this.emailMessages = emailMessages;
    }
}
