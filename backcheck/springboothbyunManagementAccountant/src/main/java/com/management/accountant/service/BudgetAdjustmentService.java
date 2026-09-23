package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetAdjustment;


import java.util.Map;

/**
 * 预算调整Service接口
 * 
 * @description 预算调整业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetAdjustmentService {

    /**
     * 创建调整申请
     * 
     * @param adjustment 调整对象
     * @return 创建后的调整对象
     */
    BudgetAdjustment create(BudgetAdjustment adjustment);

    /**
     * 根据ID查询调整
     * 
     * @param adjustmentId 调整ID
     * @return 调整对象
     */
    BudgetAdjustment getById(String adjustmentId);

    /**
     * 更新调整申请
     * 
     * @param adjustment 调整对象
     */
    void update(BudgetAdjustment adjustment);

    /**
     * 分页查询调整列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    Map<String, Object> getPage(Map<String, Object> params);

    /**
     * 提交审批
     * 
     * @param adjustmentId 调整ID
     */
    void submit(String adjustmentId);

    /**
     * 审批调整
     * 
     * @param params 审批参数
     */
    void approve(Map<String, Object> params);

    /**
     * 执行调整
     * 
     * @param adjustmentId 调整ID
     */
    void execute(String adjustmentId);

    /**
     * 删除调整申请
     *
     * @param adjustmentId 调整ID
     */
    void delete(String adjustmentId);
}
