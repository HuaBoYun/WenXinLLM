package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentMonitoringDTO;
import com.global.treasurer.dto.InvestmentMonitoringQueryDTO;
import com.global.treasurer.entity.TblInvestmentMonitoring;

import java.util.List;
import java.util.Map;

/**
 * 投资监控服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
public interface InvestmentMonitoringService {

    /**
     * 分页查询投资监控列表
     */
    PageInfo<TblInvestmentMonitoring> getInvestmentMonitoringList(InvestmentMonitoringQueryDTO queryDTO);

    /**
     * 根据ID获取投资监控详情
     */
    TblInvestmentMonitoring getInvestmentMonitoringById(Long monitoringId);

    /**
     * 保存投资监控（新增或更新）
     */
    TblInvestmentMonitoring saveInvestmentMonitoring(InvestmentMonitoringDTO dto);

    /**
     * 删除投资监控
     */
    void deleteInvestmentMonitoring(Long monitoringId);

    /**
     * 批量删除投资监控
     */
    void batchDeleteInvestmentMonitorings(List<Long> monitoringIds);

    /**
     * 创建投资监控记录
     */
    void createMonitoring(Long investmentId, String monitoringType);

    /**
     * 更新监控数据
     */
    void updateMonitoringData(Long monitoringId, java.math.BigDecimal currentValue);

    /**
     * 计算绩效评分
     */
    void calculatePerformanceScore(Long monitoringId);

    /**
     * 计算风险评分
     */
    void calculateRiskScore(Long monitoringId);

    /**
     * 生成预警
     */
    void generateAlert(Long monitoringId);

    /**
     * 获取仪表盘数据
     */
    Map<String, Object> getDashboardData();

    /**
     * 获取预警列表
     */
    List<TblInvestmentMonitoring> getAlertList(String alertLevel);

    /**
     * 获取统计信息
     */
    Map<String, Object> getMonitoringStatistics();

    /**
     * 获取监控分析数据
     */
    Map<String, Object> getMonitoringAnalysis();

    /**
     * 获取风险趋势分析
     */
    List<Map<String, Object>> getRiskTrend(Integer months);

    /**
     * 生成监控报告
     */
    Map<String, Object> generateMonitoringReport(String reportType, String startDate, String endDate);

    /**
     * 获取优化建议
     */
    List<Map<String, Object>> getOptimizationRecommendations();

    /**
     * 异常检测
     */
    List<Map<String, Object>> detectAnomalies(String investmentType);
}
