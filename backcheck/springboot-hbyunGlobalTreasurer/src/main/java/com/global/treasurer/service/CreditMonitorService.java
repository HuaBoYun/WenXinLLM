package com.global.treasurer.service;

import java.util.List;
import java.util.Map;

/**
 * 授信监控服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface CreditMonitorService {

    Map<String, Object> getCreditOverview(Long companyId);

    Map<String, Object> getCreditUtilization(Long companyId);

    List<Map<String, Object>> getCreditTrend(Long companyId, String period);

    List<Map<String, Object>> getCreditByBank(Long companyId);

    List<Map<String, Object>> getCreditByType(Long companyId);

    List<Map<String, Object>> getCreditExpiringContracts(Long companyId, Integer days);

    Map<String, Object> getCreditRiskIndicators(Long companyId);

    List<Map<String, Object>> getCreditAlerts(Long companyId);

    Map<String, Object> getCreditDashboard(Long companyId);

    List<Map<String, Object>> getCreditLimitDistribution(Long companyId);
}

