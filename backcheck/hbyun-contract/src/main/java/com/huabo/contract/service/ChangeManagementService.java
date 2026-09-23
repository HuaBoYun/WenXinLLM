package com.huabo.contract.service;

import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ChangeManagement;
import com.huabo.contract.vo.ChangeManagementQueryParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 项目变更管理Service接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface ChangeManagementService {

    /**
     * 分页查询项目变更管理列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<ChangeManagement> getChangeManagementList(ChangeManagementQueryParam param);

    /**
     * 根据ID获取项目变更管理详情
     *
     * @param id 主键ID
     * @return 项目变更管理详情
     */
    ChangeManagement getChangeManagementById(Long id);

    /**
     * 保存项目变更管理（新增或修改）
     *
     * @param changeManagement 项目变更管理
     * @return 保存结果
     */
    boolean saveChangeManagement(ChangeManagement changeManagement);

    /**
     * 删除项目变更管理
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteChangeManagement(Long id);

    /**
     * 批量删除项目变更管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteChangeManagement(List<Long> ids);

    /**
     * 检查变更编号是否存在
     *
     * @param changeNo 变更编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsChangeNo(String changeNo, Long excludeId);

    /**
     * 根据项目ID查询项目变更管理列表
     *
     * @param projectId 项目ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> getChangeManagementByProjectId(Long projectId);

    /**
     * 根据变更类型查询项目变更管理列表
     *
     * @param changeType 变更类型
     * @return 项目变更管理列表
     */
    List<ChangeManagement> getChangeManagementByChangeType(Integer changeType);

    /**
     * 根据变更等级查询项目变更管理列表
     *
     * @param changeLevel 变更等级
     * @return 项目变更管理列表
     */
    List<ChangeManagement> getChangeManagementByChangeLevel(Integer changeLevel);

    /**
     * 根据变更状态查询项目变更管理列表
     *
     * @param changeStatus 变更状态
     * @return 项目变更管理列表
     */
    List<ChangeManagement> getChangeManagementByChangeStatus(Integer changeStatus);

    /**
     * 根据申请人ID查询项目变更管理列表
     *
     * @param applicantId 申请人ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> getChangeManagementByApplicantId(Long applicantId);

    /**
     * 根据审核人ID查询项目变更管理列表
     *
     * @param reviewerId 审核人ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> getChangeManagementByReviewerId(Long reviewerId);

    /**
     * 根据批准人ID查询项目变更管理列表
     *
     * @param approverId 批准人ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> getChangeManagementByApproverId(Long approverId);

    /**
     * 根据实施人ID查询项目变更管理列表
     *
     * @param implementerId 实施人ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> getChangeManagementByImplementerId(Long implementerId);

    /**
     * 获取草稿状态的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getDraftChanges();

    /**
     * 获取待审核的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getPendingReviewChanges();

    /**
     * 获取审核中的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getInReviewChanges();

    /**
     * 获取已批准的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getApprovedChanges();

    /**
     * 获取已拒绝的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getRejectedChanges();

    /**
     * 获取实施中的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getImplementingChanges();

    /**
     * 获取已完成的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getCompletedChanges();

    /**
     * 获取已取消的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getCancelledChanges();

    /**
     * 获取紧急变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getUrgentChanges();

    /**
     * 获取重要变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getImportantChanges();

    /**
     * 获取高优先级变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getHighPriorityChanges();

    /**
     * 获取有成本影响的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getCostImpactChanges();

    /**
     * 获取有进度影响的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> getScheduleImpactChanges();

    /**
     * 模糊搜索项目变更管理
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目变更管理列表
     */
    List<ChangeManagement> searchChangeManagement(String keyword, Integer limit);

    /**
     * 统计项目变更管理数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getChangeManagementStatistics(ChangeManagementQueryParam param);

    /**
     * 统计变更类型分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getChangeTypeDistribution(ChangeManagementQueryParam param);

    /**
     * 统计变更等级分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getChangeLevelDistribution(ChangeManagementQueryParam param);

    /**
     * 统计变更状态分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getChangeStatusDistribution(ChangeManagementQueryParam param);

    /**
     * 统计月度变更趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getMonthlyChangeTrend(ChangeManagementQueryParam param);

    /**
     * 统计部门变更数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getDepartmentChanges(ChangeManagementQueryParam param);

    /**
     * 统计项目变更数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getProjectChanges(ChangeManagementQueryParam param);

    /**
     * 统计申请人变更数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getApplicantChanges(ChangeManagementQueryParam param);

    /**
     * 计算总成本影响
     *
     * @param param 查询参数
     * @return 总成本影响
     */
    BigDecimal calculateTotalCostImpact(ChangeManagementQueryParam param);

    /**
     * 计算总进度影响
     *
     * @param param 查询参数
     * @return 总进度影响
     */
    Integer calculateTotalScheduleImpact(ChangeManagementQueryParam param);

    /**
     * 计算平均完成度
     *
     * @param param 查询参数
     * @return 平均完成度
     */
    BigDecimal calculateAverageCompletionRate(ChangeManagementQueryParam param);

    /**
     * 计算变更成功率
     *
     * @param param 查询参数
     * @return 变更成功率
     */
    BigDecimal calculateChangeSuccessRate(ChangeManagementQueryParam param);

    /**
     * 获取变更影响分析
     *
     * @param param 查询参数
     * @return 影响分析数据
     */
    Map<String, Object> getChangeImpactAnalysis(ChangeManagementQueryParam param);

    /**
     * 获取变更效果评估
     *
     * @param param 查询参数
     * @return 效果评估数据
     */
    Map<String, Object> getChangeEffectivenessEvaluation(ChangeManagementQueryParam param);

    /**
     * 导出项目变更管理数据
     *
     * @param param 查询参数
     * @return 项目变更管理列表
     */
    List<ChangeManagement> exportChangeManagement(ChangeManagementQueryParam param);

    /**
     * 批量更新变更状态
     *
     * @param ids 主键ID列表
     * @param changeStatus 变更状态
     * @param updateBy 更新人
     * @return 更新结果
     */
    boolean batchUpdateChangeStatus(List<Long> ids, Integer changeStatus, Long updateBy);

    /**
     * 批量更新优先级
     *
     * @param ids 主键ID列表
     * @param priority 优先级
     * @param updateBy 更新人
     * @return 更新结果
     */
    boolean batchUpdatePriority(List<Long> ids, Integer priority, Long updateBy);

    /**
     * 恢复删除的项目变更管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 恢复结果
     */
    boolean restoreChangeManagement(List<Long> ids, Long updateBy);

    /**
     * 物理删除项目变更管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean physicalDeleteChangeManagement(List<Long> ids);
}
