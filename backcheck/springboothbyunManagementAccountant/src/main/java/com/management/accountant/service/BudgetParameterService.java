package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetParameter;
import java.util.List;
import java.util.Map;

public interface BudgetParameterService {
    BudgetParameter create(BudgetParameter parameter);
    BudgetParameter getById(String parameterId);
    void update(BudgetParameter parameter);
    void delete(String parameterId);
    Map<String, Object> getPage(Map<String, Object> params);
    List<Map<String, Object>> getCategoryTree();
    List<Map<String, Object>> getCategories();
    void updateStatus(String parameterId, String status);
    Map<String, Object> validateParameter(Map<String, Object> params);
    void resetParameter(String parameterId);
    Map<String, Object> batchValidate(List<String> ids);
    List<BudgetParameter> getExportData(Map<String, Object> params);
    BudgetParameter getByIdDirect(String parameterId);
    Map<String, Object> importParameters(List<BudgetParameter> parameters);
}

