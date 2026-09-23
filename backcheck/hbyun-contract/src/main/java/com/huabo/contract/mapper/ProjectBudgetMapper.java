package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.ProjectBudget;
import com.huabo.contract.vo.ProjectBudgetQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目预算表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface ProjectBudgetMapper extends BaseMapper<ProjectBudget> {

    /**
     * 分页查询项目预算列表
     *
     * @param param 查询参数
     * @return 项目预算列表
     */
    List<ProjectBudget> selectProjectBudgetList(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 根据预算编号查询项目预算
     *
     * @param budgetNo 预算编号
     * @return 项目预算
     */
    ProjectBudget selectByBudgetNo(@Param("budgetNo") String budgetNo);

    /**
     * 检查预算编号是否存在
     *
     * @param budgetNo 预算编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsBudgetNo(@Param("budgetNo") String budgetNo, @Param("excludeId") Long excludeId);

    /**
     * 获取指定日期的最大预算编号
     *
     * @param dateStr 日期字符串（yyyyMMdd格式）
     * @return 最大预算编号
     */
    String getMaxBudgetNoByDate(@Param("dateStr") String dateStr);

    /**
     * 批量更新预算状态
     *
     * @param ids 预算ID列表
     * @param budgetStatus 预算状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateBudgetStatus(@Param("ids") List<Long> ids, 
                               @Param("budgetStatus") Integer budgetStatus, 
                               @Param("updateBy") Long updateBy);

    /**
     * 更新使用金额
     *
     * @param id 预算ID
     * @param usedAmount 使用金额
     * @return 更新数量
     */
    int updateUsedAmount(@Param("id") Long id, @Param("usedAmount") BigDecimal usedAmount);

    /**
     * 批量更新使用金额
     *
     * @param ids 预算ID列表
     * @param usedAmount 使用金额
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateUsedAmount(@Param("ids") List<Long> ids, 
                             @Param("usedAmount") BigDecimal usedAmount, 
                             @Param("updateBy") Long updateBy);

    /**
     * 获取待审核的预算列表
     *
     * @return 待审核的预算列表
     */
    List<ProjectBudget> selectPendingReviewBudgets();

    /**
     * 获取执行中的预算列表
     *
     * @return 执行中的预算列表
     */
    List<ProjectBudget> selectInProgressBudgets();

    /**
     * 获取已完成的预算列表
     *
     * @return 已完成的预算列表
     */
    List<ProjectBudget> selectCompletedBudgets();

    /**
     * 获取超预算的预算列表
     *
     * @return 超预算的预算列表
     */
    List<ProjectBudget> selectOverBudgets();

    /**
     * 获取预算紧张的预算列表
     *
     * @return 预算紧张的预算列表
     */
    List<ProjectBudget> selectTightBudgets();

    /**
     * 获取大额预算列表
     *
     * @param minAmount 最小金额
     * @return 大额预算列表
     */
    List<ProjectBudget> selectLargeBudgets(@Param("minAmount") BigDecimal minAmount);

    /**
     * 获取我负责的预算列表
     *
     * @param userId 用户ID
     * @return 我负责的预算列表
     */
    List<ProjectBudget> selectMyResponsibleBudgets(@Param("userId") Long userId);

    /**
     * 获取我参与的预算列表
     *
     * @param userId 用户ID
     * @return 我参与的预算列表
     */
    List<ProjectBudget> selectMyParticipateBudgets(@Param("userId") Long userId);

    /**
     * 根据项目ID查询预算列表
     *
     * @param projectId 项目ID
     * @return 预算列表
     */
    List<ProjectBudget> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 统计项目预算数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectProjectBudgetStatistics(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 获取预算类型分布统计
     *
     * @param param 查询参数
     * @return 预算类型分布
     */
    List<Map<String, Object>> selectBudgetTypeDistribution(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 获取预算状态分布统计
     *
     * @param param 查询参数
     * @return 预算状态分布
     */
    List<Map<String, Object>> selectBudgetStatusDistribution(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 获取月度预算趋势
     *
     * @param param 查询参数
     * @return 月度预算趋势
     */
    List<Map<String, Object>> selectMonthlyBudgetTrend(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 获取预算金额分布统计
     *
     * @param param 查询参数
     * @return 预算金额分布
     */
    List<Map<String, Object>> selectBudgetAmountDistribution(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 获取使用率分布统计
     *
     * @param param 查询参数
     * @return 使用率分布
     */
    List<Map<String, Object>> selectUsageRateDistribution(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 获取编制人分布统计
     *
     * @param param 查询参数
     * @return 编制人分布
     */
    List<Map<String, Object>> selectCompilerDistribution(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 模糊搜索项目预算
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目预算列表
     */
    List<ProjectBudget> searchProjectBudgets(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取今日到期的预算列表
     *
     * @return 今日到期的预算列表
     */
    List<ProjectBudget> selectTodayExpiringBudgets();

    /**
     * 获取本周到期的预算列表
     *
     * @return 本周到期的预算列表
     */
    List<ProjectBudget> selectThisWeekExpiringBudgets();

    /**
     * 获取本月到期的预算列表
     *
     * @return 本月到期的预算列表
     */
    List<ProjectBudget> selectThisMonthExpiringBudgets();

    /**
     * 获取年度预算汇总
     *
     * @param year 年度
     * @return 年度预算汇总
     */
    Map<String, Object> selectYearlyBudgetSummary(@Param("year") Integer year);

    /**
     * 获取季度预算汇总
     *
     * @param year 年度
     * @param quarter 季度
     * @return 季度预算汇总
     */
    Map<String, Object> selectQuarterlyBudgetSummary(@Param("year") Integer year, @Param("quarter") Integer quarter);

    /**
     * 获取月度预算汇总
     *
     * @param year 年度
     * @param month 月份
     * @return 月度预算汇总
     */
    Map<String, Object> selectMonthlyBudgetSummary(@Param("year") Integer year, @Param("month") Integer month);

    /**
     * 获取部门预算汇总
     *
     * @param departmentId 部门ID
     * @param year 年度
     * @return 部门预算汇总
     */
    Map<String, Object> selectDepartmentBudgetSummary(@Param("departmentId") Long departmentId, @Param("year") Integer year);

    /**
     * 获取项目预算汇总
     *
     * @param projectId 项目ID
     * @return 项目预算汇总
     */
    Map<String, Object> selectProjectBudgetSummary(@Param("projectId") Long projectId);

    /**
     * 获取预算执行情况
     *
     * @param budgetId 预算ID
     * @return 预算执行情况
     */
    Map<String, Object> selectBudgetExecution(@Param("budgetId") Long budgetId);

    /**
     * 获取预算对比分析
     *
     * @param budgetId1 预算ID1
     * @param budgetId2 预算ID2
     * @return 预算对比分析
     */
    Map<String, Object> selectBudgetComparison(@Param("budgetId1") Long budgetId1, @Param("budgetId2") Long budgetId2);

    /**
     * 获取预算预警信息
     *
     * @param param 查询参数
     * @return 预算预警信息
     */
    List<Map<String, Object>> selectBudgetWarnings(@Param("param") ProjectBudgetQueryParam param);

    /**
     * 获取预算审批历史
     *
     * @param budgetId 预算ID
     * @return 预算审批历史
     */
    List<Map<String, Object>> selectBudgetApprovalHistory(@Param("budgetId") Long budgetId);

    /**
     * 获取预算变更历史
     *
     * @param budgetId 预算ID
     * @return 预算变更历史
     */
    List<Map<String, Object>> selectBudgetChangeHistory(@Param("budgetId") Long budgetId);

    /**
     * 获取预算使用明细
     *
     * @param budgetId 预算ID
     * @return 预算使用明细
     */
    List<Map<String, Object>> selectBudgetUsageDetails(@Param("budgetId") Long budgetId);
}
