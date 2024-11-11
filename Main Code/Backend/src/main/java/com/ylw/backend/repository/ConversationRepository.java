package com.ylw.backend.repository;

import com.ylw.backend.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 对话数据访问接口
 * 继承自JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    /**
     * 根据简历ID查找所有对话
     * @param resumeId 简历ID
     * @return 对话列表
     */
    List<Conversation> findByResumeIdOrderByCreatedAtAsc(Integer resumeId);
}