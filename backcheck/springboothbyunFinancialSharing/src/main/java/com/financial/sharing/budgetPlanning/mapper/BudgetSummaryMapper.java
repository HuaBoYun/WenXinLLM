package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetSummaryQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算数据汇总Mapper
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetSummaryMapper extends BaseMapper<TblBudgetSummary> {

    /**
     * 查询汇总记录列表
     * 
     * @param param 查询参数
     * @return 汇总记录列表
     */
    List<TblBudgetSummary> selectSummaryList(@Param("param") BudgetSummaryQueryParam param);

    /**
     * 根据条件查询汇总记录
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @param version 版本
     * @param summaryType 汇总类型
     * @param orgId 组织ID
     * @return 汇总记录列表
     */
    List<TblBudgetSummary> selectByCondition(
        @Param("modelId") String modelId,
        @Param("period") String period,
        @Param("version") String version,
        @Param("summaryType") String summaryType,
        @Param("orgId") String orgId
    );

    /**
     * 查询汇总统计信息
     * 
     * @param orgId 组织ID
     * @return 统计信息
     */
    Map<String, Object> selectSummaryStatistics(@Param("orgId") String orgId);
}

