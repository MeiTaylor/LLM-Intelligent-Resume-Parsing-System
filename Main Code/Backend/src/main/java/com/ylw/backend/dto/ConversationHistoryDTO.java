package com.ylw.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConversationHistoryDTO {
    private Integer conversationId;
    private String question;
    private String message;
    private LocalDateTime timestamp;
}