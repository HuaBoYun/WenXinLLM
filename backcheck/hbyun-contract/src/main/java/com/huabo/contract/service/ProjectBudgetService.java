package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ProjectBudget;
import com.huabo.contract.vo.ProjectBudgetQueryParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 项目预算表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface ProjectBudgetService extends IService<ProjectBudget> {

    /**
     * 分页查询项目预算列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<ProjectBudget> getProjectBudgetList(ProjectBudgetQueryParam param);

    /**
     * 保存项目预算
     *
     * @param projectBudget 项目预算
     * @return 保存结果
     */
    boolean saveProjectBudget(ProjectBudget projectBudget);

    /**
     * 根据ID获取项目预算详情
     *
     * @param id 主键ID
     * @return 项目预算详情
     */
    ProjectBudget getProjectBudgetById(Long id);

    /**
     * 根据预算编号获取项目预算
     *
     * @param budgetNo 预算编号
     * @return 项目预算
     */
    ProjectBudget getProjectBudgetByBudgetNo(String budgetNo);

    /**
     * 删除项目预算
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteProjectBudget(Long id);

    /**
     * 批量删除项目预算
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteProjectBudget(List<Long> ids);

    /**
     * 生成预算编号
     *
     * @return 预算编号
     */
    String generateBudgetNo();

    /**
     * 检查预算编号是否存在
     *
     * @param budgetNo 预算编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsBudgetNo(String budgetNo, Long excludeId);

    /**
     * 更新预算状态
     *
     * @param id 预算ID
     * @param budgetStatus 预算状态
     * @return 更新结果
     */
    boolean updateBudgetStatus(Long id, Integer budgetStatus);

    /**
     * 批量更新预算状态
     *
     * @param ids 预算ID列表
     * @param budgetStatus 预算状态
     * @return 更新结果
     */
    boolean batchUpdateBudgetStatus(List<Long> ids, Integer budgetStatus);

    /**
     * 更新使用金额
     *
     * @param id 预算ID
     * @param usedAmount 使用金额
     * @return 更新结果
     */
    boolean updateUsedAmount(Long id, BigDecimal usedAmount);

    /**
     * 批量更新使用金额
     *
     * @param ids 预算ID列表
     * @param usedAmount 使用金额
     * @return 更新结果
     */
    boolean batchUpdateUsedAmount(List<Long> ids, BigDecimal usedAmount);

    /**
     * 审核预算
     *
     * @param id 预算ID
     * @param reviewerId 审核人ID
     * @param reviewerName 审核人姓名
     * @param reviewComments 审核意见
     * @param approved 是否通过
     * @return 审核结果
     */
    boolean reviewBudget(Long id, Long reviewerId, String reviewerName, String reviewComments, boolean approved);

    /**
     * 批准预算
     *
     * @param id 预算ID
     * @param approverId 批准人ID
     * @param approverName 批准人姓名
     * @param approveComments 批准意见
     * @param approved 是否通过
     * @return 批准结果
     */
    boolean approveBudget(Long id, Long approverId, String approverName, String approveComments, boolean approved);

    /**
     * 开始执行预算
     *
     * @param id 预算ID
     * @return 执行结果
     */
    boolean startBudget(Long id);

    /**
     * 完成预算
     *
     * @param id 预算ID
     * @return 完成结果
     */
    boolean completeBudget(Long id);

    /**
     * 作废预算
     *
     * @param id 预算ID
     * @return 作废结果
     */
    boolean voidBudget(Long id);

    /**
     * 获取待审核的预算列表
     *
     * @return 待审核的预算列表
     */
    List<ProjectBudget> getPendingReviewBudgets();

    /**
     * 获取执行中的预算列表
     *
     * @return 执行中的预算列表
     */
    List<ProjectBudget> getInProgressBudgets();

    /**
     * 获取已完成的预算列表
     *
     * @return 已完成的预算列表
     */
    List<ProjectBudget> getCompletedBudgets();

    /**
     * 获取超预算的预算列表
     *
     * @return 超预算的预算列表
     */
    List<ProjectBudget> getOverBudgets();

    /**
     * 获取预算紧张的预算列表
     *
     * @return 预算紧张的预算列表
     */
    List<ProjectBudget> getTightBudgets();

    /**
     * 获取大额预算列表
     *
     * @param minAmount 最小金额
     * @return 大额预算列表
     */
    List<ProjectBudget> getLargeBudgets(BigDecimal minAmount);

    /**
     * 获取我负责的预算列表
     *
     * @param userId 用户ID
     * @return 我负责的预算列表
     */
    List<ProjectBudget> getMyResponsibleBudgets(Long userId);

    /**
     * 获取我参与的预算列表
     *
     * @param userId 用户ID
     * @return 我参与的预算列表
     */
    List<ProjectBudget> getMyParticipateBudgets(Long userId);

    /**
     * 根据项目ID查询预算列表
     *
     * @param projectId 项目ID
     * @return 预算列表
     */
    List<ProjectBudget> getProjectBudgetByProjectId(Long projectId);

    /**
     * 统计项目预算数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getProjectBudgetStatistics(ProjectBudgetQueryParam param);

    /**
     * 获取预算类型分布统计
     *
     * @param param 查询参数
     * @return 预算类型分布
     */
    List<Map<String, Object>> getBudgetTypeDistribution(ProjectBudgetQueryParam param);

    /**
     * 获取预算状态分布统计
     *
     * @param param 查询参数
     * @return 预算状态分布
     */
    List<Map<String, Object>> getBudgetStatusDistribution(ProjectBudgetQueryParam param);

    /**
     * 获取月度预算趋势
     *
     * @param param 查询参数
     * @return 月度预算趋势
     */
    List<Map<String, Object>> getMonthlyBudgetTrend(ProjectBudgetQueryParam param);

    /**
     * 获取预算金额分布统计
     *
     * @param param 查询参数
     * @return 预算金额分布
     */
    List<Map<String, Object>> getBudgetAmountDistribution(ProjectBudgetQueryParam param);

    /**
     * 获取使用率分布统计
     *
     * @param param 查询参数
     * @return 使用率分布
     */
    List<Map<String, Object>> getUsageRateDistribution(ProjectBudgetQueryParam param);

    /**
     * 获取编制人分布统计
     *
     * @param param 查询参数
     * @return 编制人分布
     */
    List<Map<String, Object>> getCompilerDistribution(ProjectBudgetQueryParam param);

    /**
     * 模糊搜索项目预算
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目预算列表
     */
    List<ProjectBudget> searchProjectBudgets(String keyword, Integer limit);

    /**
     * 获取今日到期的预算列表
     *
     * @return 今日到期的预算列表
     */
    List<ProjectBudget> getTodayExpiringBudgets();

    /**
     * 获取本周到期的预算列表
     *
     * @return 本周到期的预算列表
     */
    List<ProjectBudget> getThisWeekExpiringBudgets();

    /**
     * 获取本月到期的预算列表
     *
     * @return 本月到期的预算列表
     */
    List<ProjectBudget> getThisMonthExpiringBudgets();

    /**
     * 导入项目预算
     *
     * @param budgetList 预算列表
     * @return 导入结果
     */
    Map<String, Object> importProjectBudgets(List<ProjectBudget> budgetList);

    /**
     * 导出项目预算
     *
     * @param param 查询参数
     * @return 预算列表
     */
    List<ProjectBudget> exportProjectBudgets(ProjectBudgetQueryParam param);

    /**
     * 自动更新过期预算状态
     *
     * @return 更新数量
     */
    int autoUpdateExpiredBudgets();

    /**
     * 发送预算预警通知
     *
     * @return 发送数量
     */
    int sendBudgetWarnings();

    /**
     * 获取年度预算汇总
     *
     * @param year 年度
     * @return 年度预算汇总
     */
    Map<String, Object> getYearlyBudgetSummary(Integer year);

    /**
     * 获取季度预算汇总
     *
     * @param year 年度
     * @param quarter 季度
     * @return 季度预算汇总
     */
    Map<String, Object> getQuarterlyBudgetSummary(Integer year, Integer quarter);

    /**
     * 获取月度预算汇总
     *
     * @param year 年度
     * @param month 月份
     * @return 月度预算汇总
     */
    Map<String, Object> getMonthlyBudgetSummary(Integer year, Integer month);

    /**
     * 获取部门预算汇总
     *
     * @param departmentId 部门ID
     * @param year 年度
     * @return 部门预算汇总
     */
    Map<String, Object> getDepartmentBudgetSummary(Long departmentId, Integer year);

    /**
     * 获取项目预算汇总
     *
     * @param projectId 项目ID
     * @return 项目预算汇总
     */
    Map<String, Object> getProjectBudgetSummary(Long projectId);

    /**
     * 获取预算执行情况
     *
     * @param budgetId 预算ID
     * @return 预算执行情况
     */
    Map<String, Object> getBudgetExecution(Long budgetId);

    /**
     * 获取预算对比分析
     *
     * @param budgetId1 预算ID1
     * @param budgetId2 预算ID2
     * @return 预算对比分析
     */
    Map<String, Object> getBudgetComparison(Long budgetId1, Long budgetId2);

    /**
     * 获取预算预警信息
     *
     * @param param 查询参数
     * @return 预算预警信息
     */
    List<Map<String, Object>> getBudgetWarnings(ProjectBudgetQueryParam param);
}
