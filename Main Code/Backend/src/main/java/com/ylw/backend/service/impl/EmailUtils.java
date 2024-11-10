package com.ylw.backend.service.impl;

// 导入所需的 Jakarta Mail 相关类
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.FlagTerm;
// 导入日志相关类
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// 导入 Spring 相关注解
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// 导入自定义服务接口和模型类
import com.ylw.backend.service.ResumeServiceInterface;
import com.ylw.backend.model.Applicant;

// 导入 Java 标准库类
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.Map;
import java.security.MessageDigest;

// 标记为 Spring 组件
@Component
public class EmailUtils {
    // 创建日志记录器实例
    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);
    // 定义简历文件保存的根路径
    private static final String SAVE_PATH = "E:\\study\\Big_ruan\\code\\my_code\\down_resume";
    // 定义邮件检查的时间间隔（10秒）
    private static final long CHECK_INTERVAL = 10000;

    // 存储邮箱监控器的映射，键为邮箱ID
    private final Map<Integer, EmailMonitor> monitors = new ConcurrentHashMap<>();
    // 存储监控任务的Future对象，用于任务管理
    private final Map<Integer, Future<?>> monitorFutures = new ConcurrentHashMap<>();
    // 创建线程池执行器
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    // 注入简历解析服务
    private final ResumeServiceInterface resumeService;

    // 构造函数，通过依赖注入获取简历解析服务实例
    @Autowired
    public EmailUtils(ResumeServiceInterface resumeService) {
        this.resumeService = resumeService;
    }

    // 内部类：邮箱监控器
    private class EmailMonitor implements Runnable {
        private final String emailAddress;    // 邮箱地址
        private final String password;        // 邮箱密码
        private final String host;            // 邮箱服务器主机地址
        private volatile boolean running;     // 运行状态标志
        private final int emailId;           // 邮箱ID

        // 构造函数：初始化监控器
        public EmailMonitor(String emailAddress, String password, String host, int emailId) {
            this.emailAddress = emailAddress;
            this.password = password;
            this.host = host;
            this.emailId = emailId;
            this.running = true; // 初始状态设为运行中
        }

        // 停止监控器的方法
        public void stop() {
            running = false;
        }

        // 线程运行的主方法
        @Override
        public void run() {
            while (running) {
                try {
                    // 检查新邮件
                    checkNewEmails();
                    // 等待指定时间间隔
                    Thread.sleep(CHECK_INTERVAL);
                } catch (Exception e) {
                    // 记录错误日志
                    logger.error("检查邮件时发生错误: " + e.getMessage());
                    try {
                        // 发生错误后等待一段时间再继续
                        Thread.sleep(CHECK_INTERVAL);
                    } catch (InterruptedException ie) {
                        break; // 如果在等待时被中断，则退出循环
                    }
                }
            }
        }

        // 检查新邮件的方法
        private void checkNewEmails() {
            // 设置邮箱连接属性
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imap");
            props.setProperty("mail.imap.host", host);
            props.setProperty("mail.imap.port", "993");
            props.setProperty("mail.imap.ssl.enable", "true");

            try {
                // 创建邮件会话
                Session session = Session.getInstance(props);
                Store store = session.getStore("imap");
                // 连接到邮箱服务器
                store.connect(host, emailAddress, password);

                // 获取收件箱
                Folder folder = store.getFolder("INBOX");
                // 以读写模式打开文件夹
                folder.open(Folder.READ_WRITE);

                // 获取所有未读邮件
                Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                logger.info("发现 {} 封未读邮件", messages.length);

                // 处理每一封未读邮件
                for (Message message : messages) {
                    processEmail(message);
                    // 将邮件标记为已读
                    message.setFlag(Flags.Flag.SEEN, true);
                }

                // 关闭资源
                folder.close(false);
                store.close();
            } catch (Exception e) {
                logger.error("处理邮件时发生错误: ", e);
            }
        }

        // 处理单个邮件的方法
        private void processEmail(Message message) throws Exception {
            // 获取邮件主题和发件人
            String subject = message.getSubject();
            String from = ((InternetAddress) message.getFrom()[0]).getAddress();
            logger.info("处理新邮件 - 发件人: {} 主题: {}", from, subject);

            // 处理邮件内容
            handleContent(message);
        }

        // 处理邮件内容的方法
        private void handleContent(Part part) throws Exception {
            // 如果是多部分邮件
            if (part.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();
                int count = multipart.getCount();
                // 处理每个部分
                for (int i = 0; i < count; i++) {
                    handleContent(multipart.getBodyPart(i));
                }
            }
            // 如果是附件
            else if (part.getDisposition() != null &&
                    (part.getDisposition().equals(Part.ATTACHMENT) ||
                            part.getDisposition().equals(Part.INLINE))) {
                saveAttachment(part);
            }
        }

        // 保存附件的方法
        private void saveAttachment(Part part) throws Exception {
            // 解码附件文件名
            String fileName = MimeUtility.decodeText(part.getFileName());
            if (fileName != null) {
                try {
                    // 获取文件扩展名
                    String fileExtension = getFileExtension(fileName);
                    // 检查是否是简历文件
                    if (isResumeFile(fileExtension)) {
                        // 创建基于日期的目录路径
                        LocalDate today = LocalDate.now();
                        String fileUrlWithoutFileName = String.format("%d/%d/%d",
                                today.getYear(), today.getMonthValue(), today.getDayOfMonth());

                        // 创建目录
                        Path directoryPath = Paths.get(SAVE_PATH, fileUrlWithoutFileName);
                        Files.createDirectories(directoryPath);

                        // 计算文件的MD5哈希值作为文件名
                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                        byte[] content = readPartContent(part);
                        byte[] hashBytes = md5.digest(content);
                        String hashedFileName = bytesToHex(hashBytes);

                        // 构建新的文件名和路径
                        String newFileName = hashedFileName + "." + fileExtension;
                        Path filePath = directoryPath.resolve(newFileName);

                        // 保存文件
                        Files.write(filePath, content);
                        logger.info("附件已保存: {}", filePath);

                        try {
                            // 解析简历并保存到数据库
                            Applicant applicant = resumeService.parseResume(filePath.toString());
                            int jobId = extractJobId();
                            int userId = extractUserId();
                            resumeService.createAndSaveResume(filePath.toString(), applicant, jobId, userId);
                            logger.info("简历解析和存储成功: {}", filePath);
                        } catch (Exception e) {
                            logger.error("简历解析或存储失败: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error("保存和处理附件失败: " + e.getMessage());
                    throw e;
                }
            }
        }

        // 读取附件内容的辅助方法
        private byte[] readPartContent(Part part) throws IOException, MessagingException {
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

        // 将字节数组转换为十六进制字符串的辅助方法
        private String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }

        // 获取文件扩展名的辅助方法
        private String getFileExtension(String fileName) {
            int lastIndexOfDot = fileName.lastIndexOf('.');
            return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1).toLowerCase();
        }

        // 检查是否是简历文件的辅助方法
        private boolean isResumeFile(String extension) {
            return extension.equals("doc") ||
                    extension.equals("docx") ||
                    extension.equals("pdf") ||
                    extension.equals("txt");
        }

        // TODO: 从邮件中提取jobId，目前返回默认值1
        private int extractJobId() {
            return 1;
        }

        // TODO: 从邮件中提取userId，目前返回默认值1
        private int extractUserId() {
            return 1;
        }
    }
}