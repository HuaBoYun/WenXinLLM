package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetPeriod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算期间Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetPeriodMapper extends BaseMapper<BudgetPeriod> {

    /**
     * 根据期间编码查询期间
     * 
     * @param periodCode 期间编码
     * @return 预算期间
     */
    BudgetPeriod selectByPeriodCode(@Param("periodCode") String periodCode);

    /**
     * 查询当前期间
     * 
     * @return 当前期间
     */
    BudgetPeriod selectCurrentPeriod();

    /**
     * 根据预算年度查询期间列表
     * 
     * @param budgetYear 预算年度
     * @return 期间列表
     */
    List<BudgetPeriod> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /**
     * 根据期间类型查询期间列表
     * 
     * @param periodType 期间类型
     * @return 期间列表
     */
    List<BudgetPeriod> selectByPeriodType(@Param("periodType") String periodType);

    /**
     * 更新期间状态
     * 
     * @param periodId 期间ID
     * @param periodStatus 期间状态
     * @return 更新数量
     */
    int updatePeriodStatus(@Param("periodId") String periodId, @Param("periodStatus") String periodStatus);

    /**
     * 批量删除期间
     * 
     * @param periodIds 期间ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("periodIds") List<String> periodIds);
}

