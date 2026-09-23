package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Indicator Service接口
 * 
 * @description Indicator业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetIndicatorService {

    /**
     * saveIndicator
     */
    Map<String, Object> saveIndicator(Map<String, Object> params, String companyId, String userId);

    /**
     * deleteIndicator
     */
    Map<String, Object> deleteIndicator(Map<String, Object> params, String companyId, String userId);

    /**
     * getIndicatorTree
     */
    Map<String, Object> getIndicatorTree(String companyId, String userId);

    /**
     * getIndicatorList
     */
    Map<String, Object> getIndicatorList(String companyId, String userId);

    /**
     * getIndicatorDetail
     */
    Map<String, Object> getIndicatorDetail(String companyId, String userId);

}
