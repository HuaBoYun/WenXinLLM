package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetVarianceAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算差异分析Mapper接口
 * 对应表：TBL_BUDGET_VARIANCE_ANALYSIS
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetVarianceAnalysisMapper extends BaseMapper<BudgetVarianceAnalysis> {

    // ==================== 原有基础查询方法 ====================

    /** 根据预算ID查询差异分析列表 */
    List<BudgetVarianceAnalysis> selectByBudgetId(@Param("budgetId") String budgetId);

    /** 根据预算年度查询差异分析列表 */
    List<BudgetVarianceAnalysis> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /** 根据组织ID查询差异分析列表 */
    List<BudgetVarianceAnalysis> selectByOrganizationId(@Param("organizationId") String organizationId);

    /** 根据差异类型查询差异分析列表 */
    List<BudgetVarianceAnalysis> selectByVarianceType(@Param("varianceType") String varianceType);

    /** 根据分析状态查询差异分析列表 */
    List<BudgetVarianceAnalysis> selectByAnalysisStatus(@Param("analysisStatus") String analysisStatus);

    // ==================== 差异分析页面专用方法 ====================

    /**
     * 分页查询（使用分页插件，在service层处理分页逻辑）
     * 参数支持: organizationId/accountId/varianceType/budgetYear/budgetPeriod/startDate/endDate
     *
     * @param params 查询参数Map
     * @return 差异分析记录列表
     */
    List<BudgetVarianceAnalysis> selectByPage(Map<String, Object> params);

    /**
     * 统计数据查询（totalCount/totalVariance/positiveVariance/negativeVariance/totalBudget/varianceRate）
     *
     * @return 统计结果Map
     */
    Map<String, Object> selectStats();

    /**
     * 月度图表数据查询（近12个月，按月汇总正差异/负差异/差异率）
     *
     * @return 月度统计列表
     */
    List<Map<String, Object>> selectMonthlyChartData();

    /**
     * 季度图表数据查询（近6个季度，按季度汇总正差异/负差异/差异率）
     *
     * @return 季度统计列表
     */
    List<Map<String, Object>> selectQuarterlyChartData();

    /**
     * 年度图表数据查询（近5年，按年汇总正差异/负差异/差异率）
     *
     * @return 年度统计列表
     */
    List<Map<String, Object>> selectYearlyChartData();

    /**
     * 差异分布：按部门（ORGANIZATION_NAME）聚合，返回 DIST_NAME/DIST_VALUE 列表
     */
    List<Map<String, Object>> selectDistributionByDept();

    /**
     * 差异分布：按预算科目（ACCOUNT_NAME）聚合，返回 DIST_NAME/DIST_VALUE 列表
     */
    List<Map<String, Object>> selectDistributionByAccount();

    /**
     * 差异分布：按差异类型（VARIANCE_TYPE）聚合，返回 DIST_NAME/DIST_VALUE 列表
     */
    List<Map<String, Object>> selectDistributionByProject();

    /**
     * 对比分析数据（按ID查指定记录的期间对比数据）
     *
     * @param id 差异分析ID
     * @return 期间对比列表
     */
    List<Map<String, Object>> selectCompareData(@Param("id") String id);

    /**
     * 更新差异原因、改进措施、负责人
     *
     * @param id                  差异分析ID
     * @param varianceReason      差异原因
     * @param improvementMeasures 改进措施
     * @param reviewedBy          负责人（reviewed_by字段复用为responsible_person）
     * @return 影响行数
     */
    int updateReasonById(@Param("id") String id,
                         @Param("varianceReason") String varianceReason,
                         @Param("improvementMeasures") String improvementMeasures,
                         @Param("reviewedBy") String reviewedBy);

    /**
     * 批量逻辑删除
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<String> ids);
}

