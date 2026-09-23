package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetIntegrationConfig;

import java.util.List;

/**
 * 预算集成配置Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetIntegrationConfigService extends IService<BudgetIntegrationConfig> {

    /**
     * 根据配置类型查询配置列表
     * 
     * @param configType 配置类型
     * @return 配置列表
     */
    List<BudgetIntegrationConfig> listByConfigType(String configType);

    /**
     * 根据配置分组查询配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    List<BudgetIntegrationConfig> listByConfigGroup(String configGroup);

    /**
     * 根据配置键查询配置
     * 
     * @param configKey 配置键
     * @return 配置
     */
    BudgetIntegrationConfig getByConfigKey(String configKey);

    /**
     * 根据生效范围查询配置列表
     * 
     * @param scope 生效范围
     * @return 配置列表
     */
    List<BudgetIntegrationConfig> listByScope(String scope);

    /**
     * 查询启用的配置列表
     * 
     * @return 配置列表
     */
    List<BudgetIntegrationConfig> listEnabled();

    /**
     * 查询系统配置列表
     * 
     * @return 配置列表
     */
    List<BudgetIntegrationConfig> listSystemConfigs();

    /**
     * 更新配置值
     * 
     * @param configKey 配置键
     * @param configValue 配置值
     * @return 是否更新成功
     */
    boolean updateConfigValue(String configKey, String configValue);

    /**
     * 批量更新配置
     * 
     * @param configs 配置列表
     * @return 是否更新成功
     */
    boolean batchUpdateConfigs(List<BudgetIntegrationConfig> configs);

    /**
     * 重置配置为默认值
     * 
     * @param configId 配置ID
     * @return 是否重置成功
     */
    boolean resetToDefault(String configId);

    /**
     * 批量启用/禁用配置
     * 
     * @param configIds 配置ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> configIds, boolean enabled);
}

