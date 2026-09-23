package com.huabo.fxgl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.entity.TblWarningConfig;
import com.huabo.fxgl.mapper.TblWarningConfigMapper;
import com.huabo.fxgl.service.IWarningConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * 预警配置服务实现类
 *
 * @author 华博云
 * @date 2025-09-30
 */
@Slf4j
@Service
public class WarningConfigServiceImpl extends ServiceImpl<TblWarningConfigMapper, TblWarningConfig> 
        implements IWarningConfigService {

    @Autowired
    private TblWarningConfigMapper warningConfigMapper;

    @Override
    public JsonBean getWarningConfigList(Integer pageNum, Integer pageSize, String configType, String isEnabled) {
        try {
            Page<TblWarningConfig> page = new Page<>(pageNum, pageSize);
            IPage<TblWarningConfig> result = warningConfigMapper.selectConfigPage(page, configType, isEnabled);
            
            return new JsonBean(1, "查询成功", result);
        } catch (Exception e) {
            log.error("查询预警配置列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveWarningConfig(TblWarningConfig warningConfig, String currentUser) {
        try {
            // 验证必要字段
            if (StringUtil.isEmpty(warningConfig.getConfigType())) {
                return new JsonBean(0, "配置类型不能为空", null);
            }
            if (StringUtil.isEmpty(warningConfig.getConfigName())) {
                return new JsonBean(0, "配置名称不能为空", null);
            }
            if (StringUtil.isEmpty(warningConfig.getConfigValue())) {
                return new JsonBean(0, "配置值不能为空", null);
            }

            // 验证配置名称唯一性
            QueryWrapper<TblWarningConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("CONFIG_NAME", warningConfig.getConfigName());
            if (warningConfigMapper.selectCount(queryWrapper) > 0) {
                return new JsonBean(0, "配置名称已存在", null);
            }

            // 验证配置值格式
            JsonBean validateResult = validateConfigValue(warningConfig.getConfigType(), warningConfig.getConfigValue());
            if (validateResult.getCode() != 1) {
                return validateResult;
            }

            // 设置默认值
            if (StringUtil.isEmpty(warningConfig.getIsEnabled())) {
                warningConfig.setIsEnabled(TblWarningConfig.EnableStatus.ENABLED);
            }
            if (warningConfig.getSortOrder() == null) {
                warningConfig.setSortOrder(0);
            }

            warningConfig.setCreateUser(currentUser);
            warningConfig.setCreateTime(new Date());

            warningConfigMapper.insert(warningConfig);
            
            log.info("保存预警配置成功，configId: {}, configName: {}", 
                    warningConfig.getConfigId(), warningConfig.getConfigName());
            
            return new JsonBean(1, "保存成功", warningConfig);
        } catch (Exception e) {
            log.error("保存预警配置失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean updateWarningConfig(TblWarningConfig warningConfig, String currentUser) {
        try {
            // 验证配置是否存在
            TblWarningConfig existConfig = warningConfigMapper.selectById(warningConfig.getConfigId());
            if (existConfig == null) {
                return new JsonBean(0, "配置不存在", null);
            }

            // 验证配置值格式
            if (StringUtil.isNotEmpty(warningConfig.getConfigValue())) {
                JsonBean validateResult = validateConfigValue(warningConfig.getConfigType(), warningConfig.getConfigValue());
                if (validateResult.getCode() != 1) {
                    return validateResult;
                }
            }

            warningConfig.setUpdateUser(currentUser);
            warningConfig.setUpdateTime(new Date());

            warningConfigMapper.updateById(warningConfig);
            
            log.info("更新预警配置成功，configId: {}", warningConfig.getConfigId());
            
            return new JsonBean(1, "更新成功", warningConfig);
        } catch (Exception e) {
            log.error("更新预警配置失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean deleteWarningConfig(String configId) {
        try {
            // 验证配置是否存在
            TblWarningConfig existConfig = warningConfigMapper.selectById(configId);
            if (existConfig == null) {
                return new JsonBean(0, "配置不存在", null);
            }

            warningConfigMapper.deleteById(configId);
            
            log.info("删除预警配置成功，configId: {}", configId);
            
            return new JsonBean(1, "删除成功", null);
        } catch (Exception e) {
            log.error("删除预警配置失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean updateConfigStatus(String configId, String isEnabled, String currentUser) {
        try {
            // 验证配置是否存在
            TblWarningConfig existConfig = warningConfigMapper.selectById(configId);
            if (existConfig == null) {
                return new JsonBean(0, "配置不存在", null);
            }

            int result = warningConfigMapper.updateConfigStatus(configId, isEnabled, currentUser);
            if (result > 0) {
                log.info("更新配置状态成功，configId: {}, isEnabled: {}", configId, isEnabled);
                return new JsonBean(1, "状态更新成功", null);
            } else {
                return new JsonBean(0, "状态更新失败", null);
            }
        } catch (Exception e) {
            log.error("更新配置状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean batchUpdateConfigStatus(List<String> configIds, String isEnabled, String currentUser) {
        try {
            if (configIds == null || configIds.isEmpty()) {
                return new JsonBean(0, "配置ID列表不能为空", null);
            }

            int successCount = 0;
            for (String configId : configIds) {
                try {
                    int result = warningConfigMapper.updateConfigStatus(configId, isEnabled, currentUser);
                    if (result > 0) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.warn("批量更新配置状态时，configId: {} 更新失败", configId, e);
                }
            }

            log.info("批量更新配置状态完成，总数: {}, 成功: {}", configIds.size(), successCount);
            
            return new JsonBean(1, "批量更新完成，成功: " + successCount + "/" + configIds.size(), null);
        } catch (Exception e) {
            log.error("批量更新配置状态失败", e);
            return new JsonBean(0, "批量更新失败: " + e.getMessage(), null);
        }
    }

    @Override
    public List<TblWarningConfig> getConfigByType(String configType) {
        try {
            return warningConfigMapper.selectByConfigType(configType);
        } catch (Exception e) {
            log.error("根据类型获取配置失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public TblWarningConfig getConfigByName(String configName) {
        try {
            return warningConfigMapper.selectByConfigName(configName);
        } catch (Exception e) {
            log.error("根据名称获取配置失败", e);
            return null;
        }
    }

    @Override
    public List<TblWarningConfig> getAllEnabledConfigs() {
        try {
            return warningConfigMapper.selectAllEnabled();
        } catch (Exception e) {
            log.error("获取所有启用配置失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public JsonBean validateConfigValue(String configType, String configValue) {
        try {
            if (StringUtil.isEmpty(configType) || StringUtil.isEmpty(configValue)) {
                return new JsonBean(0, "配置类型和配置值不能为空", null);
            }

            // 验证JSON格式
            try {
                JSONObject.parseObject(configValue);
            } catch (Exception e) {
                return new JsonBean(0, "配置值必须是有效的JSON格式", null);
            }

            // 根据配置类型进行特定验证
            switch (configType) {
                case TblWarningConfig.ConfigType.NOTIFICATION:
                    return validateNotificationConfig(configValue);
                case TblWarningConfig.ConfigType.SCHEDULE:
                    return validateScheduleConfig(configValue);
                case TblWarningConfig.ConfigType.THRESHOLD:
                    return validateThresholdConfig(configValue);
                default:
                    return new JsonBean(0, "不支持的配置类型: " + configType, null);
            }
        } catch (Exception e) {
            log.error("验证配置值失败", e);
            return new JsonBean(0, "验证失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getConfigTemplate(String configType) {
        try {
            String template;
            switch (configType) {
                case TblWarningConfig.ConfigType.NOTIFICATION:
                    template = "{\"enabled\": true, \"smtpHost\": \"smtp.company.com\", \"smtpPort\": 587, \"username\": \"dev@example.com\", \"recipients\": [\"dev@example.com\"]}";
                    break;
                case TblWarningConfig.ConfigType.SCHEDULE:
                    template = "{\"frequency\": \"*/5 * * * *\", \"enabled\": true, \"maxConcurrency\": 5}";
                    break;
                case TblWarningConfig.ConfigType.THRESHOLD:
                    template = "{\"engineering\": 4000000, \"material\": 2000000, \"service\": 1000000, \"accountAge\": 365, \"urgentRatio\": 10}";
                    break;
                default:
                    return new JsonBean(0, "不支持的配置类型: " + configType, null);
            }
            
            return new JsonBean(1, "获取模板成功", template);
        } catch (Exception e) {
            log.error("获取配置模板失败", e);
            return new JsonBean(0, "获取模板失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean exportConfigs(String configType) {
        try {
            List<TblWarningConfig> configs;
            if (StringUtil.isNotEmpty(configType)) {
                configs = warningConfigMapper.selectByConfigType(configType);
            } else {
                configs = warningConfigMapper.selectList(null);
            }
            
            return new JsonBean(1, "导出成功", configs);
        } catch (Exception e) {
            log.error("导出配置失败", e);
            return new JsonBean(0, "导出失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean importConfigs(String configData, String currentUser) {
        try {
            List<TblWarningConfig> configs = JSON.parseArray(configData, TblWarningConfig.class);
            if (configs == null || configs.isEmpty()) {
                return new JsonBean(0, "导入数据为空", null);
            }

            int successCount = 0;
            for (TblWarningConfig config : configs) {
                try {
                    config.setCreateUser(currentUser);
                    config.setCreateTime(new Date());
                    warningConfigMapper.insert(config);
                    successCount++;
                } catch (Exception e) {
                    log.warn("导入配置时，configName: {} 导入失败", config.getConfigName(), e);
                }
            }

            log.info("导入配置完成，总数: {}, 成功: {}", configs.size(), successCount);
            
            return new JsonBean(1, "导入完成，成功: " + successCount + "/" + configs.size(), null);
        } catch (Exception e) {
            log.error("导入配置失败", e);
            return new JsonBean(0, "导入失败: " + e.getMessage(), null);
        }
    }

    // 私有验证方法
    private JsonBean validateNotificationConfig(String configValue) {
        try {
            JSONObject config = JSONObject.parseObject(configValue);
            if (!config.containsKey("smtpHost") || !config.containsKey("username")) {
                return new JsonBean(0, "通知配置缺少必要字段：smtpHost, username", null);
            }
            return new JsonBean(1, "验证通过", null);
        } catch (Exception e) {
            return new JsonBean(0, "通知配置格式错误", null);
        }
    }

    private JsonBean validateScheduleConfig(String configValue) {
        try {
            JSONObject config = JSONObject.parseObject(configValue);
            if (!config.containsKey("frequency")) {
                return new JsonBean(0, "调度配置缺少必要字段：frequency", null);
            }
            return new JsonBean(1, "验证通过", null);
        } catch (Exception e) {
            return new JsonBean(0, "调度配置格式错误", null);
        }
    }

    private JsonBean validateThresholdConfig(String configValue) {
        try {
            JSONObject config = JSONObject.parseObject(configValue);
            if (config.isEmpty()) {
                return new JsonBean(0, "阈值配置不能为空", null);
            }
            return new JsonBean(1, "验证通过", null);
        } catch (Exception e) {
            return new JsonBean(0, "阈值配置格式错误", null);
        }
    }
}
