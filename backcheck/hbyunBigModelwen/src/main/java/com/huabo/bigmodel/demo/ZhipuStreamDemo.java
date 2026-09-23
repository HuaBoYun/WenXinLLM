package com.huabo.bigmodel.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 智谱AI多轮对话Demo - 支持联网搜索(web_search)
 * 自动记录对话上下文，支持深度思考和工具调用
 *
 * 运行方式:
 * 1. 在IDE中直接运行main方法
 * 2. 或者编译后: java -cp "target/classes;target/dependency/*" com.huabo.bigmodel.demo.ZhipuStreamDemo
 */
public class ZhipuStreamDemo {

    // 智谱AI配置 - 请替换为你的API Key
    private static final String API_KEY = "c5c7af5081b74a829befe6b63577ca8b.xad0SCAvD2ZygAnc";
    private static final String BASE_URL = "https://open.bigmodel.cn/api/anthropic";
    private static final String MODEL = "glm-4.7";

    // 是否开启深度思考 (Extended Thinking)
    private static final boolean ENABLE_THINKING = false;
    // 思考过程的token预算 (建议10000-50000)
    private static final int THINKING_BUDGET_TOKENS = 10000;

    // 是否开启联网搜索 (Web Search)
    private static final boolean ENABLE_WEB_SEARCH = true;

    // ========== 搜索API配置 (选择一个配置即可) ==========
    // 方案1: Serper.dev (推荐，免费2500次/月) - 注册: https://serper.dev
    private static final String SERPER_API_KEY = "";  // 填入你的Serper API Key

    // 方案2: Tavily (专为AI设计，免费1000次/月) - 注册: https://tavily.com
    private static final String TAVILY_API_KEY = "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";

    // 系统提示词
    private static final String SYSTEM_PROMPT = "你是一个专业的AI助手，名叫小智。你擅长回答各种问题，编写代码，解决技术难题。" +
            "当用户询问实时信息、新闻、天气、股票等需要联网查询的问题时，请使用web_search工具进行搜索。";

    // 对话历史 (自动记录上下文)
    private static final List<JSONObject> conversationHistory = new ArrayList<>();

    // HTTP客户端
    private static OkHttpClient httpClient;

    public static void main(String[] args) throws Exception {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     智谱AI多轮对话Demo - 支持联网搜索(web_search)           ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  输入 'exit' 或 'quit' 退出                                ║");
        System.out.println("║  输入 'clear' 清空对话历史                                 ║");
        System.out.println("║  输入 'history' 查看对话历史                               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\n系统提示词: " + SYSTEM_PROMPT);
        System.out.println("深度思考模式: " + (ENABLE_THINKING ? "开启" : "关闭"));
        System.out.println("联网搜索模式: " + (ENABLE_WEB_SEARCH ? "开启" : "关闭"));
        System.out.println("\n");

        // 初始化HTTP客户端
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();

        // 交互式对话循环
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

        while (true) {
            System.out.print("\n👤 你: ");
            String userInput = reader.readLine();

            if (userInput == null || userInput.trim().isEmpty()) {
                continue;
            }

            userInput = userInput.trim();

            // 处理特殊命令
            if ("exit".equalsIgnoreCase(userInput) || "quit".equalsIgnoreCase(userInput)) {
                System.out.println("\n再见！对话已结束。");
                break;
            }

            if ("clear".equalsIgnoreCase(userInput)) {
                conversationHistory.clear();
                System.out.println("✅ 对话历史已清空");
                continue;
            }

            if ("history".equalsIgnoreCase(userInput)) {
                printHistory();
                continue;
            }

            // 发送消息并获取回复
            System.out.println("\n🤖 小智: ");
            String response = chat(userInput);

            if (response != null && !response.isEmpty()) {
                // 自动记录对话历史
                addToHistory("user", userInput);
                addToHistory("assistant", response);
            }
        }

        reader.close();
    }

    /**
     * 添加消息到对话历史
     */
    private static void addToHistory(String role, String content) {
        JSONObject message = new JSONObject();
        message.put("role", role);
        message.put("content", content);
        conversationHistory.add(message);
    }

    /**
     * 打印对话历史
     */
    private static void printHistory() {
        if (conversationHistory.isEmpty()) {
            System.out.println("📜 对话历史为空");
            return;
        }

        System.out.println("\n📜 对话历史 (" + conversationHistory.size() + " 条消息):");
        System.out.println("──────────────────────────────────────────────────");
        for (int i = 0; i < conversationHistory.size(); i++) {
            JSONObject msg = conversationHistory.get(i);
            String role = msg.getString("role");
            String content = msg.getString("content");
            String icon = "user".equals(role) ? "👤" : "🤖";
            String roleName = "user".equals(role) ? "你" : "小智";

            // 截断过长的内容
            if (content.length() > 100) {
                content = content.substring(0, 100) + "...";
            }
            System.out.println(String.format("[%d] %s %s: %s", i + 1, icon, roleName, content));
        }
        System.out.println("──────────────────────────────────────────────────");
    }

    /**
     * 发送消息并获取流式回复（支持工具调用）
     * @param userMessage 用户消息
     * @return AI回复内容
     */
    public static String chat(String userMessage) throws Exception {
        // 添加用户消息到临时列表
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);

        List<JSONObject> currentMessages = new ArrayList<>(conversationHistory);
        currentMessages.add(userMsg);

        // 循环处理工具调用
        String finalResponse = "";
        int maxToolCalls = 5; // 最多允许5次工具调用
        int toolCallCount = 0;

        while (toolCallCount < maxToolCalls) {
            ChatResult result = sendRequest(currentMessages);

            if (result.error != null) {
                System.err.println("\n❌ 请求失败: " + result.error);
                return "";
            }

            // 如果有工具调用
            if (result.toolUse != null) {
                toolCallCount++;
                String toolName = result.toolUse.getString("name");
                String toolId = result.toolUse.getString("id");
                JSONObject toolInput = result.toolUse.getJSONObject("input");

                System.out.println("\n🔧 [调用工具: " + toolName + "]");

                // 执行工具
                String toolResult = executeWebSearch(toolInput.getString("query"));

                // 添加assistant的工具调用消息
                JSONObject assistantMsg = new JSONObject();
                assistantMsg.put("role", "assistant");
                JSONArray contentArray = new JSONArray();
                contentArray.add(result.toolUse);
                assistantMsg.put("content", contentArray);
                currentMessages.add(assistantMsg);

                // 添加工具结果消息
                JSONObject toolResultMsg = new JSONObject();
                toolResultMsg.put("role", "user");
                JSONArray toolResultContent = new JSONArray();
                JSONObject toolResultBlock = new JSONObject();
                toolResultBlock.put("type", "tool_result");
                toolResultBlock.put("tool_use_id", toolId);
                toolResultBlock.put("content", toolResult);
                toolResultContent.add(toolResultBlock);
                toolResultMsg.put("content", toolResultContent);
                currentMessages.add(toolResultMsg);

                System.out.println("📥 [搜索完成，正在生成回答...]\n");

            } else {
                // 没有工具调用，返回最终结果
                finalResponse = result.textContent;
                break;
            }
        }

        return finalResponse;
    }

    /**
     * 发送请求并解析响应
     */
    private static ChatResult sendRequest(List<JSONObject> messages) throws Exception {
        // 构建Anthropic格式请求
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("stream", true);

        // 设置系统提示词
        if (SYSTEM_PROMPT != null && !SYSTEM_PROMPT.isEmpty()) {
            requestBody.put("system", SYSTEM_PROMPT);
        }

        // 开启深度思考 (Extended Thinking)
        if (ENABLE_THINKING) {
            requestBody.put("max_tokens", 16000);
            JSONObject thinking = new JSONObject();
            thinking.put("type", "enabled");
            thinking.put("budget_tokens", THINKING_BUDGET_TOKENS);
            requestBody.put("thinking", thinking);
        } else {
            requestBody.put("max_tokens", 4096);
        }

        // 添加web_search工具定义
        if (ENABLE_WEB_SEARCH) {
            JSONArray tools = new JSONArray();
            JSONObject webSearchTool = new JSONObject();
            webSearchTool.put("name", "web_search");
            webSearchTool.put("description", "搜索互联网获取实时信息，如新闻、天气、股票、最新事件等。当用户询问需要联网查询的实时信息时使用此工具。");

            JSONObject inputSchema = new JSONObject();
            inputSchema.put("type", "object");
            JSONObject properties = new JSONObject();
            JSONObject queryProp = new JSONObject();
            queryProp.put("type", "string");
            queryProp.put("description", "搜索关键词");
            properties.put("query", queryProp);
            inputSchema.put("properties", properties);
            JSONArray required = new JSONArray();
            required.add("query");
            inputSchema.put("required", required);

            webSearchTool.put("input_schema", inputSchema);
            tools.add(webSearchTool);
            requestBody.put("tools", tools);
        }

        // 构建消息列表
        JSONArray messagesArray = new JSONArray();
        for (JSONObject msg : messages) {
            messagesArray.add(msg);
        }
        requestBody.put("messages", messagesArray);

        String url = BASE_URL + "/v1/messages";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-api-key", API_KEY)
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "text/event-stream")
                .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        ChatResult result = new ChatResult();
        StringBuilder fullResponse = new StringBuilder();
        StringBuilder thinkingContent = new StringBuilder();
        final boolean[] inThinking = {false};
        final JSONObject[] currentToolUse = {null};

        EventSource.Factory factory = EventSources.createFactory(httpClient);
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                try {
                    JSONObject event = JSON.parseObject(data);
                    String eventType = event.getString("type");

                    // 处理内容块开始
                    if ("content_block_start".equals(eventType)) {
                        JSONObject contentBlock = event.getJSONObject("content_block");
                        if (contentBlock != null) {
                            String blockType = contentBlock.getString("type");
                            if ("thinking".equals(blockType)) {
                                inThinking[0] = true;
                                System.out.println("\n🧠 [思考中...]");
                            } else if ("text".equals(blockType)) {
                                if (inThinking[0]) {
                                    inThinking[0] = false;
                                    System.out.println("\n💡 [思考完成]\n");
                                }
                            } else if ("tool_use".equals(blockType)) {
                                // 工具调用开始
                                currentToolUse[0] = new JSONObject();
                                currentToolUse[0].put("type", "tool_use");
                                currentToolUse[0].put("id", contentBlock.getString("id"));
                                currentToolUse[0].put("name", contentBlock.getString("name"));
                                currentToolUse[0].put("input", new JSONObject());
                            }
                        }
                    }
                    // 处理内容增量
                    else if ("content_block_delta".equals(eventType)) {
                        JSONObject delta = event.getJSONObject("delta");
                        String deltaType = delta.getString("type");

                        if ("thinking_delta".equals(deltaType)) {
                            String thinking = delta.getString("thinking");
                            if (thinking != null) {
                                thinkingContent.append(thinking);
                            }
                        } else if ("text_delta".equals(deltaType)) {
                            String text = delta.getString("text");
                            if (text != null) {
                                System.out.print(text);
                                fullResponse.append(text);
                            }
                        } else if ("input_json_delta".equals(deltaType)) {
                            // 工具输入增量
                            String partialJson = delta.getString("partial_json");
                            if (partialJson != null && currentToolUse[0] != null) {
                                // 累积JSON片段
                                String existingInput = currentToolUse[0].getString("_partial_input");
                                if (existingInput == null) {
                                    existingInput = "";
                                }
                                currentToolUse[0].put("_partial_input", existingInput + partialJson);
                            }
                        }
                    }
                    // 内容块结束
                    else if ("content_block_stop".equals(eventType)) {
                        if (currentToolUse[0] != null) {
                            // 解析完整的工具输入
                            String partialInput = currentToolUse[0].getString("_partial_input");
                            if (partialInput != null) {
                                try {
                                    JSONObject input = JSON.parseObject(partialInput);
                                    currentToolUse[0].put("input", input);
                                } catch (Exception e) {
                                    // 解析失败，使用空对象
                                }
                                currentToolUse[0].remove("_partial_input");
                            }
                            result.toolUse = currentToolUse[0];
                        }
                    }
                    else if ("message_stop".equals(eventType)) {
                        latch.countDown();
                    }
                } catch (Exception e) {
                    // 忽略解析错误
                }
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                String errorMsg = "";
                if (response != null) {
                    try {
                        errorMsg = response.body() != null ? response.body().string() : "";
                    } catch (IOException ignored) {}
                }
                result.error = t != null ? t.getMessage() : errorMsg;
                latch.countDown();
            }

            @Override
            public void onClosed(EventSource eventSource) {
                latch.countDown();
            }
        });

        // 等待响应完成
        latch.await(180, TimeUnit.SECONDS);

        result.textContent = fullResponse.toString();
        result.thinkingContent = thinkingContent.toString();

        if (result.thinkingContent.length() > 0) {
            System.out.println("\n📊 [思考过程: " + result.thinkingContent.length() + " 字符]");
        }

        return result;
    }

    /**
     * 执行网络搜索 - 使用多种搜索源
     */
    private static String executeWebSearch(String query) {
        System.out.println("🔍 搜索: " + query);

        try {
            // 优先使用专业搜索API

            // 方案1: Serper.dev (Google搜索结果)
            if (SERPER_API_KEY != null && !SERPER_API_KEY.isEmpty()) {
                String serperResult = searchWithSerper(query);
                if (serperResult != null && !serperResult.isEmpty()) {
                    return serperResult;
                }
            }

            // 方案2: Tavily (专为AI设计)
            if (TAVILY_API_KEY != null && !TAVILY_API_KEY.isEmpty()) {
                String tavilyResult = searchWithTavily(query);
                if (tavilyResult != null && !tavilyResult.isEmpty()) {
                    return tavilyResult;
                }
            }

            // 方案3: 天气查询使用专门API
//            String lowerQuery = query.toLowerCase();
//            if (lowerQuery.contains("天气") || lowerQuery.contains("weather") ||
//                lowerQuery.contains("温度") || lowerQuery.contains("气温")) {
//                String weatherResult = getWeather(query);
//                if (weatherResult != null && !weatherResult.isEmpty()) {
//                    return weatherResult;
//                }
//            }

            // 方案4: 备用 - Bing网页搜索
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String bingResult = searchWithBing(encodedQuery);
            if (bingResult != null && !bingResult.isEmpty()) {
                return bingResult;
            }

            // 方案5: 备用 - DuckDuckGo
            String ddgResult = searchWithDuckDuckGo(encodedQuery);
            if (ddgResult != null && !ddgResult.isEmpty()) {
                return ddgResult;
            }

            return "搜索完成，但未找到直接相关的结果。建议配置 SERPER_API_KEY 或 TAVILY_API_KEY 以获得更好的搜索效果。";

        } catch (Exception e) {
            return "搜索出错: " + e.getMessage() + "。我将根据已有知识来回答。";
        }
    }

    /**
     * 使用Serper.dev搜索 (Google搜索结果，免费2500次/月)
     * 注册地址: https://serper.dev
     */
    private static String searchWithSerper(String query) {
        try {
            System.out.println("  📡 使用Serper搜索...");

            JSONObject requestBody = new JSONObject();
            requestBody.put("q", query);
            requestBody.put("gl", "cn");  // 地区：中国
            requestBody.put("hl", "zh-cn"); // 语言：中文

            Request request = new Request.Builder()
                    .url("https://google.serper.dev/search")
                    .addHeader("X-API-KEY", SERPER_API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String body = response.body().string();
                    JSONObject result = JSON.parseObject(body);

                    StringBuilder searchResult = new StringBuilder();
                    searchResult.append("【搜索结果】\n\n");

                    // 知识图谱答案
                    JSONObject knowledgeGraph = result.getJSONObject("knowledgeGraph");
                    if (knowledgeGraph != null) {
                        String title = knowledgeGraph.getString("title");
                        String description = knowledgeGraph.getString("description");
                        if (title != null) {
                            searchResult.append("📌 ").append(title);
                            if (description != null) {
                                searchResult.append(": ").append(description);
                            }
                            searchResult.append("\n\n");
                        }
                    }

                    // 精选摘要
                    JSONObject answerBox = result.getJSONObject("answerBox");
                    if (answerBox != null) {
                        String answer = answerBox.getString("answer");
                        String snippet = answerBox.getString("snippet");
                        if (answer != null) {
                            searchResult.append("💡 答案: ").append(answer).append("\n\n");
                        } else if (snippet != null) {
                            searchResult.append("💡 摘要: ").append(snippet).append("\n\n");
                        }
                    }

                    // 搜索结果列表
                    JSONArray organic = result.getJSONArray("organic");
                    if (organic != null && organic.size() > 0) {
                        searchResult.append("相关结果:\n");
                        int count = Math.min(5, organic.size());
                        for (int i = 0; i < count; i++) {
                            JSONObject item = organic.getJSONObject(i);
                            String title = item.getString("title");
                            String snippet = item.getString("snippet");
                            String link = item.getString("link");

                            searchResult.append(i + 1).append(". ").append(title).append("\n");
                            if (snippet != null) {
                                searchResult.append("   ").append(snippet).append("\n");
                            }
                            searchResult.append("   🔗 ").append(link).append("\n\n");
                        }
                    }

                    if (searchResult.length() > 20) {
                        return searchResult.toString();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  Serper搜索失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 使用Tavily搜索 (专为AI设计，免费1000次/月)
     * 注册地址: https://tavily.com
     */
    private static String searchWithTavily(String query) {
        try {
            System.out.println("  📡 使用Tavily搜索...");

            JSONObject requestBody = new JSONObject();
            requestBody.put("api_key", TAVILY_API_KEY);
            requestBody.put("query", query);
            requestBody.put("search_depth", "basic");
            requestBody.put("include_answer", true);
            requestBody.put("include_raw_content", false);
            requestBody.put("max_results", 5);

            Request request = new Request.Builder()
                    .url("https://api.tavily.com/search")
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String body = response.body().string();
                    JSONObject result = JSON.parseObject(body);

                    StringBuilder searchResult = new StringBuilder();
                    searchResult.append("【搜索结果】\n\n");

                    // Tavily的AI生成答案
                    String answer = result.getString("answer");
                    if (answer != null && !answer.isEmpty()) {
                        searchResult.append("💡 AI摘要: ").append(answer).append("\n\n");
                    }

                    // 搜索结果
                    JSONArray results = result.getJSONArray("results");
                    if (results != null && results.size() > 0) {
                        searchResult.append("相关来源:\n");
                        for (int i = 0; i < results.size(); i++) {
                            JSONObject item = results.getJSONObject(i);
                            String title = item.getString("title");
                            String content = item.getString("content");
                            String url = item.getString("url");

                            searchResult.append(i + 1).append(". ").append(title).append("\n");
                            if (content != null) {
                                // 截取前200字符
                                String snippet = content.length() > 200 ? content.substring(0, 200) + "..." : content;
                                searchResult.append("   ").append(snippet).append("\n");
                            }
                            searchResult.append("   🔗 ").append(url).append("\n\n");
                        }
                    }

                    if (searchResult.length() > 20) {
                        return searchResult.toString();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  Tavily搜索失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 使用Bing搜索
     */
    private static String searchWithBing(String encodedQuery) {
        try {
            String searchUrl = "https://www.bing.com/search?q=" + encodedQuery + "&setlang=zh-CN";

            Request request = new Request.Builder()
                    .url(searchUrl)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/192.0.2.200 Safari/537.36")
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String html = response.body().string();
                    return parseBingResults(html);
                }
            }
        } catch (Exception e) {
            System.out.println("  Bing搜索失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 解析Bing搜索结果
     */
    private static String parseBingResults(String html) {
        StringBuilder results = new StringBuilder();

        try {
            // 提取搜索结果摘要 (简单的正则匹配)
            // Bing的搜索结果通常在 <li class="b_algo"> 标签中
            int count = 0;
            int startIndex = 0;

            while (count < 5 && startIndex < html.length()) {
                // 查找结果块
                int algoStart = html.indexOf("class=\"b_algo\"", startIndex);
                if (algoStart == -1) {
                    break;
                }

                // 查找标题
                int titleStart = html.indexOf("<a", algoStart);
                if (titleStart == -1 || titleStart > algoStart + 500) {
                    startIndex = algoStart + 20;
                    continue;
                }

                int titleTextStart = html.indexOf(">", titleStart);
                int titleTextEnd = html.indexOf("</a>", titleTextStart);

                if (titleTextStart != -1 && titleTextEnd != -1 && titleTextEnd > titleTextStart) {
                    String title = html.substring(titleTextStart + 1, titleTextEnd);
                    title = stripHtmlTags(title).trim();

                    // 查找摘要
                    int captionStart = html.indexOf("class=\"b_caption\"", titleTextEnd);
                    if (captionStart != -1 && captionStart < titleTextEnd + 1000) {
                        int pStart = html.indexOf("<p", captionStart);
                        int pEnd = html.indexOf("</p>", pStart);

                        if (pStart != -1 && pEnd != -1 && pEnd > pStart) {
                            int pContentStart = html.indexOf(">", pStart);
                            String snippet = html.substring(pContentStart + 1, pEnd);
                            snippet = stripHtmlTags(snippet).trim();

                            if (!title.isEmpty() && !snippet.isEmpty()) {
                                results.append(count + 1).append(". ").append(title).append("\n");
                                results.append("   ").append(snippet).append("\n\n");
                                count++;
                            }
                        }
                    }
                }

                startIndex = algoStart + 100;
            }

            // 如果没有找到标准结果，尝试提取天气信息
            if (count == 0) {
                // 尝试提取天气卡片信息
                int weatherIndex = html.indexOf("class=\"wtr_");
                if (weatherIndex != -1) {
                    results.append("天气信息:\n");
                    // 提取温度等信息
                    int tempIndex = html.indexOf("class=\"wtr_currTemp\"", weatherIndex);
                    if (tempIndex != -1) {
                        int tempStart = html.indexOf(">", tempIndex);
                        int tempEnd = html.indexOf("<", tempStart + 1);
                        if (tempStart != -1 && tempEnd != -1) {
                            String temp = html.substring(tempStart + 1, tempEnd).trim();
                            results.append("当前温度: ").append(temp).append("\n");
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("  解析Bing结果失败: " + e.getMessage());
        }

        return results.length() > 0 ? results.toString() : null;
    }

    /**
     * 使用DuckDuckGo搜索
     */
    private static String searchWithDuckDuckGo(String encodedQuery) {
        try {
            String searchUrl = "https://api.duckduckgo.com/?q=" + encodedQuery + "&format=json&no_html=1";

            Request request = new Request.Builder()
                    .url(searchUrl)
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String body = response.body().string();
                    JSONObject result = JSON.parseObject(body);

                    StringBuilder searchResult = new StringBuilder();

                    String abstractText = result.getString("Abstract");
                    if (abstractText != null && !abstractText.isEmpty()) {
                        searchResult.append("摘要: ").append(abstractText).append("\n\n");
                    }

                    JSONArray relatedTopics = result.getJSONArray("RelatedTopics");
                    if (relatedTopics != null && relatedTopics.size() > 0) {
                        searchResult.append("相关信息:\n");
                        int count = 0;
                        for (int i = 0; i < relatedTopics.size() && count < 5; i++) {
                            Object item = relatedTopics.get(i);
                            if (item instanceof JSONObject) {
                                JSONObject topic = (JSONObject) item;
                                String text = topic.getString("Text");
                                if (text != null && !text.isEmpty()) {
                                    searchResult.append("- ").append(text).append("\n");
                                    count++;
                                }
                            }
                        }
                    }

                    if (searchResult.length() > 0) {
                        return searchResult.toString();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  DuckDuckGo搜索失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 去除HTML标签
     */
    private static String stripHtmlTags(String html) {
        if (html == null) {
            return "";
        }
        // 移除HTML标签
        String text = html.replaceAll("<[^>]+>", "");
        // 解码HTML实体
        text = text.replace("&nbsp;", " ")
                   .replace("&amp;", "&")
                   .replace("&lt;", "<")
                   .replace("&gt;", ">")
                   .replace("&quot;", "\"")
                   .replace("&#39;", "'")
                   .replace("&hellip;", "...")
                   .replace("&mdash;", "—");
        return text.trim();
    }

    /**
     * 聊天结果类
     */
    private static class ChatResult {
        String textContent = "";
        String thinkingContent = "";
        JSONObject toolUse = null;
        String error = null;
    }
}