package com.huabo.bigmodel.service;

import com.huabo.bigmodel.config.LlmProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * LLM服务工厂
 * 根据配置或请求参数选择合适的LLM服务
 */
@Slf4j
@Service
public class LlmServiceFactory {

    private final LlmProperties llmProperties;
    private final Map<String, LlmService> serviceMap = new HashMap<>();

    public LlmServiceFactory(
            LlmProperties llmProperties,
            @Qualifier("openaiService") LlmService openaiService,
            @Qualifier("claudeService") LlmService claudeService,
            @Qualifier("zhipuService") LlmService zhipuService) {
        this.llmProperties = llmProperties;
        this.serviceMap.put("openai", openaiService);
        this.serviceMap.put("claude", claudeService);
        this.serviceMap.put("zhipu", zhipuService);
    }

    @PostConstruct
    public void init() {
        log.info("LLM服务工厂初始化完成，默认提供商: {}", llmProperties.getProvider());
        serviceMap.forEach((name, service) -> {
            log.info("  - {} 服务: {}", name, service.isAvailable() ? "可用" : "未配置");
        });
    }

    /**
     * 获取默认的LLM服务
     */
    public LlmService getDefaultService() {
        return getService(llmProperties.getProvider());
    }

    /**
     * 根据提供商名称获取LLM服务
     */
    public LlmService getService(String provider) {
        LlmService service = serviceMap.get(provider);
        if (service == null) {
            log.warn("未找到提供商 {} 的服务，使用默认提供商", provider);
            service = serviceMap.get(llmProperties.getProvider());
        }
        return service;
    }

    /**
     * 根据模型名称智能选择服务
     * 例如：gpt-* 使用openai，claude-* 使用claude，glm-* 使用zhipu
     */
    public LlmService getServiceByModel(String model) {
        if (model == null) {
            return getDefaultService();
        }

        String lowerModel = model.toLowerCase();
        if (lowerModel.startsWith("gpt-") || lowerModel.startsWith("o1-") || lowerModel.startsWith("o3-")) {
            return getService("openai");
        } else if (lowerModel.startsWith("claude-")) {
            return getService("claude");
        } else if (lowerModel.startsWith("glm-")) {
            return getService("zhipu");
        }

        // 默认使用配置的提供商
        return getDefaultService();
    }

    /**
     * 检查指定提供商是否可用
     */
    public boolean isProviderAvailable(String provider) {
        LlmService service = serviceMap.get(provider);
        return service != null && service.isAvailable();
    }
}

