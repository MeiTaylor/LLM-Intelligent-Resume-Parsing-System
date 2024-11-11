package com.ylw.backend.repository;

import com.ylw.backend.model.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 对话消息数据访问接口
 * 继承自JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Integer> {
    /**
     * 根据对话ID查找所有消息并按时间戳排序
     * @param conversationId 对话ID
     * @return 消息列表
     */
    List<ConversationMessage> findByConversationIdOrderByTimestampAsc(Integer conversationId);
}