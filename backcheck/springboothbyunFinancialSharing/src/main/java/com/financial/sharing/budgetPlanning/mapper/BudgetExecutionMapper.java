package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetExecutionQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetExecution;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算执行分析Mapper接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetExecutionMapper extends BaseMapper<TblBudgetExecution> {

    /**
     * 查询预算执行列表
     * 
     * @param param 查询参数
     * @return 执行列表
     */
    List<TblBudgetExecution> selectExecutionList(@Param("param") BudgetExecutionQueryParam param);

    /**
     * 查询预算执行统计
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectExecutionStatistics(@Param("param") BudgetExecutionQueryParam param);

    /**
     * 查询预算执行趋势
     * 
     * @param param 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> selectExecutionTrend(@Param("param") BudgetExecutionQueryParam param);

    /**
     * 查询预算执行预警列表
     * 
     * @param param 查询参数
     * @return 预警列表
     */
    List<TblBudgetExecution> selectExecutionWarnings(@Param("param") BudgetExecutionQueryParam param);
}

