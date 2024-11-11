package com.ylw.backend.controller;

import com.ylw.backend.dto.WebSendResumeId;
import com.ylw.backend.dto.WebSentUserId;
import com.ylw.backend.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ylw.backend.dto.ConversationHistoryDTO;
import com.ylw.backend.dto.ConversationQuestionDTO;

import java.util.List;

import com.ylw.backend.model.Resume;
import com.ylw.backend.repository.ResumeRepository;




/**
 * 对话功能的控制器
 * 处理与对话相关的 HTTP 请求
 */
@RestController
@RequestMapping("/api/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * 构造函数注入 ConversationService
     * @param conversationService 对话服务接口
     */
    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }




    /**
     * 获取指定简历的对话历史记录
//     * @param resumeId 简历ID
     * @return 对话历史记录列表
     */
    @GetMapping("/history/resumeIdHistory")
    public ResponseEntity<?> getConversationHistory(@RequestBody WebSendResumeId webSendResumeId) {
        try {
            List<ConversationHistoryDTO> history = conversationService.getConversationHistory(webSendResumeId.getResumeId());
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching conversation history: " + e.getMessage());
        }
    }







    /**
     * 处理用户问题的端点
     * @param questionDTO 包含resumeId和question的DTO
     * @return 问题的回答
     */
    @PostMapping("/ask")
    public ResponseEntity<?> askQuestion(@RequestBody ConversationQuestionDTO questionDTO) {
        try {
            String answer = conversationService.processQuestion(
                    questionDTO.getResumeId(),
                    questionDTO.getQuestion()
            );
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing question: " + e.getMessage());
        }
    }


}