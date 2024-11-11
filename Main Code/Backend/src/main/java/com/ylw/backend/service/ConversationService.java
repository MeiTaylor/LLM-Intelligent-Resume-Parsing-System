package com.ylw.backend.service;

import com.ylw.backend.dto.ConversationHistoryDTO;
import java.util.List;

/**
 * 对话功能的服务接口
 * 定义了所有与对话相关的业务操作
 */
public interface ConversationService {

    /**
     * 测试对话功能的方法
     */
    String testConversation(Integer resumeId);

    /**
     * 获取指定简历的对话历史记录
     * @param resumeId 简历ID
     * @return 对话历史记录列表
     */
    List<ConversationHistoryDTO> getConversationHistory(Integer resumeId);



    /**
     * 处理用户问题并返回回答
     * @param resumeId 简历ID
     * @param question 用户问题
     * @return 问题的回答
     */
    String processQuestion(Integer resumeId, String question);




}