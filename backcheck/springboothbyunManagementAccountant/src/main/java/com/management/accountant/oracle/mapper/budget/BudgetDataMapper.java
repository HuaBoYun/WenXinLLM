package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算数据Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetDataMapper extends BaseMapper<BudgetData> {

    /**
     * 批量插入预算数据
     * 
     * @param dataList 预算数据列表
     * @return 插入数量
     */
    int batchInsert(@Param("dataList") List<BudgetData> dataList);

    /**
     * 批量更新预算数据
     * 
     * @param dataList 预算数据列表
     * @return 更新数量
     */
    int batchUpdate(@Param("dataList") List<BudgetData> dataList);

    /**
     * 根据任务ID查询预算数据
     * 
     * @param taskId 任务ID
     * @return 预算数据列表
     */
    List<BudgetData> selectByTaskId(@Param("taskId") String taskId);

    /**
     * 根据指标ID查询预算数据
     * 
     * @param indicatorId 指标ID
     * @return 预算数据列表
     */
    List<BudgetData> selectByIndicatorId(@Param("indicatorId") String indicatorId);

    /**
     * 根据组织ID和期间查询预算数据
     * 
     * @param organizationId 组织ID
     * @param budgetPeriod 预算期间
     * @return 预算数据列表
     */
    List<BudgetData> selectByOrganizationAndPeriod(
        @Param("organizationId") String organizationId,
        @Param("budgetPeriod") String budgetPeriod
    );

    /**
     * 计算预算差异
     * 
     * @param dataId 数据ID
     * @return 更新数量
     */
    int calculateVariance(@Param("dataId") String dataId);

    /**
     * 批量删除预算数据
     * 
     * @param dataIds 数据ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("dataIds") List<String> dataIds);

    /**
     * 查询绩效统计数据（从预算数据聚合计算）
     * 返回: AVG_EXECUTION_RATE, AVG_VARIANCE_RATE, TOTAL_BUDGET, TOTAL_ACTUAL, DATA_COUNT
     */
    Map<String, Object> selectPerformanceStats();

    /**
     * 按期间分组查询预算值和实际值（用于图表）
     */
    List<Map<String, Object>> selectBudgetActualByPeriod();

    /**
     * 查询上一年度的平均差异率（用于计算改进率）
     */
    Map<String, Object> selectPreviousYearStats();

    /**
     * 查询准确性分布（按差异率区间分组统计）
     */
    List<Map<String, Object>> selectAccuracyDistribution();

    /**
     * 查询雷达图维度数据（执行效率、准确性、及时性、合规性、成本控制、质量）
     */
    Map<String, Object> selectRadarDimensions();
}

