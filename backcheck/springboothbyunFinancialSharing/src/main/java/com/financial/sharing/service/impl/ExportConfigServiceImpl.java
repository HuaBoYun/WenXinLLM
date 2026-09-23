package com.financial.sharing.service.impl;

import com.financial.sharing.enums.ExportFormat;
import com.financial.sharing.service.ExportConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 导出配置管理服务实现
 */
@Slf4j
@Service
public class ExportConfigServiceImpl implements ExportConfigService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Redis键前缀
    private static final String CONFIG_PREFIX = "export:config:";
    private static final String PREFERENCE_PREFIX = "export:preference:";
    private static final String TEMPLATE_PREFIX = "export:template:";

    @Override
    public List<ExportFormat> getSupportedFormats() {
        return Arrays.asList(ExportFormat.getAllFormats());
    }

    @Override
    public ExportFormat getDefaultFormat(String module) {
        String key = CONFIG_PREFIX + "default:" + module;
        Object formatCode = redisTemplate.opsForValue().get(key);

        if (formatCode != null) {
            try {
                return ExportFormat.getByCode(formatCode.toString());
            } catch (IllegalArgumentException e) {
                log.warn("无效的默认导出格式: {}", formatCode);
            }
        }

        // 返回系统默认格式
        return ExportFormat.EXCEL;
    }

    @Override
    public void setDefaultFormat(String module, ExportFormat format) {
        String key = CONFIG_PREFIX + "default:" + module;
        redisTemplate.opsForValue().set(key, format.getCode());
        log.info("设置模块{}的默认导出格式为: {}", module, format.getCode());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getFormatConfig(ExportFormat format) {
        String key = CONFIG_PREFIX + "format:" + format.getCode();
        Object config = redisTemplate.opsForValue().get(key);

        if (config instanceof Map) {
            return (Map<String, Object>) config;
        }

        // 返回默认配置
        return getDefaultFormatConfig(format);
    }

    @Override
    public void setFormatConfig(ExportFormat format, Map<String, Object> config) {
        String key = CONFIG_PREFIX + "format:" + format.getCode();
        redisTemplate.opsForValue().set(key, config);
        log.info("设置导出格式{}的配置: {}", format.getCode(), config);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserExportPreferences(Long userId) {
        String key = PREFERENCE_PREFIX + userId;
        Object preferences = redisTemplate.opsForValue().get(key);

        if (preferences instanceof Map) {
            return (Map<String, Object>) preferences;
        }

        // 返回默认偏好设置
        return getDefaultUserPreferences();
    }

    @Override
    public void setUserExportPreferences(Long userId, Map<String, Object> preferences) {
        String key = PREFERENCE_PREFIX + userId;
        redisTemplate.opsForValue().set(key, preferences);
        log.info("设置用户{}的导出偏好: {}", userId, preferences);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getExportTemplate(String module, ExportFormat format) {
        String key = TEMPLATE_PREFIX + module + ":" + format.getCode();
        Object template = redisTemplate.opsForValue().get(key);

        if (template instanceof Map) {
            return (Map<String, Object>) template;
        }

        // 返回默认模板
        return getDefaultExportTemplate(module, format);
    }

    @Override
    public void saveExportTemplate(String module, ExportFormat format, Map<String, Object> template) {
        String key = TEMPLATE_PREFIX + module + ":" + format.getCode();
        redisTemplate.opsForValue().set(key, template);
        log.info("保存模块{}格式{}的导出模板", module, format.getCode());
    }

    @Override
    public boolean isFormatSupported(String module, ExportFormat format) {
        // 检查模块是否支持该格式
        Map<String, Object> moduleConfig = getModuleConfig(module);
        if (moduleConfig != null) {
            @SuppressWarnings("unchecked")
            List<String> supportedFormats = (List<String>) moduleConfig.get("supportedFormats");
            if (supportedFormats != null) {
                return supportedFormats.contains(format.getCode());
            }
        }

        // 默认所有格式都支持
        return true;
    }

    @Override
    public Map<String, Object> getExportPermissionConfig(Long userId) {
        Map<String, Object> permissionConfig = new HashMap<>();

        // 基础权限配置
        permissionConfig.put("dailyLimit", 50);
        permissionConfig.put("fileSizeLimit", 50 * 1024 * 1024L); // 50MB
        permissionConfig.put("recordCountLimit", 10000);
        permissionConfig.put("rateLimitPerHour", 10);

        // 用户特殊权限（如果有的话）
        if (isPremiumUser(userId)) {
            permissionConfig.put("dailyLimit", 200);
            permissionConfig.put("fileSizeLimit", 200 * 1024 * 1024L); // 200MB
            permissionConfig.put("recordCountLimit", 50000);
            permissionConfig.put("rateLimitPerHour", 50);
        }
        return permissionConfig;
    }

    @Override
    public Map<String, Object> getExportQueueConfig() {
        Map<String, Object> queueConfig = new HashMap<>();

        // 异步导出队列配置
        queueConfig.put("queueSize", 1000);
        queueConfig.put("threadPoolSize", 10);
        queueConfig.put("maxThreadPoolSize", 20);
        queueConfig.put("queueTimeout", 300000); // 5分钟
        queueConfig.put("retryAttempts", 3);
        queueConfig.put("retryDelay", 5000); // 5秒

        return queueConfig;
    }

    @Override
    public Map<String, Object> getExportCacheConfig() {
        Map<String, Object> cacheConfig = new HashMap<>();

        // 缓存配置
        cacheConfig.put("enabled", true);
        cacheConfig.put("ttl", 1800); // 30分钟
        cacheConfig.put("maxCacheSize", 1000);
        cacheConfig.put("cacheKeyPrefix", "export:cache:");

        return cacheConfig;
    }

    // 私有辅助方法
    private Map<String, Object> getDefaultFormatConfig(ExportFormat format) {
        Map<String, Object> config = new HashMap<>();

        switch (format) {
            case EXCEL:
                config.put("maxRows", 1048576);
                config.put("maxColumns", 16384);
                config.put("autoFilter", true);
                config.put("freezeHeader", true);
                break;
            case PDF:
                config.put("pageSize", "A4");
                config.put("orientation", "portrait");
                config.put("includeHeader", true);
                config.put("includeFooter", true);
                break;
            case CSV:
                config.put("delimiter", ",");
                config.put("encoding", "UTF-8");
                config.put("includeHeader", true);
                break;
            case JSON:
                config.put("prettyPrint", false);
                config.put("includeNull", false);
                config.put("dateFormat", "yyyy-MM-dd HH:mm:ss");
                break;
            case XML:
                config.put("rootElement", "data");
                config.put("itemElement", "record");
                config.put("includeHeader", true);
                break;
        }
        return config;
    }

    private Map<String, Object> getDefaultUserPreferences() {
        Map<String, Object> preferences = new HashMap<>();

        preferences.put("defaultFormat", "excel");
        preferences.put("autoDownload", true);
        preferences.put("includeHeader", true);
        preferences.put("compressionEnabled", false);
        preferences.put("notificationEnabled", true);

        return preferences;
    }

    private Map<String, Object> getDefaultExportTemplate(String module, ExportFormat format) {
        Map<String, Object> template = new HashMap<>();

        // 模块特定的默认模板配置
        template.put("module", module);
        template.put("format", format.getCode());
        template.put("includeHeader", true);
        template.put("includeFooter", false);
        template.put("dateFormat", "yyyy-MM-dd HH:mm:ss");

        return template;
    }

    private Map<String, Object> getModuleConfig(String module) {
        // 这里可以从配置文件或数据库获取模块配置
        // 暂时返回null
        return null;
    }

    private boolean isPremiumUser(Long userId) {
        // 检查是否为高级用户
        // 这里需要根据实际业务逻辑实现
        return false; // 暂时返回false
    }
}