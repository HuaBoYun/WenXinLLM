package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetConsolidation;

import java.util.List;

/**
 * 预算合并Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetConsolidationService extends IService<BudgetConsolidation> {

    /**
     * 根据合并编码查询合并
     */
    BudgetConsolidation getByConsolidationCode(String consolidationCode);

    /**
     * 根据合并类型查询合并列表
     */
    List<BudgetConsolidation> listByConsolidationType(String consolidationType);

    /**
     * 根据预算年度查询合并列表
     */
    List<BudgetConsolidation> listByBudgetYear(Integer budgetYear);

    /**
     * 根据合并状态查询合并列表
     */
    List<BudgetConsolidation> listByConsolidationStatus(String consolidationStatus);

    /**
     * 批量删除合并
     */
    boolean batchDeleteConsolidations(List<String> consolidationIds);

    /**
     * 保存或更新合并
     */
    boolean saveOrUpdateConsolidation(BudgetConsolidation consolidation);

    /**
     * 执行预算合并
     */
    boolean executeConsolidation(String consolidationId);
}

