package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * OrganizationStructure Service接口
 * 
 * @description OrganizationStructure业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetOrganizationStructureService {

    /**
     * enableOrganizationStructure
     */
    Map<String, Object> enableOrganizationStructure(Map<String, Object> params, String companyId, String userId);

}
