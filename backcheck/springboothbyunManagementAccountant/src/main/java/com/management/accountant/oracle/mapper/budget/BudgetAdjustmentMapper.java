package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetAdjustment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算调整Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetAdjustmentMapper extends BaseMapper<BudgetAdjustment> {

    /**
     * 根据调整编码查询调整
     * 
     * @param adjustmentCode 调整编码
     * @return 预算调整
     */
    BudgetAdjustment selectByAdjustmentCode(@Param("adjustmentCode") String adjustmentCode);

    /**
     * 根据原预算ID查询调整记录
     * 
     * @param originalBudgetId 原预算ID
     * @return 调整列表
     */
    List<BudgetAdjustment> selectByOriginalBudgetId(@Param("originalBudgetId") String originalBudgetId);

    /**
     * 查询待审批的调整列表
     * 
     * @param approverId 审批人ID
     * @return 调整列表
     */
    List<BudgetAdjustment> selectPendingAdjustments(@Param("approverId") String approverId);

    /**
     * 更新调整状态
     * 
     * @param adjustmentId 调整ID
     * @param adjustmentStatus 调整状态
     * @return 更新数量
     */
    int updateAdjustmentStatus(@Param("adjustmentId") String adjustmentId, @Param("adjustmentStatus") String adjustmentStatus);

    /**
     * 批量删除调整记录
     * 
     * @param adjustmentIds 调整ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("adjustmentIds") List<String> adjustmentIds);
}

