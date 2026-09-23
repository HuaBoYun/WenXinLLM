package com.management.accountant.oracle.service.advanced;

import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 集成监控Service接口
 * 
 * @description 集成监控业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-06
 */
public interface IntegrationMonitorService {

    /**
     * 获取集成日志（分页）
     * 
     * @param integrationId 集成ID
     * @param companyId 公司ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getIntegrationLogs(String integrationId, String companyId,
                                                       Integer pageNo, Integer pageSize);

    /**
     * 获取集成告警（分页）
     * 
     * @param status 状态
     * @param companyId 公司ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getIntegrationAlerts(String status, String companyId,
                                                         Integer pageNo, Integer pageSize);

    /**
     * 配置集成
     * 
     * @param integrationId 集成ID
     * @param config 配置信息
     * @param companyId 公司ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean configureIntegration(String integrationId, Map<String, Object> config,
                                 String companyId, String userId);

    /**
     * 获取实时监控数据
     * 
     * @param integrationId 集成ID
     * @return 实时监控数据
     */
    Map<String, Object> getRealtimeMonitorData(String integrationId);

    /**
     * 告警管理
     * 
     * @param alertId 告警ID
     * @param action 操作：resolve(解决)、ignore(忽略)
     * @param userId 用户ID
     * @param userName 用户名称
     * @return 是否成功
     */
    boolean manageAlert(String alertId, String action, String userId, String userName);

    /**
     * 性能分析
     * 
     * @param integrationId 集成ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 性能分析数据
     */
    Map<String, Object> analyzePerformance(String integrationId, String startDate, String endDate);

    /**
     * 监控设置
     * 
     * @param settings 设置信息
     * @param companyId 公司ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean updateMonitorSettings(Map<String, Object> settings, String companyId, String userId);

    /**
     * 导出监控数据
     * 
     * @param exportType 导出类型：logs(日志)、alerts(告警)、performance(性能)
     * @param params 导出参数
     * @param companyId 公司ID
     * @return 导出结果
     */
    Map<String, Object> exportMonitorData(String exportType, Map<String, Object> params,
                                          String companyId);
}

