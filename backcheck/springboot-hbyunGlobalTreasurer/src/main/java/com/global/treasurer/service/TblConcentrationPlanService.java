package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblConcentrationPlan;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
 * 归集计划Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblConcentrationPlanService {

    /**
     * 分页查询归集计划列表
     */
    PageInfo<TblConcentrationPlan> getPlanPage(Integer pageNum, Integer pageSize,
                                               String planName, String planType, String planStatus,
                                               Long strategyId, String executionDateStart, String executionDateEnd);

    /**
     * 根据ID查询归集计划
     */
    TblConcentrationPlan getPlanById(Long planId);

    /**
     * 保存归集计划
     */
    TblConcentrationPlan savePlan(TblConcentrationPlan plan);

    /**
     * 更新归集计划
     */
    void updatePlan(TblConcentrationPlan plan);

    /**
     * 删除归集计划
     */
    void deletePlan(Long planId);

    /**
     * 执行归集计划
     */
    String executePlan(Long planId);

    /**
     * 批量执行归集计划
     */
    String batchExecutePlan(List<Long> planIds);

    /**
     * 暂停归集计划
     */
    void pausePlan(Long planId);

    /**
     * 恢复归集计划
     */
    void resumePlan(Long planId);

    /**
     * 取消归集计划
     */
    void cancelPlan(Long planId);

    /**
     * 获取待执行的计划
     */
    List<TblConcentrationPlan> getPendingPlans();

    /**
     * 批量删除归集计划
     */
    void batchDeletePlan(List<Long> planIds);

    /**
     * 导出归集计划
     */
    void exportPlan(String planName, String planStatus, Long strategyId,
                  ServletOutputStream outputStream) throws Exception;
}
