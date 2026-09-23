package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Reserve Service接口
 * 
 * @description Reserve业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetReserveService {

    /**
     * create
     */
    Map<String, Object> create(Map<String, Object> params, String companyId, String userId);

    /**
     * update
     */
    Map<String, Object> update(Map<String, Object> params, String companyId, String userId);

    /**
     * reserve
     */
    Map<String, Object> reserve(Map<String, Object> params, String companyId, String userId);

}
