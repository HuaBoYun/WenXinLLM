package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetBiIntegration;

import java.util.List;

/**
 * 预算BI系统集成Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetBiIntegrationService extends IService<BudgetBiIntegration> {

    /**
     * 根据BI系统类型查询集成列表
     * 
     * @param biType BI系统类型
     * @return 集成列表
     */
    List<BudgetBiIntegration> listByBiType(String biType);

    /**
     * 根据连接方式查询集成列表
     * 
     * @param connectionType 连接方式
     * @return 集成列表
     */
    List<BudgetBiIntegration> listByConnectionType(String connectionType);

    /**
     * 根据同步模式查询集成列表
     * 
     * @param syncMode 同步模式
     * @return 集成列表
     */
    List<BudgetBiIntegration> listBySyncMode(String syncMode);

    /**
     * 查询启用的BI集成列表
     * 
     * @return 集成列表
     */
    List<BudgetBiIntegration> listEnabled();

    /**
     * 执行BI数据同步
     * 
     * @param biId BI集成ID
     * @return 同步结果
     */
    boolean executeSync(String biId);

    /**
     * 刷新BI报表
     * 
     * @param biId BI集成ID
     * @param reportId 报表ID
     * @return 刷新结果
     */
    boolean refreshReport(String biId, String reportId);

    /**
     * 测试BI连接
     * 
     * @param biId BI集成ID
     * @return 测试结果
     */
    boolean testConnection(String biId);

    /**
     * 更新同步统计
     * 
     * @param biId BI集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @return 是否更新成功
     */
    boolean updateSyncStatistics(String biId, boolean success, Integer recordCount);

    /**
     * 批量启用/禁用BI集成
     * 
     * @param biIds BI集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> biIds, boolean enabled);
}

