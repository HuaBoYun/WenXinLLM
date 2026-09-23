package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetAccount;

import java.util.List;

/**
 * 预算科目Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetAccountService extends IService<BudgetAccount> {

    /**
     * 根据科目编码查询科目
     */
    BudgetAccount getByAccountCode(String accountCode);

    /**
     * 根据科目类型查询科目列表
     */
    List<BudgetAccount> listByAccountType(String accountType);

    /**
     * 根据上级科目ID查询子科目列表
     */
    List<BudgetAccount> listByParentId(String parentId);

    /**
     * 查询科目树结构
     */
    List<BudgetAccount> getAccountTree(String parentId);

    /**
     * 查询末级科目列表
     */
    List<BudgetAccount> listLeafAccounts();

    /**
     * 查询启用的科目列表
     */
    List<BudgetAccount> listEnabledAccounts();

    /**
     * 批量删除科目
     */
    boolean batchDeleteAccounts(List<String> accountIds);

    /**
     * 保存或更新科目
     */
    boolean saveOrUpdateAccount(BudgetAccount account);
}

