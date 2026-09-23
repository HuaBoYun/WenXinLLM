package com.global.treasurer.service;

import com.global.treasurer.entity.TblFundPlan;
import java.util.Map;

/**
 * 资金计划服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
public interface FundPlanService {

    /**
     * 分页查询资金计划
     *
     * @param params 查询参数
     * @return 分页结果
     */
    Map<String, Object> getFundPlanPage(Map<String, Object> params);

    /**
     * 根据ID查询资金计划
     *
     * @param planId 计划ID
     * @return 资金计划
     */
    TblFundPlan getFundPlanById(Long planId);

    /**
     * 创建资金计划
     *
     * @param fundPlan 资金计划
     * @return 影响行数
     */
    int createFundPlan(TblFundPlan fundPlan);

    /**
     * 更新资金计划
     *
     * @param fundPlan 资金计划
     * @return 影响行数
     */
    int updateFundPlan(TblFundPlan fundPlan);

    /**
     * 删除资金计划
     *
     * @param planId 计划ID
     * @return 影响行数
     */
    int deleteFundPlan(Long planId);

    /**
     * 提交资金计划
     *
     * @param planId 计划ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int submitFundPlan(Long planId, Long userId);

    /**
     * 审批资金计划
     *
     * @param planId 计划ID
     * @param userId 用户ID
     * @param approved 是否通过
     * @param opinion 审批意见
     * @return 影响行数
     */
    int approveFundPlan(Long planId, Long userId, Boolean approved, String opinion);

    /**
     * 执行资金计划
     *
     * @param planId 计划ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int executeFundPlan(Long planId, Long userId);

    /**
     * 完成资金计划
     *
     * @param planId 计划ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int completeFundPlan(Long planId, Long userId);

    /**
     * 取消资金计划
     *
     * @param planId 计划ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int cancelFundPlan(Long planId, Long userId);
}
