package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.PeriodHistory;

import java.util.List;

/**
 * 期间操作历史Service接口
 *
 * @author AI Agent
 * @date 2026-03-31
 */
public interface PeriodHistoryService {
    List<PeriodHistory> getHistoryByPeriodId(String periodId);
    PeriodHistory save(PeriodHistory history);
}
