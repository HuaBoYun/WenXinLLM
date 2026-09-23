package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblInvestmentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 投资计划Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Mapper
public interface InvestmentPlanMapper extends BaseMapper<TblInvestmentPlan> {

    /**
     * 分页查询投资计划列表
     */
    List<TblInvestmentPlan> selectPlanList(Map<String, Object> params);

    /**
     * 根据ID查询投资计划
     */
    TblInvestmentPlan selectPlanById(@Param("planId") Long planId);

    /**
     * 根据计划编号查询
     */
    TblInvestmentPlan selectByPlanNo(@Param("planNo") String planNo);

    /**
     * 根据状态查询计划列表
     */
    List<TblInvestmentPlan> selectByStatus(@Param("planStatus") String planStatus);

    /**
     * 根据投资类型查询计划列表
     */
    List<TblInvestmentPlan> selectByInvestmentType(@Param("investmentType") String investmentType);

    /**
     * 查询活跃计划（已审批和执行中）
     */
    List<TblInvestmentPlan> selectActivePlans();

    /**
     * 更新计划状态
     */
    int updatePlanStatus(@Param("planId") Long planId, @Param("planStatus") String planStatus);

    /**
     * 更新已投资金额
     */
    int updateInvestedAmount(@Param("planId") Long planId, @Param("investedAmount") BigDecimal investedAmount);

    /**
     * 更新实际收益率
     */
    int updateActualReturnRate(@Param("planId") Long planId, @Param("actualReturnRate") BigDecimal actualReturnRate);

    /**
     * 批量删除（逻辑删除）
     */
    int batchDeleteByIds(@Param("planIds") List<Long> planIds);

    /**
     * 统计计划数量按状态
     */
    List<Map<String, Object>> countPlansByStatus();

    /**
     * 统计计划数量按投资类型
     */
    List<Map<String, Object>> countPlansByInvestmentType();

    /**
     * 获取计划统计信息
     */
    Map<String, Object> selectPlanStatistics();

    /**
     * 查询即将到期的计划
     */
    List<TblInvestmentPlan> selectNearExpiryPlans(@Param("days") Integer days);

    /**
     * 查询逾期计划
     */
    List<TblInvestmentPlan> selectOverduePlans();
}

