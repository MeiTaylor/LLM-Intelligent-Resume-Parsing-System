package com.ylw.backend.service.impl;

import com.ylw.backend.dto.ConversationHistoryDTO;
import com.ylw.backend.model.Conversation;
import com.ylw.backend.model.ConversationMessage;
import com.ylw.backend.model.Resume;
import com.ylw.backend.repository.ConversationRepository;
import com.ylw.backend.repository.ConversationMessageRepository;
import com.ylw.backend.repository.ResumeRepository;
import com.ylw.backend.service.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;







/**
 * ConversationService 的实现类
 * 实现了对话相关的具体业务逻辑
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationServiceImpl.class);

    private final ConversationRepository conversationRepository;
    private final ConversationMessageRepository messageRepository;
    private final ResumeRepository resumeRepository;

    @Autowired
    public ConversationServiceImpl(
            ConversationRepository conversationRepository,
            ConversationMessageRepository messageRepository,
            ResumeRepository resumeRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.resumeRepository = resumeRepository;
    }


    /**
     * 根据resumeId获取简历内容
     * @param resumeId 简历ID
     * @return 简历文本内容
     * @throws RuntimeException 当找不到简历、文件路径为空或文件读取失败时抛出
     */
    private String getResumeContent(int resumeId) {

        logger.debug("Fetching content for resume ID: {}", resumeId);

        // 1. 获取原始文件路径
        String originalFilePath = getResumeFilePath(resumeId);

        try {
            // 2. 构建转换后的txt文件路径
            Path originalPath = Paths.get(originalFilePath);
            String fileName = originalPath.getFileName().toString();
            String baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));

            // 3. 获取年月目录路径（如：.../2024/11）
            Path yearMonthPath = originalPath.getParent().getParent();

            // 4. 构建转换后文件的完整路径
            Path convertedFilePath = yearMonthPath.resolve("Conversions")
                    .resolve("Text_Conversions")
                    .resolve(baseFileName + ".txt");

            logger.debug("Looking for converted file at: {}", convertedFilePath);

            // 5. 检查文件是否存在
            if (!Files.exists(convertedFilePath)) {
                throw new RuntimeException("找不到转换后的文本文件: " + convertedFilePath);
            }

            // 6. 读取文件内容
            String content = Files.readString(convertedFilePath);
            logger.debug("Successfully read content from converted file");

            return content;

        } catch (IOException e) {
            logger.error("Error reading converted file for resume ID: {}", resumeId, e);
            throw new RuntimeException("读取转换后的文本文件失败: " + e.getMessage());
        }
    }

    /**
     * 根据resumeId获取简历文件路径
     * @param resumeId 简历ID
     * @return 文件路径
     * @throws RuntimeException 当找不到简历或文件路径为空时抛出
     */
    private String getResumeFilePath(int resumeId) {
        logger.debug("Fetching file path for resume ID: {}", resumeId);

        //Resume resume = resumeRepository.findById(resumeId);
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(() -> new RuntimeException("找不到ID为 " + resumeId + " 的简历"));
        if (resume == null) {
            throw new RuntimeException("找不到ID为 " + resumeId + " 的简历");
        }

        String filePath = resume.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new RuntimeException("简历 " + resumeId + " 的文件路径为空");
        }

        logger.debug("Found file path: {} for resume ID: {}", filePath, resumeId);
        return filePath;
    }

    @Override
    @Transactional
    public String testConversation(Integer resumeId) {
        try {
            logger.info("Starting test conversation for resume ID: {}", resumeId);

            // 创建新的对话实例
            Conversation conversation = new Conversation();
            conversation.setCreatedAt(LocalDateTime.now());

            // 设置关联的简历
            Resume resume = new Resume();
            resume.setId(resumeId);
            conversation.setResume(resume);

            // 保存对话记录
            conversation = conversationRepository.save(conversation);
            logger.debug("Created new conversation with ID: {}", conversation.getId());

            // 创建测试消息
            ConversationMessage message = new ConversationMessage();
            message.setConversation(conversation);
            message.setQuestion("这是一条测试问题");
            message.setMessage("这是一条测试回答");
            message.setTimestamp(LocalDateTime.now());

            // 保存消息
            messageRepository.save(message);
            logger.debug("Created test message for conversation ID: {}", conversation.getId());

            return String.format("对话创建成功! ID: %d", conversation.getId());

        } catch (Exception e) {
            logger.error("Error creating test conversation for resume ID: " + resumeId, e);
            throw new RuntimeException("创建对话失败: " + e.getMessage());
        }
    }







    @Override
    @Transactional(readOnly = true)
    public List<ConversationHistoryDTO> getConversationHistory(Integer resumeId) {
        try {
            logger.info("Fetching conversation history for resume ID: {}", resumeId);

            // 获取所有对话并按创建时间排序
            List<Conversation> conversations = conversationRepository.findByResumeIdOrderByCreatedAtAsc(resumeId);

            List<ConversationHistoryDTO> historyList = new ArrayList<>();

            // 遍历每个对话，获取其消息
            for (Conversation conversation : conversations) {
                List<ConversationMessage> messages = messageRepository
                        .findByConversationIdOrderByTimestampAsc(conversation.getId());

                // 转换为DTO
                List<ConversationHistoryDTO> conversationDTOs = messages.stream()
                        .map(msg -> {
                            ConversationHistoryDTO dto = new ConversationHistoryDTO();
                            dto.setConversationId(conversation.getId());
                            dto.setQuestion(msg.getQuestion());
                            dto.setMessage(msg.getMessage());
                            dto.setTimestamp(msg.getTimestamp());
                            return dto;
                        })
                        .collect(Collectors.toList());

                historyList.addAll(conversationDTOs);
            }

            logger.debug("Found {} messages for resume ID: {}", historyList.size(), resumeId);
            return historyList;

        } catch (Exception e) {
            logger.error("Error fetching conversation history for resume ID: " + resumeId, e);
            throw new RuntimeException("获取对话历史失败: " + e.getMessage());
        }
    }



    @Override
    @Transactional
    public String processQuestion(Integer resumeId, String question) {
        try {
            logger.info("Processing question for resume ID: {}", resumeId);
            logger.debug("Question: {}", question);

            // 创建新的对话记录
            Conversation conversation = new Conversation();
            conversation.setCreatedAt(LocalDateTime.now());
            Resume resume = new Resume();
            resume.setId(resumeId);
            conversation.setResume(resume);
            conversation = conversationRepository.save(conversation);


            // 获取简历内容
            String resumeContent = getResumeContent(resumeId);

            // 获取历史对话记录
            List<ConversationHistoryDTO> conversationHistory = getConversationHistory(resumeId);

            // 生成答案
            String answer = generateAnswer(resumeId, resumeContent, conversationHistory, question);


            // 保存问答记录
            ConversationMessage message = new ConversationMessage();
            message.setConversation(conversation);
            message.setQuestion(question);
            message.setMessage(answer);
            message.setTimestamp(LocalDateTime.now());
            messageRepository.save(message);

            return answer;
        } catch (Exception e) {
            logger.error("Error processing question for resume ID: " + resumeId, e);
            throw new RuntimeException("处理问题失败: " + e.getMessage());
        }
    }



    /**
     * 生成问题的回答
     * @param resumeId 简历ID
     * @param resumeContent 简历文本内容
     * @param conversationHistory 历史对话记录
     * @param question 当前问题
     * @return 生成的回答
     */
    private String generateAnswer(Integer resumeId, String resumeContent,
                                  List<ConversationHistoryDTO> conversationHistory, String question) {
        try {
            logger.info("Generating answer for resume ID: {}", resumeId);

            // 构建历史对话文本
            StringBuilder historyText = new StringBuilder();
            if (!conversationHistory.isEmpty()) {
                historyText.append("以下是之前的对话历史：\n");
                for (ConversationHistoryDTO history : conversationHistory) {
                    historyText.append("问：").append(history.getQuestion()).append("\n");
                    historyText.append("答：").append(history.getMessage()).append("\n\n");
                }
            }

            // 构建完整的prompt
            String prompt = String.format(
                    "以下是简历的完整内容：\n\n%s\n\n" +
                            "%s\n" +  // 如果有历史对话，这里会插入历史对话记录
                            "请根据以上简历内容%s回答以下问题：\n%s",
                    resumeContent,
                    !historyText.isEmpty() ? historyText.toString() : "",
                    !historyText.isEmpty() ? "和上述对话历史，" : "，",
                    question
            );



            logger.debug("Generated prompt: {}", prompt);
            // 输出prompt
            System.out.println("Generated prompt: " + prompt);

            // 调用GPT获取回答
            String answer = communicateWithGPT(prompt);
            logger.info("Generated answer for resume ID: {}", resumeId);

            System.out.println("Generated answer for resume ID: " + resumeId);


            return answer;

        } catch (Exception e) {
            logger.error("Error generating answer for resume ID: " + resumeId, e);
            throw new RuntimeException("生成回答失败: " + e.getMessage());
        }
    }






    // API配置，从application.properties中读取
    @Value("${gpt.api.url:https://cn2us02.opapi.win/v1/chat/completions}")
    private String apiUrl;

    @Value("${gpt.api.key}")
    private String apiKey;



    /**
     * 与GPT API通信的方法
     * @param question 要发送给GPT的问题/提示
     * @return GPT的响应
     */
    private String communicateWithGPT(String question) {
        HttpURLConnection conn = null;
        try {
            logger.debug("Sending request to GPT API. Question length: {}", question.length());

            // 创建URL对象
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // 配置连接
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 使用Jackson构建JSON
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ArrayNode messagesNode = mapper.createArrayNode();
            ObjectNode messageNode = mapper.createObjectNode();

            messageNode.put("role", "user");
            messageNode.put("content", question);
            messagesNode.add(messageNode);

            rootNode.put("model", "gpt-4o-mini");
            rootNode.set("messages", messagesNode);

            String requestBody = mapper.writeValueAsString(rootNode);
            logger.debug("Request body: {}", requestBody);

            // 发送请求
            try (OutputStream os = conn.getOutputStream();
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
                writer.write(requestBody);
                writer.flush();
            }

            // 处理响应
            int responseCode = conn.getResponseCode();

            // 读取错误响应（如果有）
            if (responseCode != HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    logger.error("API Error Response: {}", errorResponse.toString());
                    return "Error: " + errorResponse.toString();
                }
            }

            // 读取成功响应
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                String responseStr = response.toString();
                logger.debug("Raw API Response: {}", responseStr);

                // 使用Jackson解析响应
                ObjectNode responseNode = (ObjectNode) mapper.readTree(responseStr);
                ArrayNode choicesNode = (ArrayNode) responseNode.get("choices");
                if (choicesNode != null && !choicesNode.isEmpty()) {
                    ObjectNode messageObject = (ObjectNode) choicesNode.get(0).get("message");
                    if (messageObject != null) {
                        String content = messageObject.get("content").asText();
                        logger.debug("Processed response length: {}", content.length());
                        return content;
                    }
                }
            }

            return "Error: Unable to parse response";

        } catch (Exception e) {
            logger.error("Error communicating with GPT API", e);
            return "Error: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }






}



