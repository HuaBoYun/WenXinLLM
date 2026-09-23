package com.global.treasurer.service;

import java.util.List;
import java.util.Map;

/**
 * 担保监控服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface GuaranteeMonitorService {

    Map<String, Object> getGuaranteeOverview(Long companyId);

    Map<String, Object> getGuaranteeStructure(Long companyId);

    List<Map<String, Object>> getGuaranteeTrend(Long companyId, String period);

    List<Map<String, Object>> getGuaranteeByType(Long companyId);

    List<Map<String, Object>> getCollateralDistribution(Long companyId);

    List<Map<String, Object>> getGuaranteeExpiringContracts(Long companyId, Integer days);

    Map<String, Object> getGuaranteeRiskIndicators(Long companyId);

    List<Map<String, Object>> getGuaranteeAlerts(Long companyId);

    Map<String, Object> getGuaranteeDashboard(Long companyId);

    Map<String, Object> getCollateralValuation(Long companyId);
}

