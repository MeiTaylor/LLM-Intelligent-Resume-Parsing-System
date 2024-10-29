package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class communicate {
    private static final String API_URL = "https://cn2us02.opapi.win/v1/chat/completions";
    private static final String API_KEY = "sk-HPl4EATP60B3E73fb251T3BLbkFJ02E42fB642Ab4748BBd9";  // 替换为你的API密钥

    public static String askGPT(String question) {
        HttpURLConnection conn = null;
        try {
            // 创建URL对象
            URL url = new URL(API_URL);

            // 创建连接
            conn = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            conn.setRequestMethod("POST");

            // 设置请求头
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("User-Agent", "Apifox/1.0.0 (https://apifox.com)");

            // 允许输入输出
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 构建JSON请求体（手动构建）
            String requestBody = String.format(
                    "{\"model\":\"gpt-4o-mini\"," +
                            "\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}",
                    question.replace("\"", "\\\"")  // 转义引号
            );

            // 写入请求体
            try (OutputStream os = conn.getOutputStream();
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
                writer.write(requestBody);
                writer.flush();
            }

            // 获取响应
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // 手动解析JSON响应
                    String responseStr = response.toString();
                    // 查找消息内容
                    int contentStart = responseStr.indexOf("\"content\":\"") + 11;
                    int contentEnd = responseStr.indexOf("\"", contentStart);

                    if (contentStart > 10 && contentEnd > contentStart) {
                        return responseStr.substring(contentStart, contentEnd)
                                .replace("\\n", "\n")
                                .replace("\\\"", "\"")
                                .replace("\\\\", "\\");
                    }
                }
            }

            return "Error: Response code " + responseCode;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}