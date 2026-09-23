package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Trend Service接口
 * 
 * @description Trend业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetTrendService {

    /**
     * executeBudgetVariance
     */
    Map<String, Object> executeBudgetVariance(Map<String, Object> params, String companyId, String userId);

    /**
     * updateBudgetTrend
     */
    Map<String, Object> updateBudgetTrend(Map<String, Object> params, String companyId, String userId);

}
