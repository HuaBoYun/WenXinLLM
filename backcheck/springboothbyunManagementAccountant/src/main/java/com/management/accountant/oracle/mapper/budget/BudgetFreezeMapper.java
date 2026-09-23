package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetFreeze;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算冻结Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetFreezeMapper extends BaseMapper<BudgetFreeze> {

    /**
     * 根据冻结编码查询冻结
     */
    BudgetFreeze selectByFreezeCode(@Param("freezeCode") String freezeCode);

    /**
     * 根据预算ID查询冻结列表
     */
    List<BudgetFreeze> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据冻结状态查询冻结列表
     */
    List<BudgetFreeze> selectByFreezeStatus(@Param("freezeStatus") String freezeStatus);

    /**
     * 查询已冻结的列表
     */
    List<BudgetFreeze> selectFrozenList();

    /**
     * 批量删除冻结
     */
    int batchDeleteFreezes(@Param("freezeIds") List<String> freezeIds);
}

