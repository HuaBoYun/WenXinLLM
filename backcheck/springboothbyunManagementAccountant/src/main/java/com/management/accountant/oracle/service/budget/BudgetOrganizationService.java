package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Organization Service接口
 * 
 * @description Organization业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetOrganizationService {

    /**
     * createOrganizationStructure
     */
    Map<String, Object> createOrganizationStructure(Map<String, Object> params, String companyId, String userId);

    /**
     * updateOrganizationStructure
     */
    Map<String, Object> updateOrganizationStructure(Map<String, Object> params, String companyId, String userId);

    /**
     * deleteOrganizationStructure
     */
    Map<String, Object> deleteOrganizationStructure(Map<String, Object> params, String companyId, String userId);

    /**
     * batchDeleteOrganizationStructures
     */
    Map<String, Object> batchDeleteOrganizationStructures(Map<String, Object> params, String companyId, String userId);

    /**
     * toggleOrganizationStructure
     */
    Map<String, Object> toggleOrganizationStructure(Map<String, Object> params, String companyId, String userId);

    /**
     * getOrganizationStructureList
     */
    Map<String, Object> getOrganizationStructureList(String companyId, String userId);

    /**
     * getOrganizationTree
     */
    Map<String, Object> getOrganizationTree(String companyId, String userId);

}
