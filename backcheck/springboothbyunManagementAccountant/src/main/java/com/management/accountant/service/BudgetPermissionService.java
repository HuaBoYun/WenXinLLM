package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetPermission;
import com.management.accountant.util.PageResult;
import java.util.List;
import java.util.Map;

public interface BudgetPermissionService {
    PageResult<BudgetPermission> getPage(Map<String, Object> params);
    BudgetPermission getById(String permissionId);
    BudgetPermission create(BudgetPermission permission);
    void update(BudgetPermission permission);
    void delete(String permissionId);
    List<BudgetPermission> exportData(Map<String, Object> params);
}

