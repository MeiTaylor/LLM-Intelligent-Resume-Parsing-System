package com.ylw.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ConversationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

//
//    private String question;
//
//    private String message;


    @Column(columnDefinition = "LONGTEXT")
    private String question;

    @Column(columnDefinition = "LONGTEXT")
    private String message;


    @Column(name = "timestamp")
    private LocalDateTime timestamp;

}
