package org.example;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.Properties;
import java.text.SimpleDateFormat;

public class QQEmailReader {
    private static final String EMAIL_ACCOUNT = "2141377591@qq.com";
    private static final String EMAIL_PASSWORD = "wibrttcsbjvgedde";
    private static final String POP3_HOST = "pop.qq.com";
    private static final String SAVE_PATH = "E:\\study\\Big_ruan\\code\\java_code\\attachment";
    private static boolean contentDisplayed = false; // 用于跟踪是否已显示内容

    public static void readAllEmails() throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.host", POP3_HOST);
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.ssl.enable", "true");

        Session session = Session.getInstance(props);
        Store store = session.getStore("pop3");

        try {
            store.connect(POP3_HOST, EMAIL_ACCOUNT, EMAIL_PASSWORD);
            System.out.println("成功连接到邮箱！\n");

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();
            System.out.println("总邮件数: " + messages.length + "\n");

            // 显示最近的10封邮件
            for (int i = messages.length - 1; i >= Math.max(0, messages.length - 10); i--) {
                contentDisplayed = false; // 重置标志
                displayEmailInfo(messages[i], messages.length - i);
            }

            folder.close(false);
        } finally {
            store.close();
        }
    }

    private static void displayEmailInfo(Message message, int number) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("\n========== 第 " + number + " 封邮件 ==========");

        // 解码发件人信息
        Address[] froms = message.getFrom();
        if (froms != null && froms.length > 0) {
            InternetAddress addr = (InternetAddress) froms[0];
            String personal = addr.getPersonal();
            if (personal != null) {
                personal = MimeUtility.decodeText(personal);
            }
            System.out.println("发件人: " + (personal != null ? personal : "") + " <" + addr.getAddress() + ">");
        }

        // 解码邮件主题
        String subject = message.getSubject();
        if (subject != null) {
            subject = MimeUtility.decodeText(subject);
        }
        System.out.println("主题: " + subject);

        // 显示发送时间
        System.out.println("时间: " + sdf.format(message.getSentDate()));

        // 处理邮件内容和附件
        handleContent(message);

        System.out.println("=====================================");
    }

    private static void handleContent(Part part) throws Exception {
        // 如果是附件或内嵌内容
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();

                // 处理附件
                if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT) ||
                        disposition.equalsIgnoreCase(Part.INLINE))) {
                    saveAttachment(bodyPart);
                }
                // 处理正文
                else {
                    handleContent(bodyPart);
                }
            }
        }
        // 优先显示纯文本内容
        else if (part.isMimeType("text/plain") && !contentDisplayed) {
            System.out.println("内容: " + part.getContent().toString().trim());
            contentDisplayed = true;
        }
        // 如果没有纯文本内容，则显示HTML内容
        else if (part.isMimeType("text/html") && !contentDisplayed) {
            System.out.println("内容: " + part.getContent().toString().trim());
            contentDisplayed = true;
        }
    }

    private static void saveAttachment(BodyPart bodyPart) throws Exception {
        String fileName = bodyPart.getFileName();
        if (fileName != null) {
            fileName = MimeUtility.decodeText(fileName); // 解码文件名
            System.out.println("\n发现附件: " + fileName);

            // 创建保存目录
            File dir = new File(SAVE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 创建时间戳子目录
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            File subDir = new File(dir, timestamp);
            if (!subDir.exists()) {
                subDir.mkdirs();
            }

            // 保存附件
            String fullPath = subDir.getPath() + File.separator + fileName;
            try (InputStream is = bodyPart.getInputStream();
                 FileOutputStream fos = new FileOutputStream(fullPath)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                System.out.println("附件已保存到: " + fullPath);
            } catch (IOException e) {
                System.out.println("保存附件失败: " + e.getMessage());
                throw e;
            }
        }
    }
}