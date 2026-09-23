package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetCloudIntegration;

import java.util.List;

/**
 * 预算云平台集成Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetCloudIntegrationService extends IService<BudgetCloudIntegration> {

    /**
     * 根据云平台类型查询集成列表
     * 
     * @param cloudType 云平台类型
     * @return 集成列表
     */
    List<BudgetCloudIntegration> listByCloudType(String cloudType);

    /**
     * 根据服务类型查询集成列表
     * 
     * @param serviceType 服务类型
     * @return 集成列表
     */
    List<BudgetCloudIntegration> listByServiceType(String serviceType);

    /**
     * 根据同步模式查询集成列表
     * 
     * @param syncMode 同步模式
     * @return 集成列表
     */
    List<BudgetCloudIntegration> listBySyncMode(String syncMode);

    /**
     * 查询启用的云平台集成列表
     * 
     * @return 集成列表
     */
    List<BudgetCloudIntegration> listEnabled();

    /**
     * 执行云平台数据同步
     * 
     * @param cloudId 云平台集成ID
     * @return 同步结果
     */
    boolean executeSync(String cloudId);

    /**
     * 上传文件到云平台
     * 
     * @param cloudId 云平台集成ID
     * @param filePath 文件路径
     * @return 上传结果
     */
    boolean uploadFile(String cloudId, String filePath);

    /**
     * 从云平台下载文件
     * 
     * @param cloudId 云平台集成ID
     * @param remoteFilePath 远程文件路径
     * @return 本地文件路径
     */
    String downloadFile(String cloudId, String remoteFilePath);

    /**
     * 测试云平台连接
     * 
     * @param cloudId 云平台集成ID
     * @return 测试结果
     */
    boolean testConnection(String cloudId);

    /**
     * 更新同步统计
     * 
     * @param cloudId 云平台集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @param dataTransferMb 数据传输量(MB)
     * @return 是否更新成功
     */
    boolean updateSyncStatistics(String cloudId, boolean success, Integer recordCount, Long dataTransferMb);

    /**
     * 批量启用/禁用云平台集成
     * 
     * @param cloudIds 云平台集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> cloudIds, boolean enabled);
}

