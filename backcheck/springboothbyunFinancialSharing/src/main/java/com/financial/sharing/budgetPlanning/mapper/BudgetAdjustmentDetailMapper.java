package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.entity.TblBudgetAdjustmentDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算调整明细Mapper
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetAdjustmentDetailMapper extends BaseMapper<TblBudgetAdjustmentDetail> {

    /**
     * 根据调整单ID查询明细列表
     * 
     * @param adjustmentId 调整单ID
     * @return 明细列表
     */
    List<TblBudgetAdjustmentDetail> selectByAdjustmentId(@Param("adjustmentId") String adjustmentId);

    /**
     * 批量插入明细
     * 
     * @param detailList 明细列表
     * @return 插入条数
     */
    int batchInsert(@Param("detailList") List<TblBudgetAdjustmentDetail> detailList);

    /**
     * 根据调整单ID删除明细
     * 
     * @param adjustmentId 调整单ID
     * @return 删除条数
     */
    int deleteByAdjustmentId(@Param("adjustmentId") String adjustmentId);
}

