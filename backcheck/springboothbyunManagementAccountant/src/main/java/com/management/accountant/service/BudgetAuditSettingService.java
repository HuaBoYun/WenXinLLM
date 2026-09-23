package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetAuditSetting;

import java.util.List;

public interface BudgetAuditSettingService {

    List<BudgetAuditSetting> getAll();

    BudgetAuditSetting getByKey(String settingKey);

    void saveOrUpdate(BudgetAuditSetting setting);

    void deleteById(String settingId);
}
