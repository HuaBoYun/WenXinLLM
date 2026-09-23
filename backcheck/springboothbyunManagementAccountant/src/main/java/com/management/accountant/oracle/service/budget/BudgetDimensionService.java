package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Dimension Service接口
 * 
 * @description Dimension业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetDimensionService {

    /**
     * getDimension
     */
    Map<String, Object> getDimension(Map<String, Object> params, String companyId, String userId);

    /**
     * getOrganizationStructurePage
     */
    Map<String, Object> getOrganizationStructurePage(Map<String, Object> params, String companyId, String userId);

    /**
     * deleteDimensionConfiguration
     */
    Map<String, Object> deleteDimensionConfiguration(Map<String, Object> params, String companyId, String userId);

    /**
     * getDimensionDetail
     */
    Map<String, Object> getDimensionDetail(String companyId, String userId);

    /**
     * getDimensionList
     */
    Map<String, Object> getDimensionList(String companyId, String userId);

    /**
     * getDimensionTree
     */
    Map<String, Object> getDimensionTree(String companyId, String userId);

}
