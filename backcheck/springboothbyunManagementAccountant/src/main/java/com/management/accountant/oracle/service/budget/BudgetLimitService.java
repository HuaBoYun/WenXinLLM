package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Limit Service接口
 * 
 * @description Limit业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetLimitService {

    /**
     * create
     */
    Map<String, Object> create(Map<String, Object> params, String companyId, String userId);

    /**
     * update
     */
    Map<String, Object> update(Map<String, Object> params, String companyId, String userId);

    /**
     * enable
     */
    Map<String, Object> enable(Map<String, Object> params, String companyId, String userId);

}
