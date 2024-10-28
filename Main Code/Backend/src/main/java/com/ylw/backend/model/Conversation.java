package com.ylw.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "resume_id", insertable = false, updatable = false)
    private int resumeId;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<ConversationMessage> messages;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ConversationMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ConversationMessage> messages) {
        this.messages = messages;
    }
}
