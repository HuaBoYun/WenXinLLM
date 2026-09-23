package com.global.treasurer.service;

import java.util.List;
import java.util.Map;

/**
 * 融资监控服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface FinancingMonitorService {

    Map<String, Object> getFinancingOverview(Long companyId);

    Map<String, Object> getFinancingStructure(Long companyId);

    List<Map<String, Object>> getFinancingTrend(Long companyId, String period);

    Map<String, Object> getFinancingCostAnalysis(Long companyId);

    List<Map<String, Object>> getFinancingMaturityDistribution(Long companyId);

    List<Map<String, Object>> getFinancingByBank(Long companyId);

    List<Map<String, Object>> getFinancingByType(Long companyId);

    Map<String, Object> getFinancingRiskIndicators(Long companyId);

    List<Map<String, Object>> getFinancingAlerts(Long companyId);

    Map<String, Object> getFinancingDashboard(Long companyId);
}

