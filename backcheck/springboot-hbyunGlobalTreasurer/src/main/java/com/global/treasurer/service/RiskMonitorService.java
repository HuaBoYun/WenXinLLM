package com.global.treasurer.service;

import java.util.List;
import java.util.Map;

/**
 * 风险监控服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface RiskMonitorService {

    Map<String, Object> getRiskOverview(Long companyId);

    Map<String, Object> getLiquidityRisk(Long companyId);

    Map<String, Object> getInterestRateRisk(Long companyId);

    Map<String, Object> getExchangeRateRisk(Long companyId);

    Map<String, Object> getCreditRisk(Long companyId);

    Map<String, Object> getConcentrationRisk(Long companyId);

    List<Map<String, Object>> getRiskTrend(Long companyId, String period);

    List<Map<String, Object>> getRiskAlerts(Long companyId);

    Map<String, Object> getRiskDashboard(Long companyId);

    Map<String, Object> getRiskScorecard(Long companyId);

    List<Map<String, Object>> getRiskMitigationSuggestions(Long companyId);

    Map<String, Object> getStressTestResults(Long companyId, Map<String, Object> scenarios);
}

