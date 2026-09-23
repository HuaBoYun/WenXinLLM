package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingMonitoringDTO;
import com.global.treasurer.dto.FinancingMonitoringQueryDTO;
import com.global.treasurer.entity.TblFinancingMonitoring;

import java.util.List;
import java.util.Map;

/**
 * 融资监控服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
public interface FinancingMonitoringService {

    /**
     * 分页查询融资监控列表
     */
    PageInfo<TblFinancingMonitoring> getFinancingMonitoringList(FinancingMonitoringQueryDTO queryDTO);

    /**
     * 获取监控概览
     */
    Map<String, Object> getMonitoringOverview();

    /**
     * 更新监控数据
     */
    void updateMonitoringData(FinancingMonitoringDTO dto);

    /**
     * 处理风险预警
     */
    void processAlert(Long monitoringId, String handleOpinion, Long handlerId, String handlerName);

    /**
     * 导出监控报告
     */
    List<Map<String, Object>> exportMonitoringReport(FinancingMonitoringQueryDTO queryDTO);

    /**
     * 获取仪表盘数据
     */
    Map<String, Object> getDashboardData();

    /**
     * 获取趋势分析数据
     */
    Map<String, Object> getTrendsAnalysis(String period);

    /**
     * 刷新监控数据
     */
    void refreshMonitoringData();

    /**
     * 根据ID获取融资监控详情
     */
    TblFinancingMonitoring getFinancingMonitoringById(Long monitoringId);

    /**
     * 获取预警列表
     */
    List<TblFinancingMonitoring> getAlertList(String alertLevel);

    /**
     * 获取统计信息
     */
    Map<String, Object> getMonitoringStatistics();

    /**
     * 获取融资类型分布图表数据
     */
    List<Map<String, Object>> getFinancingTypeDistribution();

    /**
     * 获取风险等级分布图表数据
     */
    List<Map<String, Object>> getRiskLevelDistribution();

    /**
     * 获取合规状态分布图表数据
     */
    List<Map<String, Object>> getComplianceDistribution();

    /**
     * 获取融资规模趋势数据（按月统计）
     * @param months 查询月数
     * @return 趋势数据列表
     */
    List<Map<String, Object>> getFinancingScaleTrend(Integer months);

    /**
     * 获取融资结构分析数据（按融资类型统计）
     * @return 结构分析数据列表
     */
    List<Map<String, Object>> getFinancingStructureAnalysis();
}
