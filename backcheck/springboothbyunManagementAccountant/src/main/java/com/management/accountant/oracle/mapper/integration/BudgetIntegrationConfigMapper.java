package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetIntegrationConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算集成配置Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetIntegrationConfigMapper extends BaseMapper<BudgetIntegrationConfig> {

    /**
     * 根据配置类型查询配置列表
     * 
     * @param configType 配置类型
     * @return 配置列表
     */
    List<BudgetIntegrationConfig> listByConfigType(@Param("configType") String configType);

    /**
     * 根据配置分组查询配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    List<BudgetIntegrationConfig> listByConfigGroup(@Param("configGroup") String configGroup);

    /**
     * 根据配置键查询配置
     * 
     * @param configKey 配置键
     * @return 配置
     */
    BudgetIntegrationConfig getByConfigKey(@Param("configKey") String configKey);

    /**
     * 根据生效范围查询配置列表
     * 
     * @param scope 生效范围
     * @return 配置列表
     */
    List<BudgetIntegrationConfig> listByScope(@Param("scope") String scope);

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
}

