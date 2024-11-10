// 声明包名，指定类所在的包路径
package com.ylw.backend.service.impl;

// 导入所需的所有依赖
import jakarta.mail.*;                       // 核心邮件API
import jakarta.mail.internet.InternetAddress;// 邮件地址处理
import jakarta.mail.internet.MimeUtility;    // MIME工具，用于处理邮件编码
import jakarta.mail.search.FlagTerm;         // 邮件搜索功能
import org.slf4j.Logger;                     // 日志接口
import org.slf4j.LoggerFactory;              // 日志工厂
import org.springframework.beans.factory.annotation.Autowired;  // Spring自动注入
import org.springframework.stereotype.Component;                // Spring组件注解
import com.ylw.backend.service.ResumeServiceInterface;         // 简历服务接口
import com.ylw.backend.model.Applicant;                        // 应聘者模型

import java.io.*;                           // IO操作
import java.text.SimpleDateFormat;          // 日期格式化
import java.util.Properties;                // 属性配置
import java.util.concurrent.*;              // 并发工具
import java.util.Map;                       // Map接口

// 标记为Spring组件，使其能被Spring容器管理
@Component
public class EmailUtils {
    // 创建日志记录器实例，用于记录程序运行状态和错误信息
    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);

    // 定义附件保存的根路径
    private static final String SAVE_PATH = "E:\\study\\Big_ruan\\code\\my_code\\down_resume";

    // 定义邮件检查的时间间隔（10秒）
    private static final long CHECK_INTERVAL = 10000;

    // 存储邮箱监控器的Map，使用ConcurrentHashMap确保线程安全
    private final Map<Integer, EmailMonitor> monitors = new ConcurrentHashMap<>();

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

    // 启动邮箱监控
    public void startMonitor(int emailId, String emailAddress, String password) {
        // 如果已存在监控，先停止旧的监控
        stopMonitor(emailId);

        // 获取邮箱服务器地址
        String host = getEmailHost(emailAddress);
        if (host == null) {
            logger.error("不支持的邮箱类型：" + emailAddress);
            return;
        }

        // 创建新的监控器
        EmailMonitor monitor = new EmailMonitor(emailAddress, password, host, emailId);
        monitors.put(emailId, monitor);
        // 提交监控任务到线程池并保存Future对象
        Future<?> future = executorService.submit(monitor);
        monitorFutures.put(emailId, future);
        logger.info("开始监控邮箱: " + emailAddress);
    }

    // 停止指定邮箱的监控
    public void stopMonitor(int emailId) {
        // 移除并停止监控器
        EmailMonitor monitor = monitors.remove(emailId);
        if (monitor != null) {
            monitor.stop();
        }

        // 取消监控任务
        Future<?> future = monitorFutures.remove(emailId);
        if (future != null) {
            future.cancel(true);
        }
        logger.info("停止监控邮箱ID: " + emailId);
    }

    // 停止所有邮箱监控
    public void stopAllMonitors() {
        // 遍历停止所有监控器
        for (Integer emailId : monitors.keySet()) {
            stopMonitor(emailId);
        }
        // 关闭线程池
        executorService.shutdown();
        logger.info("停止所有邮箱监控");
    }

    // 检查指定邮箱是否正在被监控
    public boolean isMonitoring(int emailId) {
        return monitors.containsKey(emailId);
    }

    // 内部类：邮箱监控器
    private class EmailMonitor implements Runnable {
        private final String emailAddress;  // 邮箱地址
        private final String password;      // 邮箱密码
        private final String host;          // 服务器地址
        private volatile boolean running = true;  // 运行状态标志
        private final int emailId;          // 邮箱ID

        // 构造函数：初始化监控器
        public EmailMonitor(String emailAddress, String password, String host, int emailId) {
            this.emailAddress = emailAddress;
            this.password = password;
            this.host = host;
            this.emailId = emailId;
        }

        // 停止监控器运行
        public void stop() {
            running = false;
        }

        // 监控器运行的主方法
        @Override
        public void run() {
            while (running) {
                try {
                    checkNewEmails();  // 检查新邮件
                    Thread.sleep(CHECK_INTERVAL);  // 等待指定时间
                } catch (Exception e) {
                    logger.error("检查邮件时发生错误: " + e.getMessage());
                    try {
                        Thread.sleep(CHECK_INTERVAL);  // 发生错误后等待
                    } catch (InterruptedException ie) {
                        break;  // 如果在等待时被中断，则退出循环
                    }
                }
            }
        }

        // 检查新邮件的具体实现
        private void checkNewEmails() {
            // 设置邮箱连接属性
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imap");
            props.setProperty("mail.imap.host", host);
            props.setProperty("mail.imap.port", "993");
            props.setProperty("mail.imap.ssl.enable", "true");

            try {
                // 创建邮件会话并连接服务器
                Session session = Session.getInstance(props);
                Store store = session.getStore("imap");
                store.connect(host, emailAddress, password);

                // 打开收件箱
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);

                // 搜索未读邮件
                Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                logger.info("发现 {} 封未读邮件", messages.length);

                // 处理每封未读邮件
                for (Message message : messages) {
                    processEmail(message);
                    message.setFlag(Flags.Flag.SEEN, true);  // 标记为已读
                }

                // 关闭资源
                folder.close(false);
                store.close();
            } catch (Exception e) {
                logger.error("处理邮件时发生错误: ", e);
            }
        }

        // 处理单封邮件
        private void processEmail(Message message) throws Exception {
            // 获取邮件主题和发件人信息
            String subject = message.getSubject();
            String from = ((InternetAddress) message.getFrom()[0]).getAddress();
            logger.info("处理新邮件 - 发件人: {} 主题: {}", from, subject);

            // 处理邮件内容
            handleContent(message);
        }

        // 处理邮件内容
        private void handleContent(Part part) throws Exception {
            if (part.isMimeType("multipart/*")) {
                // 如果是多部分邮件，递归处理每个部分
                Multipart multipart = (Multipart) part.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    handleContent(multipart.getBodyPart(i));
                }
            } else if (part.getDisposition() != null &&
                    (part.getDisposition().equals(Part.ATTACHMENT) ||
                            part.getDisposition().equals(Part.INLINE))) {
                // 如果是附件，保存附件
                saveAttachment(part);
            }
        }

        // 保存附件并处理简历
        private void saveAttachment(Part part) throws Exception {
            // 解码附件文件名
            String fileName = MimeUtility.decodeText(part.getFileName());
            if (fileName != null) {
                // 创建保存目录
                File dir = new File(SAVE_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // 创建带时间戳的子目录
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
                File subDir = new File(dir, timestamp);
                if (!subDir.exists()) {
                    subDir.mkdirs();
                }

                // 构建完整的文件保存路径
                String fullPath = subDir.getPath() + File.separator + fileName;

                // 保存附件文件
                try (InputStream is = part.getInputStream();
                     FileOutputStream fos = new FileOutputStream(fullPath)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    logger.info("附件已保存: {}", fullPath);

                    // 调用简历解析服务
                    try {
                        // 解析简历文件
                        Applicant applicant = resumeService.parseResume(fullPath);
                        // 保存简历到数据库（使用临时的jobId和userId）
                        resumeService.createAndSaveResume(fullPath, applicant, 1, 1);
                        logger.info("简历解析和保存成功");
                    } catch (Exception e) {
                        logger.error("简历解析或保存失败: " + e.getMessage());
                    }
                } catch (IOException e) {
                    logger.error("保存附件失败: " + e.getMessage());
                    throw e;
                }
            }
        }
    }
}