package com.ylw.backend.service.impl;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.FlagTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ylw.backend.service.ResumeServiceInterface;
import com.ylw.backend.model.Applicant;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.Map;
import java.security.MessageDigest;

@Component
public class EmailUtils {
    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);
    private static final String SAVE_PATH = "E:\\study\\Big_ruan\\code\\my_code\\down_resume";
    private static final long CHECK_INTERVAL = 10000; // 10秒的检查间隔
    private final Map<Integer, EmailMonitor> monitors = new ConcurrentHashMap<>();
    private final Map<Integer, Future<?>> monitorFutures = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ResumeServiceInterface resumeService;

    @Autowired
    public EmailUtils(ResumeServiceInterface resumeService) {
        this.resumeService = resumeService;
    }

    // 邮箱验证相关方法
    public boolean validateEmailCredentials(String email, String password) {
        String host = getEmailHost(email);
        if (host == null) {
            return false;
        }

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");

        Session session = Session.getInstance(props);
        try {
            Store store = session.getStore("imap");
            store.connect(host, email, password);
            store.close();
            return true;
        } catch (MessagingException e) {
            logger.error("邮箱验证失败: " + e.getMessage());
            return false;
        }
    }

    public String getEmailHost(String email) {
        if (email.endsWith("@qq.com")) {
            return "imap.qq.com";
        } else if (email.endsWith("@163.com")) {
            return "imap.163.com";
        }
        return null;
    }

    // 监控管理相关方法
    public void startMonitor(int emailId, String emailAddress, String password) {
        stopMonitor(emailId); // 如果已存在，先停止旧的监控
        String host = getEmailHost(emailAddress);
        if (host == null) {
            logger.error("不支持的邮箱类型：" + emailAddress);
            return;
        }

        EmailMonitor monitor = new EmailMonitor(emailAddress, password, host, emailId);
        monitors.put(emailId, monitor);
        Future<?> future = executorService.submit(monitor);
        monitorFutures.put(emailId, future);
        logger.info("开始监控邮箱: " + emailAddress);
    }

    public void stopMonitor(int emailId) {
        EmailMonitor monitor = monitors.remove(emailId);
        if (monitor != null) {
            monitor.stop();
        }

        Future<?> future = monitorFutures.remove(emailId);
        if (future != null) {
            future.cancel(true);
        }
        logger.info("停止监控邮箱ID: " + emailId);
    }

    public void stopAllMonitors() {
        for (Integer emailId : monitors.keySet()) {
            stopMonitor(emailId);
        }
        executorService.shutdown();
        logger.info("停止所有邮箱监控");
    }

    public boolean isMonitoring(int emailId) {
        return monitors.containsKey(emailId);
    }

    private class EmailMonitor implements Runnable {
        private final String emailAddress;
        private final String password;
        private final String host;
        private volatile boolean running = true;
        private final int emailId;

        public EmailMonitor(String emailAddress, String password, String host, int emailId) {
            this.emailAddress = emailAddress;
            this.password = password;
            this.host = host;
            this.emailId = emailId;
        }

        public void stop() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    checkNewEmails();
                    Thread.sleep(CHECK_INTERVAL);
                } catch (Exception e) {
                    logger.error("检查邮件时发生错误: " + e.getMessage());
                    try {
                        Thread.sleep(CHECK_INTERVAL);
                    } catch (InterruptedException ie) {
                        break;
                    }
                }
            }
        }

        private void checkNewEmails() {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imap");
            props.setProperty("mail.imap.host", host);
            props.setProperty("mail.imap.port", "993");
            props.setProperty("mail.imap.ssl.enable", "true");

            try {
                Session session = Session.getInstance(props);
                Store store = session.getStore("imap");
                store.connect(host, emailAddress, password);

                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);

                Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                logger.info("发现 {} 封未读邮件", messages.length);

                for (Message message : messages) {
                    processEmail(message);
                    message.setFlag(Flags.Flag.SEEN, true);
                }

                folder.close(false);
                store.close();
            } catch (Exception e) {
                logger.error("处理邮件时发生错误: ", e);
            }
        }

        private void processEmail(Message message) throws Exception {
            String subject = message.getSubject();
            String from = ((InternetAddress) message.getFrom()[0]).getAddress();
            logger.info("处理新邮件 - 发件人: {} 主题: {}", from, subject);

            handleContent(message);
        }

        private void handleContent(Part part) throws Exception {
            if (part.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();
                int count = multipart.getCount();
                for (int i = 0; i < count; i++) {
                    handleContent(multipart.getBodyPart(i));
                }
            } else if (part.getDisposition() != null &&
                    (part.getDisposition().equals(Part.ATTACHMENT) ||
                            part.getDisposition().equals(Part.INLINE))) {
                saveAttachment(part);
            }
        }

        private void saveAttachment(Part part) throws Exception {
            String fileName = MimeUtility.decodeText(part.getFileName());
            if (fileName != null) {
                try {
                    String fileExtension = getFileExtension(fileName);
                    if (isResumeFile(fileExtension)) {
                        LocalDate today = LocalDate.now();
                        String fileUrlWithoutFileName = String.format("%d/%d/%d",
                                today.getYear(), today.getMonthValue(), today.getDayOfMonth());

                        Path directoryPath = Paths.get(SAVE_PATH, fileUrlWithoutFileName);
                        Files.createDirectories(directoryPath);

                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                        byte[] content = readPartContent(part);
                        byte[] hashBytes = md5.digest(content);
                        String hashedFileName = bytesToHex(hashBytes);

                        String newFileName = hashedFileName + "." + fileExtension;
                        Path filePath = directoryPath.resolve(newFileName);

                        Files.write(filePath, content);
                        logger.info("附件已保存: {}", filePath);

                        try {
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

        private String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }

        private String getFileExtension(String fileName) {
            int lastIndexOfDot = fileName.lastIndexOf('.');
            return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1).toLowerCase();
        }

        private boolean isResumeFile(String extension) {
            return extension.equals("doc") ||
                    extension.equals("docx") ||
                    extension.equals("pdf") ||
                    extension.equals("txt");
        }

        private int extractJobId() {
            // TODO: 实现从邮件中提取jobId的逻辑
            return 1; // 默认值
        }

        private int extractUserId() {
            // TODO: 实现从邮件中提取userId的逻辑
            return 1; // 默认值
        }
    }
}