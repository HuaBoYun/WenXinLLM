package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Task Service接口
 * 
 * @description Task业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetTaskService {

    /**
     * createBudgetTask
     */
    Map<String, Object> createBudgetTask(Map<String, Object> params, String companyId, String userId);

    /**
     * assignTask
     */
    Map<String, Object> assignTask(String companyId, String userId);

}
