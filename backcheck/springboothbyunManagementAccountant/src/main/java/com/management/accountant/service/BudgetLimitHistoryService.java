package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetLimitHistory;

import java.util.List;

/**
 * 预算限额操作历史Service接口
 */
public interface BudgetLimitHistoryService {

    /**
     * 根据限额ID查询操作历史
     */
    List<BudgetLimitHistory> getHistoryByLimitId(String limitId);

    /**
     * 记录操作历史
     */
    BudgetLimitHistory recordHistory(String limitId, String operationType, String operationDesc,
                                      String beforeValue, String afterValue, String operator);
}
