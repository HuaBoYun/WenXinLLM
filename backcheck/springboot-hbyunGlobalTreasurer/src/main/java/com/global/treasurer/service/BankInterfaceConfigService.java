package com.global.treasurer.service;

import com.global.treasurer.entity.TblBankInterfaceConfig;

import java.util.List;
import java.util.Map;

/**
 * 银企联配置服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
public interface BankInterfaceConfigService {

    /**
     * 分页查询银企联配置
     *
     * @param params 查询参数
     * @return 分页结果
     */
    Map<String, Object> getBankConfigPage(Map<String, Object> params);

    /**
     * 根据ID查询银企联配置
     *
     * @param configId 配置ID
     * @return 银企联配置
     */
    TblBankInterfaceConfig getBankConfigById(Long configId);

    /**
     * 创建银企联配置
     *
     * @param config 银企联配置
     * @return 影响行数
     */
    int createBankConfig(TblBankInterfaceConfig config);

    /**
     * 更新银企联配置
     *
     * @param config 银企联配置
     * @return 影响行数
     */
    int updateBankConfig(TblBankInterfaceConfig config);

    /**
     * 删除银企联配置
     *
     * @param configIds 配置ID列表
     * @return 影响行数
     */
    int deleteBankConfig(List<Long> configIds);

    /**
     * 启用配置
     *
     * @param configIds 配置ID列表
     * @return 影响行数
     */
    int enableBankConfig(List<Long> configIds);

    /**
     * 禁用配置
     *
     * @param configIds 配置ID列表
     * @return 影响行数
     */
    int disableBankConfig(List<Long> configIds);

    /**
     * 测试连接
     *
     * @param configId 配置ID
     * @return 测试结果
     */
    Map<String, Object> testConnection(Long configId);

    /**
     * 健康检查
     *
     * @param configId 配置ID
     * @return 健康检查结果
     */
    Map<String, Object> healthCheck(Long configId);

    /**
     * 查询可用配置
     *
     * @param params 查询参数
     * @return 配置列表
     */
    List<TblBankInterfaceConfig> getAvailableConfigs(Map<String, Object> params);

    /**
     * 统计配置概要
     *
     * @param orgId 组织ID
     * @return 统计数据
     */
    Map<String, Object> getBankConfigSummary(Long orgId);

    /**
     * 批量测试连接
     *
     * @param configIds 配置ID列表
     * @return 测试结果
     */
    Map<String, Object> batchTestConnection(List<Long> configIds);

    /**
     * 导出配置数据
     *
     * @param params 查询参数
     * @return 配置列表
     */
    List<TblBankInterfaceConfig> exportConfigs(Map<String, Object> params);
}