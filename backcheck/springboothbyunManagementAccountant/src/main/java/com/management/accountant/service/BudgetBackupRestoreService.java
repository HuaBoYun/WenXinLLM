package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetBackupRestore;
import com.management.accountant.util.PageResult;
import java.util.List;
import java.util.Map;

public interface BudgetBackupRestoreService {
    PageResult<BudgetBackupRestore> getPage(Map<String, Object> params);
    BudgetBackupRestore getById(String backupId);
    BudgetBackupRestore create(BudgetBackupRestore backup);
    void delete(String backupId);
    void update(BudgetBackupRestore backup);
    List<BudgetBackupRestore> exportData(Map<String, Object> params);
    Map<String, Object> getStats();
    Map<String, Integer> getTypeStats();
}

