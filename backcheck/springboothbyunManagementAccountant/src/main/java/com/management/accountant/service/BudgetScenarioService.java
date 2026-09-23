package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetScenario;

import java.util.List;
import java.util.Map;

public interface BudgetScenarioService {
    BudgetScenario create(BudgetScenario scenario);
    BudgetScenario getById(String scenarioId);
    void update(BudgetScenario scenario);
    void delete(String scenarioId);
    Map<String, Object> getPage(Map<String, Object> params);
    List<BudgetScenario> getBaselineScenarios();
    List<Map<String, Object>> getAccountOptions();
    Map<String, Object> calculateScenario(Map<String, Object> params);
    Map<String, Object> compareScenarios(Map<String, Object> params);
    void setBaseline(String scenarioId);
    void submitApproval(String scenarioId);
}

