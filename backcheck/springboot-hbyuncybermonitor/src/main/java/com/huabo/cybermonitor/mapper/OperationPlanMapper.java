package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.OperationPlan;
import com.huabo.cybermonitor.vo.OperationPlanQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 经营计划管理Mapper接口
 *
 * @author huabo
 * @since 2024-12-12
 */
@Mapper
public interface OperationPlanMapper extends BaseMapper<OperationPlan> {

    /**
     * 分页查询经营计划管理
     *
     * @param page    分页参数
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<OperationPlan> selectOperationPlanPage(Page<OperationPlan> page, @Param("queryVo") OperationPlanQueryVo queryVo);

    /**
     * 根据企业ID获取经营计划管理统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> selectStatisticsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取计划类型分布
     *
     * @param enterpriseId 企业ID
     * @return 计划类型分布
     */
    List<Map<String, Object>> selectPlanTypeDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取计划状态分布
     *
     * @param enterpriseId 企业ID
     * @return 计划状态分布
     */
    List<Map<String, Object>> selectPlanStatusDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取审批状态分布
     *
     * @param enterpriseId 企业ID
     * @return 审批状态分布
     */
    List<Map<String, Object>> selectApprovalStatusDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取执行状态分布
     *
     * @param enterpriseId 企业ID
     * @return 执行状态分布
     */
    List<Map<String, Object>> selectExecutionStatusDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取执行进度分布
     *
     * @param enterpriseId 企业ID
     * @return 执行进度分布
     */
    List<Map<String, Object>> selectExecutionProgressDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取计划执行趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 计划执行趋势
     */
    List<Map<String, Object>> selectPlanExecutionTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取目标达成趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 目标达成趋势
     */
    List<Map<String, Object>> selectTargetAchievementTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取计划调整统计
     *
     * @param enterpriseId 企业ID
     * @return 计划调整统计
     */
    Map<String, Object> selectPlanAdjustmentStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取计划调整趋势
     *
     * @param enterpriseId 企业ID
     * @param months       月份数
     * @return 计划调整趋势
     */
    List<Map<String, Object>> selectPlanAdjustmentTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 根据企业ID获取部门计划统计
     *
     * @param enterpriseId 企业ID
     * @return 部门计划统计
     */
    List<Map<String, Object>> selectDepartmentPlanStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取负责人计划统计
     *
     * @param enterpriseId 企业ID
     * @return 负责人计划统计
     */
    List<Map<String, Object>> selectManagerPlanStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取审批人员统计
     *
     * @param enterpriseId 企业ID
     * @return 审批人员统计
     */
    List<Map<String, Object>> selectApproverStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取制定方法统计
     *
     * @param enterpriseId 企业ID
     * @return 制定方法统计
     */
    List<Map<String, Object>> selectFormulationMethodStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取监控频率统计
     *
     * @param enterpriseId 企业ID
     * @return 监控频率统计
     */
    List<Map<String, Object>> selectMonitoringFrequencyStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取计划执行效率分析
     *
     * @param enterpriseId 企业ID
     * @return 计划执行效率分析
     */
    List<Map<String, Object>> selectPlanExecutionEfficiencyAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取目标达成效果分析
     *
     * @param enterpriseId 企业ID
     * @return 目标达成效果分析
     */
    List<Map<String, Object>> selectTargetAchievementEffectivenessAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取执行质量分析
     *
     * @param enterpriseId 企业ID
     * @return 执行质量分析
     */
    List<Map<String, Object>> selectExecutionQualityAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取创新性分析
     *
     * @param enterpriseId 企业ID
     * @return 创新性分析
     */
    List<Map<String, Object>> selectInnovationAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取计划改进建议
     *
     * @param enterpriseId 企业ID
     * @return 计划改进建议
     */
    List<Map<String, Object>> selectPlanImprovementSuggestions(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取成功经验总结
     *
     * @param enterpriseId 企业ID
     * @return 成功经验总结
     */
    List<Map<String, Object>> selectSuccessExperiences(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取失败教训总结
     *
     * @param enterpriseId 企业ID
     * @return 失败教训总结
     */
    List<Map<String, Object>> selectFailureLessons(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取能力提升建议
     *
     * @param enterpriseId 企业ID
     * @return 能力提升建议
     */
    List<Map<String, Object>> selectCapabilityEnhancementSuggestions(@Param("enterpriseId") String enterpriseId);

    /**
     * 批量更新计划状态
     *
     * @param planIds  计划ID列表
     * @param status   状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdatePlanStatus(@Param("planIds") List<String> planIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量更新审批状态
     *
     * @param planIds  计划ID列表
     * @param status   状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateApprovalStatus(@Param("planIds") List<String> planIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量更新执行状态
     *
     * @param planIds  计划ID列表
     * @param status   状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateExecutionStatus(@Param("planIds") List<String> planIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量删除经营计划管理
     *
     * @param planIds  计划ID列表
     * @param updateBy 更新人
     * @return 删除数量
     */
    int batchDeleteOperationPlan(@Param("planIds") List<String> planIds, @Param("updateBy") String updateBy);

    /**
     * 根据企业ID获取即将到期的计划
     *
     * @param enterpriseId 企业ID
     * @param days         天数
     * @return 即将到期的计划
     */
    List<OperationPlan> selectUpcomingPlans(@Param("enterpriseId") String enterpriseId, @Param("days") Integer days);

    /**
     * 根据企业ID获取逾期的计划
     *
     * @param enterpriseId 企业ID
     * @return 逾期的计划
     */
    List<OperationPlan> selectOverduePlans(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取待审批的计划
     *
     * @param enterpriseId 企业ID
     * @return 待审批的计划
     */
    List<OperationPlan> selectPendingApprovalPlans(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取执行异常的计划
     *
     * @param enterpriseId 企业ID
     * @return 执行异常的计划
     */
    List<OperationPlan> selectAbnormalExecutionPlans(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取需要调整的计划
     *
     * @param enterpriseId 企业ID
     * @return 需要调整的计划
     */
    List<OperationPlan> selectPlansNeedingAdjustment(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取战略目标统计
     *
     * @param enterpriseId 企业ID
     * @return 战略目标统计
     */
    Map<String, Object> selectStrategicObjectivesStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取经营目标统计
     *
     * @param enterpriseId 企业ID
     * @return 经营目标统计
     */
    Map<String, Object> selectOperationalObjectivesStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取财务目标统计
     *
     * @param enterpriseId 企业ID
     * @return 财务目标统计
     */
    Map<String, Object> selectFinancialObjectivesStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取管理目标统计
     *
     * @param enterpriseId 企业ID
     * @return 管理目标统计
     */
    Map<String, Object> selectManagementObjectivesStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取市场目标统计
     *
     * @param enterpriseId 企业ID
     * @return 市场目标统计
     */
    Map<String, Object> selectMarketTargetsStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取产品目标统计
     *
     * @param enterpriseId 企业ID
     * @return 产品目标统计
     */
    Map<String, Object> selectProductTargetsStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取客户目标统计
     *
     * @param enterpriseId 企业ID
     * @return 客户目标统计
     */
    Map<String, Object> selectCustomerTargetsStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取质量目标统计
     *
     * @param enterpriseId 企业ID
     * @return 质量目标统计
     */
    Map<String, Object> selectQualityTargetsStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取创新目标统计
     *
     * @param enterpriseId 企业ID
     * @return 创新目标统计
     */
    Map<String, Object> selectInnovationTargetsStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取人力资源目标统计
     *
     * @param enterpriseId 企业ID
     * @return 人力资源目标统计
     */
    Map<String, Object> selectHrTargetsStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取技术发展目标统计
     *
     * @param enterpriseId 企业ID
     * @return 技术发展目标统计
     */
    Map<String, Object> selectTechnologyTargetsStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据企业ID获取可持续发展目标统计
     *
     * @param enterpriseId 企业ID
     * @return 可持续发展目标统计
     */
    Map<String, Object> selectSustainabilityTargetsStatistics(@Param("enterpriseId") String enterpriseId);
}
