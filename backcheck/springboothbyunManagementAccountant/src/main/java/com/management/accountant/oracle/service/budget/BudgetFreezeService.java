package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Freeze Service接口
 * 
 * @description Freeze业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetFreezeService {

    /**
     * create
     */
    Map<String, Object> create(Map<String, Object> params, String companyId, String userId);

    /**
     * update
     */
    Map<String, Object> update(Map<String, Object> params, String companyId, String userId);

    /**
     * freeze
     */
    Map<String, Object> freeze(Map<String, Object> params, String companyId, String userId);

    /**
     * batchUnfreeze
     */
    Map<String, Object> batchUnfreeze(Map<String, Object> params, String companyId, String userId);

}
