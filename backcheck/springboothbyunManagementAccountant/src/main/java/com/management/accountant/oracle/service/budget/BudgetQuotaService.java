package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Quota Service接口
 * 
 * @description Quota业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetQuotaService {

    /**
     * create
     */
    Map<String, Object> create(Map<String, Object> params, String companyId, String userId);

    /**
     * update
     */
    Map<String, Object> update(Map<String, Object> params, String companyId, String userId);

    /**
     * allocate
     */
    Map<String, Object> allocate(Map<String, Object> params, String companyId, String userId);

    /**
     * adjust
     */
    Map<String, Object> adjust(Map<String, Object> params, String companyId, String userId);

}
