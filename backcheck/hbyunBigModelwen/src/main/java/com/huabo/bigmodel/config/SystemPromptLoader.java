package com.huabo.bigmodel.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 系统提示词自动加载器
 * 自动加载 classpath:prompts/ 目录下的所有 .md 文件
 * 并将内容拼接后注入到 LlmProperties 的 systemPrompt 中
 * 
 * @author HuaboCloud
 */
@Slf4j
@Component
public class SystemPromptLoader {

    @Autowired
    private LlmProperties llmProperties;

    /**
     * 应用启动时自动加载系统提示词
     */
    @PostConstruct
    public void loadSystemPrompts() {
        try {
            log.info("开始加载系统提示词配置...");
            
            // 扫描 classpath:prompts/ 目录下的所有 .md 文件
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:prompts/*.md");
            
            if (resources == null || resources.length == 0) {
                log.warn("未找到系统提示词配置文件 (prompts/*.md)");
                return;
            }
            
            // 按文件名排序,确保加载顺序一致
            Arrays.sort(resources, Comparator.comparing(Resource::getFilename));
            
            // 读取所有文件内容
            List<String> promptContents = new ArrayList<>();
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                
                // 跳过 README.md
                if ("README.md".equalsIgnoreCase(filename)) {
                    log.debug("跳过文件: {}", filename);
                    continue;
                }
                
                try {
                    String content = readResourceContent(resource);
                    if (content != null && !content.trim().isEmpty()) {
                        promptContents.add(content);
                        log.info("已加载系统提示词: {} ({}字符)", filename, content.length());
                    }
                } catch (Exception e) {
                    log.error("读取系统提示词文件失败: {}", filename, e);
                }
            }
            
            // 拼接所有提示词内容
            if (!promptContents.isEmpty()) {
                String combinedPrompt = String.join("\n\n---\n\n", promptContents);
                
                // 如果配置文件中已有 systemPrompt,则追加
                String existingPrompt = llmProperties.getSystemPrompt();
                if (existingPrompt != null && !existingPrompt.trim().isEmpty()) {
                    combinedPrompt = existingPrompt + "\n\n---\n\n" + combinedPrompt;
                }
                
                // 注入到配置中
                llmProperties.setSystemPrompt(combinedPrompt);
                
                log.info("系统提示词加载完成! 共加载 {} 个文件,总计 {} 字符", 
                        promptContents.size(), combinedPrompt.length());
                log.debug("系统提示词内容预览: {}", 
                        combinedPrompt.length() > 200 ? combinedPrompt.substring(0, 200) + "..." : combinedPrompt);
            } else {
                log.warn("所有系统提示词文件均为空");
            }
            
        } catch (Exception e) {
            log.error("加载系统提示词失败", e);
        }
    }
    
    /**
     * 读取资源文件内容
     */
    private String readResourceContent(Resource resource) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString().trim();
    }
    
    /**
     * 获取当前加载的系统提示词
     */
    public String getCurrentSystemPrompt() {
        return llmProperties.getSystemPrompt();
    }
}

