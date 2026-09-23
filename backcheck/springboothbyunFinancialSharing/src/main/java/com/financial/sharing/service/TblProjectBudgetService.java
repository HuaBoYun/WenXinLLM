package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.entity.TblProjectBudget;
import java.util.List;

/**
 * 项目预算Service接口
 */
public interface TblProjectBudgetService extends IService<TblProjectBudget> {
    
    /**
     * 根据项目ID查询预算列表
     */
    List<TblProjectBudget> getByProjectId(String projectId);
    
    /**
     * 根据项目ID和年月查询预算
     */
    TblProjectBudget getByProjectAndYearMonth(String projectId, Integer year, Integer month);
    
    /**
     * 根据预算编码查询
     */
    TblProjectBudget getByBudgetCode(String budgetCode);
    
    /**
     * 批量保存项目预算
     */
    boolean saveBatchBudgets(List<TblProjectBudget> budgets);
    
    /**
     * 删除项目的所有预算
     */
    boolean deleteByProjectId(String projectId);
}

