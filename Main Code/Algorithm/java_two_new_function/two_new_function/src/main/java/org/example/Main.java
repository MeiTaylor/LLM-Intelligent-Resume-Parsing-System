package org.example;
public class Main {
    public static void main(String[] args) {
//        try {
//            System.out.println("开始连接邮箱...");
//            // 读取所有邮件
//
//            QQEmailReader.readAllEmails();
//        } catch (Exception e) {
//            System.out.println("程序出现异常：" + e.getMessage());
//            e.printStackTrace();
//        }


        // 测试调用
        String question = "What is the weather like today?";
        String answer =communicate.askGPT(question);

        System.out.println("Question: " + question);
        System.out.println("Answer: " + answer);




    }
}