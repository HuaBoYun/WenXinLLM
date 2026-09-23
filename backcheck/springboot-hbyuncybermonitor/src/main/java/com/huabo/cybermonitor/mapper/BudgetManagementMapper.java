package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.BudgetManagement;
import com.huabo.cybermonitor.vo.BudgetManagementQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算管理Mapper接口
 *
 * @author huabo
 * @since 2024-12-12
 */
@Mapper
public interface BudgetManagementMapper extends BaseMapper<BudgetManagement> {

    /**
     * 分页查询预算管理
     *
     * @param page    分页参数
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<BudgetManagement> selectBudgetManagementPage(Page<BudgetManagement> page, @Param("queryVo") BudgetManagementQueryVo queryVo);

    /**
     * 根据企业ID获取预算管理统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> selectStatisticsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算类型分布
     *
     * @param enterpriseId 企业ID
     * @return 预算类型分布
     */
    List<Map<String, Object>> selectBudgetTypeDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算状态分布
     *
     * @param enterpriseId 企业ID
     * @return 预算状态分布
     */
    List<Map<String, Object>> selectBudgetStatusDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取审批状态分布
     *
     * @param enterpriseId 企业ID
     * @return 审批状态分布
     */
    List<Map<String, Object>> selectApprovalStatusDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取执行率分布
     *
     * @param enterpriseId 企业ID
     * @return 执行率分布
     */
    List<Map<String, Object>> selectExecutionRateDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算执行趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 预算执行趋势
     */
    List<Map<String, Object>> selectBudgetExecutionTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取预算差异趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 预算差异趋势
     */
    List<Map<String, Object>> selectBudgetVarianceTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取预算调整统计
     *
     * @param enterpriseId 企业ID
     * @return 预算调整统计
     */
    Map<String, Object> selectBudgetAdjustmentStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算调整趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 预算调整趋势
     */
    List<Map<String, Object>> selectBudgetAdjustmentTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取部门预算统计
     *
     * @param enterpriseId 企业ID
     * @return 部门预算统计
     */
    List<Map<String, Object>> selectDepartmentBudgetStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取负责人预算统计
     *
     * @param enterpriseId 企业ID
     * @return 负责人预算统计
     */
    List<Map<String, Object>> selectManagerBudgetStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取审批人员统计
     *
     * @param enterpriseId 企业ID
     * @return 审批人员统计
     */
    List<Map<String, Object>> selectApproverStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取编制方法统计
     *
     * @param enterpriseId 企业ID
     * @return 编制方法统计
     */
    List<Map<String, Object>> selectCompilationMethodStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取监控频率统计
     *
     * @param enterpriseId 企业ID
     * @return 监控频率统计
     */
    List<Map<String, Object>> selectMonitoringFrequencyStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算执行效率分析
     *
     * @param enterpriseId 企业ID
     * @return 预算执行效率分析
     */
    List<Map<String, Object>> selectBudgetExecutionEfficiencyAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算控制效果分析
     *
     * @param enterpriseId 企业ID
     * @return 预算控制效果分析
     */
    List<Map<String, Object>> selectBudgetControlEffectivenessAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取风险评估分析
     *
     * @param enterpriseId 企业ID
     * @return 风险评估分析
     */
    List<Map<String, Object>> selectRiskAssessmentAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取绩效评价分析
     *
     * @param enterpriseId 企业ID
     * @return 绩效评价分析
     */
    List<Map<String, Object>> selectPerformanceEvaluationAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算改进建议
     *
     * @param enterpriseId 企业ID
     * @return 预算改进建议
     */
    List<Map<String, Object>> selectBudgetImprovementSuggestions(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取最佳实践案例
     *
     * @param enterpriseId 企业ID
     * @return 最佳实践案例
     */
    List<Map<String, Object>> selectBestPracticeCases(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取能力提升建议
     *
     * @param enterpriseId 企业ID
     * @return 能力提升建议
     */
    List<Map<String, Object>> selectCapabilityEnhancementSuggestions(@Param("enterpriseId") String enterpriseId);

    /**
     * 批量更新预算状态
     *
     * @param budgetIds 预算ID列表
     * @param status    状态
     * @param updateBy  更新人
     * @return 更新数量
     */
    int batchUpdateBudgetStatus(@Param("budgetIds") List<String> budgetIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量更新审批状态
     *
     * @param budgetIds 预算ID列表
     * @param status    状态
     * @param updateBy  更新人
     * @return 更新数量
     */
    int batchUpdateApprovalStatus(@Param("budgetIds") List<String> budgetIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量删除预算管理
     *
     * @param budgetIds 预算ID列表
     * @param updateBy  更新人
     * @return 删除数量
     */
    int batchDeleteBudgetManagement(@Param("budgetIds") List<String> budgetIds, @Param("updateBy") String updateBy);

    /**
     * 根据企业ID获取即将到期的预算
     *
     * @param enterpriseId 企业ID
     * @param days         天数
     * @return 即将到期的预算
     */
    List<BudgetManagement> selectUpcomingBudgets(@Param("enterpriseId") String enterpriseId, @Param("days") Integer days);

    /**
     * 根据企业ID获取逾期的预算
     *
     * @param enterpriseId 企业ID
     * @return 逾期的预算
     */
    List<BudgetManagement> selectOverdueBudgets(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取待审批的预算
     *
     * @param enterpriseId 企业ID
     * @return 待审批的预算
     */
    List<BudgetManagement> selectPendingApprovalBudgets(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取执行异常的预算
     *
     * @param enterpriseId 企业ID
     * @return 执行异常的预算
     */
    List<BudgetManagement> selectAbnormalExecutionBudgets(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取需要调整的预算
     *
     * @param enterpriseId 企业ID
     * @return 需要调整的预算
     */
    List<BudgetManagement> selectBudgetsNeedingAdjustment(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算收入统计
     *
     * @param enterpriseId 企业ID
     * @return 预算收入统计
     */
    Map<String, Object> selectBudgetRevenueStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算支出统计
     *
     * @param enterpriseId 企业ID
     * @return 预算支出统计
     */
    Map<String, Object> selectBudgetExpenseStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算投资统计
     *
     * @param enterpriseId 企业ID
     * @return 预算投资统计
     */
    Map<String, Object> selectBudgetInvestmentStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算资金统计
     *
     * @param enterpriseId 企业ID
     * @return 预算资金统计
     */
    Map<String, Object> selectBudgetCashStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算人员统计
     *
     * @param enterpriseId 企业ID
     * @return 预算人员统计
     */
    Map<String, Object> selectBudgetPersonnelStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算运营统计
     *
     * @param enterpriseId 企业ID
     * @return 预算运营统计
     */
    Map<String, Object> selectBudgetOperatingStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取预算资本统计
     *
     * @param enterpriseId 企业ID
     * @return 预算资本统计
     */
    Map<String, Object> selectBudgetCapitalStatistics(@Param("enterpriseId") String enterpriseId);
}
