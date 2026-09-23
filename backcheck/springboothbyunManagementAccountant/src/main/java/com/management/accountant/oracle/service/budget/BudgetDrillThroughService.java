package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * DrillThrough Service接口
 * 
 * @description DrillThrough业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetDrillThroughService {

    /**
     * createDrillThroughQuery
     */
    Map<String, Object> createDrillThroughQuery(Map<String, Object> params, String companyId, String userId);

    /**
     * executeDrillThroughQuery
     */
    Map<String, Object> executeDrillThroughQuery(Map<String, Object> params, String companyId, String userId);

}
