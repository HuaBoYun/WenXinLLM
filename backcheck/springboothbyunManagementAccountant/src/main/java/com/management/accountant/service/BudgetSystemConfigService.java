package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetSystemConfigEntity;
import com.management.accountant.util.PageResult;
import java.util.List;
import java.util.Map;

public interface BudgetSystemConfigService {
    PageResult<BudgetSystemConfigEntity> getPage(Map<String, Object> params);
    List<BudgetSystemConfigEntity> getAll();
    List<BudgetSystemConfigEntity> getByType(String configType);
    BudgetSystemConfigEntity getById(String configId);
    BudgetSystemConfigEntity create(BudgetSystemConfigEntity config);
    void update(BudgetSystemConfigEntity config);
    void delete(String configId);
}

