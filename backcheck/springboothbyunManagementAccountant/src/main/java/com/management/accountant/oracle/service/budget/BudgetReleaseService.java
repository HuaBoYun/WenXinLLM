package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Release Service接口
 * 
 * @description Release业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetReleaseService {

    /**
     * create
     */
    Map<String, Object> create(Map<String, Object> params, String companyId, String userId);

    /**
     * update
     */
    Map<String, Object> update(Map<String, Object> params, String companyId, String userId);

    /**
     * release
     */
    Map<String, Object> release(Map<String, Object> params, String companyId, String userId);

}
