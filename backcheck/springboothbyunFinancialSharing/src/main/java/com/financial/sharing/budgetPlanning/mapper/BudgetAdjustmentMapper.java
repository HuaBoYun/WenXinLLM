package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetAdjustmentQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetAdjustment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算调整Mapper
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetAdjustmentMapper extends BaseMapper<TblBudgetAdjustment> {

    /**
     * 查询调整单列表
     * 
     * @param param 查询参数
     * @return 调整单列表
     */
    List<TblBudgetAdjustment> selectAdjustmentList(@Param("param") BudgetAdjustmentQueryParam param);

    /**
     * 根据ID查询调整单(包含明细)
     * 
     * @param adjustmentId 调整单ID
     * @return 调整单
     */
    TblBudgetAdjustment selectAdjustmentWithDetails(@Param("adjustmentId") String adjustmentId);

    /**
     * 查询调整单统计信息
     * 
     * @param orgId 组织ID
     * @return 统计信息
     */
    Map<String, Object> selectAdjustmentStatistics(@Param("orgId") String orgId);

    /**
     * 生成调整单号
     * 
     * @param orgId 组织ID
     * @return 调整单号
     */
    String generateAdjustmentNo(@Param("orgId") String orgId);
}

