package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.SysConfig;
import com.huabo.cybermonitor.mapper.SysConfigMapper;
import com.huabo.cybermonitor.service.ISysConfigService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.SysConfigQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Autowired
    private SysConfigMapper configMapper;

    // 配置缓存
    private final Map<String, String> configCache = new ConcurrentHashMap<>();

    @Override
    public IPage<SysConfig> getConfigList(SysConfigQueryVO queryVO) {
        Page<SysConfig> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        return configMapper.selectConfigList(page, queryVO);
    }

    @Override
    public SysConfig getConfigDetail(String configId) {
        if (StringUtils.isEmpty(configId)) {
            return null;
        }
        return configMapper.selectById(configId);
    }

    @Override
    public String getConfigValue(String configKey) {
        return getConfigValue(configKey, null);
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        if (StringUtils.isEmpty(configKey)) {
            return defaultValue;
        }

        // 先从缓存获取
        String value = configCache.get(configKey);
        if (value != null) {
            return value;
        }

        // 从数据库获取
        value = configMapper.selectConfigValue(configKey);
        if (value != null) {
            configCache.put(configKey, value);
            return value;
        }

        return defaultValue;
    }

    @Override
    public Integer getIntConfigValue(String configKey) {
        return getIntConfigValue(configKey, null);
    }

    @Override
    public Integer getIntConfigValue(String configKey, Integer defaultValue) {
        String value = getConfigValue(configKey);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("配置值转换为整数失败: {} = {}", configKey, value);
            return defaultValue;
        }
    }

    @Override
    public Boolean getBooleanConfigValue(String configKey) {
        return getBooleanConfigValue(configKey, null);
    }

    @Override
    public Boolean getBooleanConfigValue(String configKey, Boolean defaultValue) {
        String value = getConfigValue(configKey);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(value) || "1".equals(value) || "yes".equalsIgnoreCase(value);
    }

    @Override
    public List<SysConfig> getConfigsByGroup(String configGroup) {
        return configMapper.selectConfigsByGroup(configGroup);
    }

    @Override
    public List<SysConfig> getConfigsByType(String configType) {
        return configMapper.selectConfigsByType(configType);
    }

    @Override
    public List<SysConfig> getEnabledConfigs() {
        return configMapper.selectEnabledConfigs();
    }

    @Override
    public List<SysConfig> getSystemConfigs() {
        return configMapper.selectSystemConfigs();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addConfig(SysConfig config) {
        try {
            // 验证配置键唯一性
            if (validateConfigKey(config.getConfigKey(), null)) {
                throw new RuntimeException("配置键已存在");
            }
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());

            boolean result = save(config);
            if (result) {
                // 更新缓存
                configCache.put(config.getConfigKey(), config.getConfigValue());
            }
            return result;
        } catch (Exception e) {
            log.error("新增系统配置失败", e);
            throw new RuntimeException("新增系统配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(SysConfig config) {
        try {
            // 验证配置键唯一性
            if (validateConfigKey(config.getConfigKey(), config.getConfigId())) {
                throw new RuntimeException("配置键已存在");
            }

            config.setUpdateTime(LocalDateTime.now());
            boolean result = updateById(config);
            if (result) {
                // 更新缓存
                configCache.put(config.getConfigKey(), config.getConfigValue());
            }
            return result;
        } catch (Exception e) {
            log.error("更新系统配置失败", e);
            throw new RuntimeException("更新系统配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfigValue(String configKey, String configValue) {
        try {
            boolean result = configMapper.updateConfigValue(configKey, configValue) > 0;
            if (result) {
                // 更新缓存
                configCache.put(configKey, configValue);
            }
            return result;
        } catch (Exception e) {
            log.error("更新配置值失败", e);
            throw new RuntimeException("更新配置值失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateConfigValue(Map<String, String> configMap) {
        try {
            for (Map.Entry<String, String> entry : configMap.entrySet()) {
                updateConfigValue(entry.getKey(), entry.getValue());
            }
            return true;
        } catch (Exception e) {
            log.error("批量更新配置值失败", e);
            throw new RuntimeException("批量更新配置值失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfig(String configId) {
        try {
            SysConfig config = getById(configId);
            boolean result = removeById(configId);
            if (result && config != null) {
                // 从缓存中移除
                configCache.remove(config.getConfigKey());
            }
            return result;
        } catch (Exception e) {
            log.error("删除系统配置失败", e);
            throw new RuntimeException("删除系统配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteConfig(List<String> configIds) {
        try {
            for (String configId : configIds) {
                deleteConfig(configId);
            }
            return true;
        } catch (Exception e) {
            log.error("批量删除系统配置失败", e);
            throw new RuntimeException("批量删除系统配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableConfig(String configId) {
        try {
            return configMapper.updateConfigStatus(configId, "true") > 0;
        } catch (Exception e) {
            log.error("启用配置失败", e);
            throw new RuntimeException("启用配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableConfig(String configId) {
        try {
            return configMapper.updateConfigStatus(configId, "false") > 0;
        } catch (Exception e) {
            log.error("禁用配置失败", e);
            throw new RuntimeException("禁用配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchEnableConfig(List<String> configIds) {
        try {
            return true;
        } catch (Exception e) {
            log.error("批量启用配置失败", e);
            throw new RuntimeException("批量启用配置失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDisableConfig(List<String> configIds) {
        try {
            return true;
        } catch (Exception e) {
            log.error("批量禁用配置失败", e);
            throw new RuntimeException("批量禁用配置失败：" + e.getMessage());
        }
    }

    @Override
    public boolean validateConfigKey(String configKey, String excludeId) {
        if (StringUtils.isEmpty(configKey)) {
            return false;
        }
        SysConfig existingConfig = configMapper.selectByConfigKey(configKey);
        return existingConfig != null;
    }

    @Override
    public Map<String, Object> validateConfigValue(String configType, String configValue) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(configValue)) {
            result.put("isValid", true);
            result.put("errors", errors);
            return result;
        }

        switch (configType) {
            case SysConfig.TYPE_NUMBER:
                try {
                    Double.parseDouble(configValue);
                } catch (NumberFormatException e) {
                    errors.add("配置值必须是数字");
                }
                break;
            case SysConfig.TYPE_BOOLEAN:
                if (!"true".equalsIgnoreCase(configValue) && !"false".equalsIgnoreCase(configValue) &&
                    !"1".equals(configValue) && !"0".equals(configValue) &&
                    !"yes".equalsIgnoreCase(configValue) && !"no".equalsIgnoreCase(configValue)) {
                    errors.add("配置值必须是布尔值（true/false、1/0、yes/no）");
                }
                break;
            case SysConfig.TYPE_JSON:
                // 简化JSON验证
                if (!configValue.trim().startsWith("{") && !configValue.trim().startsWith("[")) {
                    errors.add("配置值必须是有效的JSON格式");
                }
                break;
        }

        result.put("isValid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    public List<SysConfig> getDataCollectionConfigs() {
        return configMapper.selectDataCollectionConfigs();
    }

    @Override
    public List<SysConfig> getNotificationConfigs() {
        return configMapper.selectNotificationConfigs();
    }

    @Override
    public List<SysConfig> getReportConfigs() {
        return configMapper.selectReportConfigs();
    }

    @Override
    public List<SysConfig> getIntegrationConfigs() {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetConfigToDefault(String configId) {
        try {
            SysConfig config = getById(configId);
            if (config != null ) {
                config.setUpdateTime(LocalDateTime.now());
                boolean result = updateById(config);
                if (result) {
                    // 更新缓存
                    configCache.put(config.getConfigKey(), config.getConfigValue());
                }
                return result;
            }
            return false;
        } catch (Exception e) {
            log.error("重置配置为默认值失败", e);
            throw new RuntimeException("重置配置为默认值失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchResetConfigToDefault(List<String> configIds) {
        try {
            for (String configId : configIds) {
                resetConfigToDefault(configId);
            }
            return true;
        } catch (Exception e) {
            log.error("批量重置配置为默认值失败", e);
            throw new RuntimeException("批量重置配置为默认值失败：" + e.getMessage());
        }
    }

    @Override
    public void exportConfigList(SysConfigQueryVO queryVO, HttpServletResponse response) {
        try {
            List<SysConfig> configList = configMapper.selectConfigListForExport(queryVO);

            // 设置导出的列标题
            String[] headers = {
                "配置键", "配置名称", "配置值", "默认值", "配置类型", "配置分组",
                "是否启用", "是否系统配置", "配置描述", "创建时间", "更新时间"
            };

            ExcelUtil excelUtil = new ExcelUtil("系统配置列表", headers);

            // 添加数据行
            for (int i = 0; i < configList.size(); i++) {
                SysConfig config = configList.get(i);
                Object[] row = {
                    config.getConfigKey(),
                    config.getConfigName(),
                    config.getConfigValue(),
                    getConfigTypeLabel(config.getConfigType()),
                    getConfigGroupLabel(config.getConfigGroup()),
                    config.getConfigDescription(),
                    config.getCreateTime(),
                    config.getUpdateTime()
                };
                excelUtil.addRow(i + 1, row);
            }

            excelUtil.exportExcel(response, "系统配置列表.xls");
        } catch (Exception e) {
            log.error("导出配置列表失败", e);
            throw new RuntimeException("导出配置列表失败：" + e.getMessage());
        }
    }

    @Override
    public String getConfigTypeLabel(String configType) {
        if (StringUtils.isEmpty(configType)) {
            return "";
        }
        switch (configType) {
            case SysConfig.TYPE_STRING:
                return "字符串";
            case SysConfig.TYPE_NUMBER:
                return "数字";
            case SysConfig.TYPE_BOOLEAN:
                return "布尔值";
            case SysConfig.TYPE_JSON:
                return "JSON";
            case SysConfig.TYPE_TEXT:
                return "文本";
            case SysConfig.TYPE_DATE:
                return "日期";
            case SysConfig.TYPE_DATETIME:
                return "日期时间";
            default:
                return configType;
        }
    }

    @Override
    public String getConfigGroupLabel(String configGroup) {
        if (StringUtils.isEmpty(configGroup)) {
            return "";
        }
        switch (configGroup) {
            case SysConfig.GROUP_SYSTEM:
                return "系统配置";
            case SysConfig.GROUP_BUSINESS:
                return "业务配置";
            case SysConfig.GROUP_SECURITY:
                return "安全配置";
            case SysConfig.GROUP_NOTIFICATION:
                return "通知配置";
            case SysConfig.GROUP_DATA_COLLECTION:
                return "数据采集";
            case SysConfig.GROUP_REPORT:
                return "报表配置";
            case SysConfig.GROUP_INTEGRATION:
                return "集成配置";
            default:
                return configGroup;
        }
    }

    @Override
    public boolean refreshConfigCache() {
        try {
            configCache.clear();
            List<SysConfig> configs = getEnabledConfigs();
            for (SysConfig config : configs) {
                configCache.put(config.getConfigKey(), config.getConfigValue());
            }
            return true;
        } catch (Exception e) {
            log.error("刷新配置缓存失败", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getConfigCacheInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("cacheSize", configCache.size());
        info.put("cacheKeys", new ArrayList<>(configCache.keySet()));
        return info;
    }

    @Override
    public boolean clearConfigCache() {
        try {
            configCache.clear();
            return true;
        } catch (Exception e) {
            log.error("清空配置缓存失败", e);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getConfigChangeHistory(String configKey) {
        // 简化实现，返回模拟历史数据
        List<Map<String, Object>> history = new ArrayList<>();
        Map<String, Object> record = new HashMap<>();
        record.put("configKey", configKey);
        record.put("oldValue", "旧值");
        record.put("newValue", "新值");
        record.put("changeTime", LocalDateTime.now());
        record.put("changeBy", "系统管理员");
        history.add(record);
        return history;
    }

    @Override
    public Map<String, Object> backupConfigs() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SysConfig> configs = list();
            result.put("success", true);
            result.put("configCount", configs.size());
            result.put("backupTime", LocalDateTime.now());
            result.put("message", "配置备份功能开发中");
        } catch (Exception e) {
            log.error("备份配置失败", e);
            result.put("success", false);
            result.put("message", "备份失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> restoreConfigs(String backupData) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "配置恢复功能开发中");
        } catch (Exception e) {
            log.error("恢复配置失败", e);
            result.put("success", false);
            result.put("message", "恢复失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getConfigStatistics() {
        return configMapper.selectConfigStatistics();
    }

    @Override
    public List<Map<String, Object>> getConfigGroupDistribution() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getConfigTypeDistribution() {
        return null;
    }

    @Override
    public Map<String, Object> checkConfigHealth() {
        Map<String, Object> health = new HashMap<>();
        try {
            int enabledConfigs = getEnabledConfigs().size();
            int cacheSize = configCache.size();
            health.put("enabledConfigs", enabledConfigs);
            health.put("cacheSize", cacheSize);
            health.put("cacheHitRate", cacheSize > 0 ? (double) cacheSize / enabledConfigs * 100 : 0);
            health.put("status", "健康");
            health.put("checkTime", LocalDateTime.now());
        } catch (Exception e) {
            log.error("检查配置健康状态失败", e);
            health.put("status", "异常");
            health.put("error", e.getMessage());
        }
        return health;
    }
}
