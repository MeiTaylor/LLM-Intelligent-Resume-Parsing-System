package com.ylw.backend.dto;

import lombok.Data;

/**
 * 对话问题的数据传输对象
 */
@Data
public class ConversationQuestionDTO {
    private Integer resumeId;
    private String question;
}