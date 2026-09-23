package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Model Service接口
 * 
 * @description Model业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetModelService {

    /**
     * saveBudgetModel
     */
    Map<String, Object> saveBudgetModel(Map<String, Object> params, String companyId, String userId);

    /**
     * deleteBudgetModel
     */
    Map<String, Object> deleteBudgetModel(Map<String, Object> params, String companyId, String userId);

    /**
     * copyBudgetModel
     */
    Map<String, Object> copyBudgetModel(Map<String, Object> params, String companyId, String userId);

    /**
     * getBudgetModelList
     */
    Map<String, Object> getBudgetModelList(String companyId, String userId);

    /**
     * getBudgetModelDetail
     */
    Map<String, Object> getBudgetModelDetail(String companyId, String userId);

}
