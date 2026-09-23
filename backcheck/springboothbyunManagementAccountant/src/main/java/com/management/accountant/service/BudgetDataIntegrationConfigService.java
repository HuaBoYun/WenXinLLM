package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetDataIntegrationConfig;
import com.management.accountant.util.PageResult;
import java.util.List;
import java.util.Map;

public interface BudgetDataIntegrationConfigService {
    PageResult<BudgetDataIntegrationConfig> getPage(Map<String, Object> params);
    BudgetDataIntegrationConfig getById(String integrationId);
    BudgetDataIntegrationConfig create(BudgetDataIntegrationConfig config);
    void update(BudgetDataIntegrationConfig config);
    void delete(String integrationId);
    List<BudgetDataIntegrationConfig> exportData(Map<String, Object> params);
}

