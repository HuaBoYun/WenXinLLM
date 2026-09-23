package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetPeriod;

import java.util.List;

/**
 * 预算期间Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetPeriodService extends IService<BudgetPeriod> {

    /**
     * 根据期间编码查询期间
     * 
     * @param periodCode 期间编码
     * @return 预算期间
     */
    BudgetPeriod getByPeriodCode(String periodCode);

    /**
     * 查询当前期间
     * 
     * @return 当前期间
     */
    BudgetPeriod getCurrentPeriod();

    /**
     * 根据预算年度查询期间列表
     * 
     * @param budgetYear 预算年度
     * @return 期间列表
     */
    List<BudgetPeriod> getByBudgetYear(Integer budgetYear);

    /**
     * 根据期间类型查询期间列表
     * 
     * @param periodType 期间类型
     * @return 期间列表
     */
    List<BudgetPeriod> getByPeriodType(String periodType);

    /**
     * 创建预算期间
     * 
     * @param period 预算期间
     * @return 是否成功
     */
    boolean createPeriod(BudgetPeriod period);

    /**
     * 更新预算期间
     * 
     * @param period 预算期间
     * @return 是否成功
     */
    boolean updatePeriod(BudgetPeriod period);

    /**
     * 开放期间
     * 
     * @param periodId 期间ID
     * @return 是否成功
     */
    boolean openPeriod(String periodId);

    /**
     * 关闭期间
     * 
     * @param periodId 期间ID
     * @return 是否成功
     */
    boolean closePeriod(String periodId);

    /**
     * 锁定期间
     * 
     * @param periodId 期间ID
     * @return 是否成功
     */
    boolean lockPeriod(String periodId);

    /**
     * 批量删除期间
     * 
     * @param periodIds 期间ID列表
     * @return 是否成功
     */
    boolean batchDeletePeriods(List<String> periodIds);

    /**
     * 分页查询期间列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param period 查询条件
     * @return 期间列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetPeriod> pageQuery(
        int pageNum, int pageSize, BudgetPeriod period);
}

