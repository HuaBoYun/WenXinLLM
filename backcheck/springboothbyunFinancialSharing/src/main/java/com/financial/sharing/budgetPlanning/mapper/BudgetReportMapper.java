package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetReportQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算报表Mapper接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetReportMapper extends BaseMapper<TblBudgetReport> {

    /**
     * 查询报表配置列表
     * 
     * @param param 查询参数
     * @return 报表配置列表
     */
    List<TblBudgetReport> selectReportList(@Param("param") BudgetReportQueryParam param);

    /**
     * 查询预算明细数据
     * 
     * @param param 查询参数
     * @return 明细数据
     */
    List<Map<String, Object>> selectBudgetDetailData(@Param("param") BudgetReportQueryParam param);

    /**
     * 查询预算汇总数据
     * 
     * @param param 查询参数
     * @return 汇总数据
     */
    List<Map<String, Object>> selectBudgetSummaryData(@Param("param") BudgetReportQueryParam param);

    /**
     * 查询预算对比数据
     * 
     * @param param 查询参数
     * @return 对比数据
     */
    List<Map<String, Object>> selectBudgetCompareData(@Param("param") BudgetReportQueryParam param);

    /**
     * 查询预算趋势数据
     * 
     * @param param 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> selectBudgetTrendData(@Param("param") BudgetReportQueryParam param);
}

