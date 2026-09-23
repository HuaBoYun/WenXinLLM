package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetAccountMapping;

import java.util.List;
import java.util.Map;

/**
 * 预算科目Service接口
 */
public interface BudgetAccountService {

    BudgetAccount create(BudgetAccount account);

    BudgetAccount getById(String accountId);

    void update(BudgetAccount account);

    void delete(String accountId);

    Map<String, Object> getPage(Map<String, Object> params);

    List<Map<String, Object>> getAccountTree();

    List<BudgetAccount> getParentAccounts();

    void updateStatus(String accountId, Boolean isEnabled);

    Map<String, Object> batchValidate(List<String> ids);

    void batchDelete(List<String> ids);

    Map<String, Object> getStats();

    List<BudgetAccount> getExportData(Map<String, Object> params);

    BudgetAccount getByIdDirect(String accountId);

    Map<String, Object> importAccounts(List<BudgetAccount> accounts);

    /**
     * 保存科目映射
     *
     * @param accountId 科目ID
     * @param mappings 映射列表
     */
    void saveAccountMappings(String accountId, List<BudgetAccountMapping> mappings);
}

