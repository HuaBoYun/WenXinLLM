package com.huabo.bigmodel.demo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 智谱AI多轮对话示例 - 展示如何设置上下文
 * 
 * 编译: javac -encoding UTF-8 ZhipuContextDemo.java
 * 运行: java ZhipuContextDemo
 */
public class ZhipuContextDemo {

    // 智谱AI配置
    private static final String API_KEY = "c5c7af5081b74a829befe6b63577ca8b.xad0SCAvD2ZygAnc";  // 替换为你的API Key
    private static final String BASE_URL = "https://open.bigmodel.cn/api/anthropic/v1/messages";
    private static final String MODEL = "glm-4.7";

    public static void main(String[] args) {
        System.out.println("=== 智谱AI多轮对话示例 ===\n");
        
        // 1. 系统提示词 (设置AI的角色和行为)
        String systemPrompt = "你是一个专业的数据库专家，名叫小智。" +
                "你擅长编写高效、安全的SQL查询语句。" +
                "回答时请简洁明了，必要时给出代码示例。";
        
        // 2. 对话历史 (多轮对话上下文)
        List<Message> conversationHistory = new ArrayList<>();
        
        // 第一轮对话
        conversationHistory.add(new Message("user", "你好，请介绍一下你自己"));
        conversationHistory.add(new Message("assistant", "你好！我是小智，一个专业的数据库专家。我可以帮你编写SQL查询、优化数据库性能、解答数据库相关问题。有什么我可以帮助你的吗？"));
        
        // 第二轮对话 (基于上下文的新问题)
        conversationHistory.add(new Message("user", "我有一个用户表users，包含id、username、age、email字段，帮我查询年龄大于18岁的用户"));
        
        System.out.println("系统提示词: " + systemPrompt);
        System.out.println("\n对话历史:");
        for (Message msg : conversationHistory) {
            System.out.println("  [" + msg.role + "]: " + msg.content);
        }
        System.out.println("\n--- 开始请求 ---\n");
        
        try {
            chat(systemPrompt, conversationHistory);
        } catch (Exception e) {
            System.err.println("请求失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送对话请求
     */
    public static void chat(String systemPrompt, List<Message> messages) throws Exception {
        // 构建请求体
        StringBuilder requestBody = new StringBuilder();
        requestBody.append("{\"model\":\"").append(MODEL).append("\"");
        requestBody.append(",\"max_tokens\":4096");
        requestBody.append(",\"stream\":true");
        
        // 设置系统提示词
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            requestBody.append(",\"system\":\"").append(escapeJson(systemPrompt)).append("\"");
        }
        
        // 添加对话历史
        requestBody.append(",\"messages\":[");
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (i > 0) {
                requestBody.append(",");
            }
            requestBody.append("{\"role\":\"").append(msg.role).append("\"");
            requestBody.append(",\"content\":\"").append(escapeJson(msg.content)).append("\"}");
        }
        requestBody.append("]}");

        System.out.println("请求体: " + requestBody.toString());
        System.out.println("\n回答: ");

        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(120000);
        conn.setReadTimeout(120000);
        
        conn.setRequestProperty("x-api-key", API_KEY);
        conn.setRequestProperty("anthropic-version", "2023-06-01");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "text/event-stream");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder error = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    error.append(line);
                }
                System.err.println("请求失败 [" + responseCode + "]: " + error.toString());
            }
            return;
        }

        // 读取流式响应
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("data: ")) {
                    String data = line.substring(6);
                    if ("[DONE]".equals(data)) {
                        break;
                    }
                    
                    String text = extractField(data, "text");
                    if (text != null && !text.isEmpty()) {
                        System.out.print(text);
                        response.append(text);
                    }
                }
            }
        }

        System.out.println("\n\n--- 响应结束 ---");
    }

    // 消息类
    static class Message {
        String role;    // "user" 或 "assistant"
        String content;
        
        Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    private static String extractField(String jsonData, String fieldName) {
        String searchKey = "\"" + fieldName + "\":\"";
        int start = jsonData.indexOf(searchKey);
        if (start != -1) {
            start += searchKey.length();
            int end = start;
            boolean escaped = false;
            while (end < jsonData.length()) {
                char c = jsonData.charAt(end);
                if (escaped) {
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    break;
                }
                end++;
            }
            if (end > start) {
                return unescapeJson(jsonData.substring(start, end));
            }
        }
        return null;
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String unescapeJson(String s) {
        return s.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}

