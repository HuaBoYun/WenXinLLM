package com.management.accountant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.oracle.entity.budget.BudgetYear;

import java.util.List;
import java.util.Map;

/**
 * 预算年度Service接口
 *
 * @author AI Agent
 * @date 2026-03-31
 */
public interface BudgetYearService {
    BudgetYear create(BudgetYear year);
    BudgetYear getById(String yearId);
    void update(BudgetYear year);
    void delete(String yearId);
    IPage<BudgetYear> getPage(Map<String, Object> params);
    List<BudgetYear> getAllYears();
    void openYear(String yearId);
    void closeYear(String yearId);
    void setCurrent(String yearId);
}
