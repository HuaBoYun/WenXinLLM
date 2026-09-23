package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetIntegrationMonitor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预算集成监控Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetIntegrationMonitorService extends IService<BudgetIntegrationMonitor> {

    /**
     * 根据集成类型查询监控记录列表
     * 
     * @param integrationType 集成类型
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByIntegrationType(String integrationType);

    /**
     * 根据集成ID查询监控记录列表
     * 
     * @param integrationId 集成ID
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByIntegrationId(String integrationId);

    /**
     * 根据执行状态查询监控记录列表
     * 
     * @param executionStatus 执行状态
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByExecutionStatus(String executionStatus);

    /**
     * 根据告警级别查询监控记录列表
     * 
     * @param alertLevel 告警级别
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByAlertLevel(String alertLevel);

    /**
     * 根据时间范围查询监控记录列表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listByTimeRange(Date startTime, Date endTime);

    /**
     * 查询未告警的错误记录
     * 
     * @return 监控记录列表
     */
    List<BudgetIntegrationMonitor> listUnalertedErrors();

    /**
     * 统计集成执行情况
     * 
     * @param integrationId 集成ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> statisticsByIntegration(String integrationId, Date startTime, Date endTime);

    /**
     * 创建监控记录
     * 
     * @param integrationType 集成类型
     * @param integrationId 集成ID
     * @param integrationName 集成名称
     * @return 监控记录ID
     */
    String createMonitorRecord(String integrationType, String integrationId, String integrationName);

    /**
     * 更新监控记录执行结果
     * 
     * @param monitorId 监控记录ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @param errorMessage 错误信息
     * @return 是否更新成功
     */
    boolean updateMonitorResult(String monitorId, boolean success, Integer recordCount, String errorMessage);

    /**
     * 标记告警已处理
     * 
     * @param monitorId 监控记录ID
     * @return 是否标记成功
     */
    boolean markAlertHandled(String monitorId);

    /**
     * 批量删除监控记录
     * 
     * @param monitorIds 监控记录ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteMonitors(List<String> monitorIds);

    /**
     * 清理过期监控记录
     * 
     * @param days 保留天数
     * @return 清理记录数
     */
    int cleanExpiredMonitors(int days);
}

