// 声明包名，指定类所在的包路径
package com.ylw.backend.service.impl;

// 导入所需的所有依赖
import jakarta.mail.*;                       // 核心邮件API

import org.slf4j.Logger;                     // 日志接口
import org.slf4j.LoggerFactory;              // 日志工厂
import org.springframework.beans.factory.annotation.Autowired;  // Spring自动注入
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;                // Spring组件注解
import com.ylw.backend.service.ResumeServiceInterface;         // 简历服务接口

import java.io.*;                           // IO操作
import java.util.Properties;                // 属性配置
import java.util.concurrent.*;              // 并发工具
import java.util.Map;                       // Map接口
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.FlagTerm;

import java.nio.file.*;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.*;
// 标记为Spring组件，使其能被Spring容器管理
@Component
public class EmailUtils {
    // 创建日志记录器实例，用于记录程序运行状态和错误信息
    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);

    // 定义附件保存的根路径
//    private static final String SAVE_PATH = "E:\\study\\Big_ruan\\code\\my_code\\down_resume";
    @Value("${resume.file.root}")
    private String SAVE_PATH;
//    private static final String SAVE_PATH = "D:\\study\\resume";


    // 定义邮件检查的时间间隔（10秒）
    private static final long CHECK_INTERVAL = 10000;


    // 存储监控任务的Future对象的Map，用于任务管理
    private final Map<Integer, Future<?>> monitorFutures = new ConcurrentHashMap<>();

    // 创建缓存线程池，用于执行邮箱监控任务
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // 注入简历服务接口
    private final ResumeServiceInterface resumeService;

    // 构造函数，通过依赖注入获取简历服务实例
    @Autowired
    public EmailUtils(ResumeServiceInterface resumeService) {
        this.resumeService = resumeService;
    }

    /**
     * 获取邮箱中包含附件的新邮件信息
     * @param emailAddress 邮箱地址
     * @param password 邮箱授权码
     * @param savePath 附件保存路径
     * @return 包含附件的邮件信息列表
     */
    public  List<Map<String, Object>> getNewEmails(String emailAddress, String password, String savePath) {
        // 定义返回结果列表，每个Map代表一封邮件的信息
        List<Map<String, Object>> emailsList = new ArrayList<>();

        // 根据邮箱类型获取对应的服务器地址
        String host = emailAddress.endsWith("@qq.com") ? "imap.qq.com" :
                emailAddress.endsWith("@163.com") ? "imap.163.com" : null;

        if (host == null) {
            throw new IllegalArgumentException("Unsupported email type");
        }

        // 设置邮箱连接属性
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");

        try {
            // 创建会话并连接邮箱
            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(host, emailAddress, password);

            // 打开收件箱
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            // 获取未读邮件
            Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            System.out.println("Found " + messages.length + " unread emails");

            // 处理每封未读邮件
            for (Message message : messages) {
                // 用于临时存储附件列表
                List<String> attachments = new ArrayList<>();

                // 处理邮件内容和附件
                processMessageContent(message, savePath, attachments);

                // 只有当邮件包含附件时才添加到结果列表
                if (!attachments.isEmpty()) {
                    Map<String, Object> emailInfo = new HashMap<>();
                    emailInfo.put("id", message.getMessageNumber());
                    emailInfo.put("subject", message.getSubject());
                    emailInfo.put("received_date", message.getReceivedDate());
                    emailInfo.put("sender", ((InternetAddress)message.getFrom()[0]).getAddress());
                    emailInfo.put("user_email_id", emailAddress);

                    // 处理邮件正文
                    String body = extractBody(message);
                    emailInfo.put("body", body);
                    emailInfo.put("attachment", attachments);

                    emailsList.add(emailInfo);
                }

                // 标记邮件为已读
                message.setFlag(Flags.Flag.SEEN, true);
            }

            // 关闭资源
            folder.close(false);
            store.close();

        } catch (Exception e) {
            System.err.println("Error processing emails: " + e.getMessage());
            e.printStackTrace();
        }

        return emailsList;
    }

    /**
     * 处理邮件内容，提取附件
     * @param part 邮件部分
     * @param savePath 保存路径
     * @param attachments 附件列表
     */
    private  void processMessageContent(Part part, String savePath, List<String> attachments) throws Exception {
        if (part.isMimeType("multipart/*")) {
            // 处理多部分邮件
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                processMessageContent(multipart.getBodyPart(i), savePath, attachments);
            }
        } else if (part.getDisposition() != null &&
                (part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT) ||
                        part.getDisposition().equalsIgnoreCase(Part.INLINE))) {
            // 处理附件
            saveAttachment(part, savePath, attachments);
        }
    }

    /**
     * 提取邮件正文
     * @param message 邮件对象
     * @return 邮件正文内容
     */
    private  String extractBody(Message message) throws Exception {
        StringBuilder body = new StringBuilder();
        if (message.isMimeType("text/plain")) {
            body.append(message.getContent());
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                Part part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain") && !Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    body.append(part.getContent());
                }
            }
        }
        return body.toString();
    }

    /**
     * 保存附件
     * @param part 邮件部分
     * @param savePath 保存路径
     * @param attachments 附件列表
     */
    private  void saveAttachment(Part part, String savePath, List<String> attachments) throws Exception {
        String fileName = MimeUtility.decodeText(part.getFileName());
        if (fileName != null) {
            // 获取文件扩展名
            String fileExtension = getFileExtension(fileName);

            // 创建基于日期的目录结构
            LocalDate today = LocalDate.now();
            String fileUrlWithoutFileName = String.format("%d/%d/%d",
                    today.getYear(), today.getMonthValue(), today.getDayOfMonth());
            Path directoryPath = Paths.get(savePath, fileUrlWithoutFileName);
            Files.createDirectories(directoryPath);

            // 计算文件MD5哈希值
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] content = readPartContent(part);
            byte[] hashBytes = md5.digest(content);
            String hashedFileName = bytesToHex(hashBytes);

            // 创建新文件名并保存
            String newFileName = hashedFileName + "." + fileExtension;
            Path filePath = directoryPath.resolve(newFileName);
            Files.write(filePath, content);

            // 添加到附件列表
            attachments.add(filePath.toString());
            System.out.println("Attachment saved: " + filePath);
        }
    }

    /**
     * 读取Part内容
     */
    private  byte[] readPartContent(Part part) throws IOException, MessagingException {
        try (InputStream is = part.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     */
    private  String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 获取文件扩展名
     */
    private  String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1).toLowerCase();
    }


    // 验证邮箱账号密码是否正确
    public boolean validateEmailCredentials(String email, String password) {
        // 获取邮箱服务器地址
        String host = getEmailHost(email);
        if (host == null) {
            return false; // 不支持的邮箱类型
        }

        // 设置邮箱连接属性
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");   // 使用IMAP协议
        props.setProperty("mail.imap.host", host);          // 设置服务器地址
        props.setProperty("mail.imap.port", "993");         // 设置SSL端口
        props.setProperty("mail.imap.ssl.enable", "true");  // 启用SSL加密

        // 创建邮件会话
        Session session = Session.getInstance(props);
        try {
            // 尝试连接邮箱服务器
            Store store = session.getStore("imap");
            store.connect(host, email, password);
            store.close();  // 验证成功后关闭连接
            return true;    // 返回验证成功
        } catch (MessagingException e) {
            logger.error("邮箱验证失败: " + e.getMessage());
            return false;   // 返回验证失败
        }
    }

    // 根据邮箱地址获取对应的服务器地址
    public String getEmailHost(String email) {
        if (email.endsWith("@qq.com")) {
            return "imap.qq.com";         // QQ邮箱服务器
        } else if (email.endsWith("@163.com")) {
            return "imap.163.com";        // 163邮箱服务器
        }
        return null;  // 不支持的邮箱类型返回null
    }

}