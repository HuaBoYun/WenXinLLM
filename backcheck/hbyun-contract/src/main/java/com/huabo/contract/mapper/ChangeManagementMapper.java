package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.ChangeManagement;
import com.huabo.contract.vo.ChangeManagementQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 项目变更管理Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface ChangeManagementMapper extends BaseMapper<ChangeManagement> {

    /**
     * 分页查询项目变更管理列表
     *
     * @param param 查询参数
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectChangeManagementList(@Param("param") ChangeManagementQueryParam param);

    /**
     * 根据变更编号查询项目变更管理
     *
     * @param changeNo 变更编号
     * @param excludeId 排除的ID
     * @return 项目变更管理
     */
    ChangeManagement selectByChangeNo(@Param("changeNo") String changeNo, @Param("excludeId") Long excludeId);

    /**
     * 根据项目ID查询项目变更管理列表
     *
     * @param projectId 项目ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据变更类型查询项目变更管理列表
     *
     * @param changeType 变更类型
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectByChangeType(@Param("changeType") Integer changeType);

    /**
     * 根据变更等级查询项目变更管理列表
     *
     * @param changeLevel 变更等级
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectByChangeLevel(@Param("changeLevel") Integer changeLevel);

    /**
     * 根据变更状态查询项目变更管理列表
     *
     * @param changeStatus 变更状态
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectByChangeStatus(@Param("changeStatus") Integer changeStatus);

    /**
     * 根据申请人ID查询项目变更管理列表
     *
     * @param applicantId 申请人ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectByApplicantId(@Param("applicantId") Long applicantId);

    /**
     * 根据审核人ID查询项目变更管理列表
     *
     * @param reviewerId 审核人ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectByReviewerId(@Param("reviewerId") Long reviewerId);

    /**
     * 根据批准人ID查询项目变更管理列表
     *
     * @param approverId 批准人ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectByApproverId(@Param("approverId") Long approverId);

    /**
     * 根据实施人ID查询项目变更管理列表
     *
     * @param implementerId 实施人ID
     * @return 项目变更管理列表
     */
    List<ChangeManagement> selectByImplementerId(@Param("implementerId") Long implementerId);

    /**
     * 获取草稿状态的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectDraftChanges();

    /**
     * 获取待审核的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectPendingReviewChanges();

    /**
     * 获取审核中的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectInReviewChanges();

    /**
     * 获取已批准的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectApprovedChanges();

    /**
     * 获取已拒绝的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectRejectedChanges();

    /**
     * 获取实施中的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectImplementingChanges();

    /**
     * 获取已完成的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectCompletedChanges();

    /**
     * 获取已取消的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectCancelledChanges();

    /**
     * 获取紧急变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectUrgentChanges();

    /**
     * 获取重要变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectImportantChanges();

    /**
     * 获取高优先级变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectHighPriorityChanges();

    /**
     * 获取有成本影响的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectCostImpactChanges();

    /**
     * 获取有进度影响的变更列表
     *
     * @return 变更列表
     */
    List<ChangeManagement> selectScheduleImpactChanges();

    /**
     * 模糊搜索项目变更管理
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目变更管理列表
     */
    List<ChangeManagement> searchChangeManagement(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 统计项目变更管理数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> statisticsChangeManagement(@Param("param") ChangeManagementQueryParam param);

    /**
     * 统计变更类型分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsChangeTypeDistribution(@Param("param") ChangeManagementQueryParam param);

    /**
     * 统计变更等级分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsChangeLevelDistribution(@Param("param") ChangeManagementQueryParam param);

    /**
     * 统计变更状态分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsChangeStatusDistribution(@Param("param") ChangeManagementQueryParam param);

    /**
     * 统计月度变更趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsMonthlyChangeTrend(@Param("param") ChangeManagementQueryParam param);

    /**
     * 统计部门变更数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsDepartmentChanges(@Param("param") ChangeManagementQueryParam param);

    /**
     * 统计项目变更数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsProjectChanges(@Param("param") ChangeManagementQueryParam param);

    /**
     * 统计申请人变更数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsApplicantChanges(@Param("param") ChangeManagementQueryParam param);

    /**
     * 计算总成本影响
     *
     * @param param 查询参数
     * @return 总成本影响
     */
    BigDecimal calculateTotalCostImpact(@Param("param") ChangeManagementQueryParam param);

    /**
     * 计算总进度影响
     *
     * @param param 查询参数
     * @return 总进度影响
     */
    Integer calculateTotalScheduleImpact(@Param("param") ChangeManagementQueryParam param);

    /**
     * 计算平均完成度
     *
     * @param param 查询参数
     * @return 平均完成度
     */
    BigDecimal calculateAverageCompletionRate(@Param("param") ChangeManagementQueryParam param);

    /**
     * 计算变更成功率
     *
     * @param param 查询参数
     * @return 变更成功率
     */
    BigDecimal calculateChangeSuccessRate(@Param("param") ChangeManagementQueryParam param);

    /**
     * 获取变更影响分析
     *
     * @param param 查询参数
     * @return 影响分析数据
     */
    Map<String, Object> getChangeImpactAnalysis(@Param("param") ChangeManagementQueryParam param);

    /**
     * 获取变更效果评估
     *
     * @param param 查询参数
     * @return 效果评估数据
     */
    Map<String, Object> getChangeEffectivenessEvaluation(@Param("param") ChangeManagementQueryParam param);

    /**
     * 导出项目变更管理数据
     *
     * @param param 查询参数
     * @return 项目变更管理列表
     */
    List<ChangeManagement> exportChangeManagement(@Param("param") ChangeManagementQueryParam param);

    /**
     * 批量更新变更状态
     *
     * @param ids 主键ID列表
     * @param changeStatus 变更状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateChangeStatus(@Param("ids") List<Long> ids, 
                               @Param("changeStatus") Integer changeStatus, 
                               @Param("updateBy") Long updateBy);

    /**
     * 批量更新优先级
     *
     * @param ids 主键ID列表
     * @param priority 优先级
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdatePriority(@Param("ids") List<Long> ids, 
                           @Param("priority") Integer priority, 
                           @Param("updateBy") Long updateBy);

    /**
     * 批量删除项目变更管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 删除数量
     */
    int batchDeleteChangeManagement(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 物理删除项目变更管理
     *
     * @param ids 主键ID列表
     * @return 删除数量
     */
    int physicalDeleteChangeManagement(@Param("ids") List<Long> ids);

    /**
     * 恢复删除的项目变更管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 恢复数量
     */
    int restoreChangeManagement(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);
}
