package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TaskAssignment;
import com.huabo.contract.vo.TaskAssignmentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务书表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface TaskAssignmentMapper extends BaseMapper<TaskAssignment> {

    /**
     * 分页查询任务书列表
     *
     * @param param 查询参数
     * @return 任务书列表
     */
    List<TaskAssignment> selectTaskAssignmentList(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 根据任务书编号查询任务书
     *
     * @param taskNo 任务书编号
     * @return 任务书
     */
    TaskAssignment selectByTaskNo(@Param("taskNo") String taskNo);

    /**
     * 检查任务书编号是否存在
     *
     * @param taskNo 任务书编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsTaskNo(@Param("taskNo") String taskNo, @Param("excludeId") Long excludeId);

    /**
     * 根据日期获取当天最大序号
     *
     * @param dateStr 日期字符串(yyyyMMdd格式)
     * @return 最大序号
     */
    int getMaxSeqByDate(@Param("dateStr") String dateStr);

    /**
     * 批量更新任务状态
     *
     * @param ids 任务ID列表
     * @param taskStatus 任务状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateTaskStatus(@Param("ids") List<Long> ids, 
                             @Param("taskStatus") Integer taskStatus, 
                             @Param("updateBy") Long updateBy);

    /**
     * 更新完成度
     *
     * @param id 任务ID
     * @param completionRate 完成度
     * @return 更新数量
     */
    int updateCompletionRate(@Param("id") Long id, @Param("completionRate") BigDecimal completionRate);

    /**
     * 批量更新完成度
     *
     * @param ids 任务ID列表
     * @param completionRate 完成度
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateCompletionRate(@Param("ids") List<Long> ids, 
                                 @Param("completionRate") BigDecimal completionRate, 
                                 @Param("updateBy") Long updateBy);

    /**
     * 获取待审核的任务列表
     *
     * @return 待审核的任务列表
     */
    List<TaskAssignment> selectPendingReviewTasks();

    /**
     * 获取已下达的任务列表
     *
     * @return 已下达的任务列表
     */
    List<TaskAssignment> selectIssuedTasks();

    /**
     * 获取执行中的任务列表
     *
     * @return 执行中的任务列表
     */
    List<TaskAssignment> selectInProgressTasks();

    /**
     * 获取已完成的任务列表
     *
     * @return 已完成的任务列表
     */
    List<TaskAssignment> selectCompletedTasks();

    /**
     * 获取延期的任务列表
     *
     * @return 延期的任务列表
     */
    List<TaskAssignment> selectDelayedTasks();

    /**
     * 获取即将到期的任务列表
     *
     * @param days 天数
     * @return 即将到期的任务列表
     */
    List<TaskAssignment> selectExpiringSoonTasks(@Param("days") Integer days);

    /**
     * 获取紧急任务列表
     *
     * @return 紧急任务列表
     */
    List<TaskAssignment> selectUrgentTasks();

    /**
     * 获取重要任务列表
     *
     * @return 重要任务列表
     */
    List<TaskAssignment> selectImportantTasks();

    /**
     * 获取大额任务列表
     *
     * @param minAmount 最小金额
     * @return 大额任务列表
     */
    List<TaskAssignment> selectLargeTasks(@Param("minAmount") BigDecimal minAmount);

    /**
     * 获取我负责的任务列表
     *
     * @param userId 用户ID
     * @return 我负责的任务列表
     */
    List<TaskAssignment> selectMyResponsibleTasks(@Param("userId") Long userId);

    /**
     * 获取我接收的任务列表
     *
     * @param userId 用户ID
     * @return 我接收的任务列表
     */
    List<TaskAssignment> selectMyReceivedTasks(@Param("userId") Long userId);

    /**
     * 获取我下达的任务列表
     *
     * @param userId 用户ID
     * @return 我下达的任务列表
     */
    List<TaskAssignment> selectMyIssuedTasks(@Param("userId") Long userId);

    /**
     * 根据项目ID查询任务列表
     *
     * @param projectId 项目ID
     * @return 任务列表
     */
    List<TaskAssignment> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 统计任务书数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectTaskAssignmentStatistics(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取任务类型分布统计
     *
     * @param param 查询参数
     * @return 任务类型分布
     */
    List<Map<String, Object>> selectTaskTypeDistribution(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取任务状态分布统计
     *
     * @param param 查询参数
     * @return 任务状态分布
     */
    List<Map<String, Object>> selectTaskStatusDistribution(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取任务等级分布统计
     *
     * @param param 查询参数
     * @return 任务等级分布
     */
    List<Map<String, Object>> selectTaskLevelDistribution(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取优先级分布统计
     *
     * @param param 查询参数
     * @return 优先级分布
     */
    List<Map<String, Object>> selectPriorityDistribution(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取月度任务趋势
     *
     * @param param 查询参数
     * @return 月度任务趋势
     */
    List<Map<String, Object>> selectMonthlyTaskTrend(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取任务金额分布统计
     *
     * @param param 查询参数
     * @return 任务金额分布
     */
    List<Map<String, Object>> selectTaskAmountDistribution(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取完成度分布统计
     *
     * @param param 查询参数
     * @return 完成度分布
     */
    List<Map<String, Object>> selectCompletionRateDistribution(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取负责人分布统计
     *
     * @param param 查询参数
     * @return 负责人分布
     */
    List<Map<String, Object>> selectResponsiblePersonDistribution(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 获取部门分布统计
     *
     * @param param 查询参数
     * @return 部门分布
     */
    List<Map<String, Object>> selectDepartmentDistribution(@Param("param") TaskAssignmentQueryParam param);

    /**
     * 模糊搜索任务书
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 任务书列表
     */
    List<TaskAssignment> searchTaskAssignments(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取今日到期的任务列表
     *
     * @return 今日到期的任务列表
     */
    List<TaskAssignment> selectTodayExpiringTasks();

    /**
     * 获取本周到期的任务列表
     *
     * @return 本周到期的任务列表
     */
    List<TaskAssignment> selectThisWeekExpiringTasks();

    /**
     * 获取本月到期的任务列表
     *
     * @return 本月到期的任务列表
     */
    List<TaskAssignment> selectThisMonthExpiringTasks();

    /**
     * 获取任务执行情况
     *
     * @param taskId 任务ID
     * @return 任务执行情况
     */
    Map<String, Object> selectTaskExecution(@Param("taskId") Long taskId);

    /**
     * 获取任务进度跟踪
     *
     * @param taskId 任务ID
     * @return 任务进度跟踪
     */
    List<Map<String, Object>> selectTaskProgressTracking(@Param("taskId") Long taskId);

    /**
     * 获取任务审批历史
     *
     * @param taskId 任务ID
     * @return 任务审批历史
     */
    List<Map<String, Object>> selectTaskApprovalHistory(@Param("taskId") Long taskId);

    /**
     * 获取任务变更历史
     *
     * @param taskId 任务ID
     * @return 任务变更历史
     */
    List<Map<String, Object>> selectTaskChangeHistory(@Param("taskId") Long taskId);

    /**
     * 获取前置任务列表
     *
     * @param taskId 任务ID
     * @return 前置任务列表
     */
    List<TaskAssignment> selectPredecessorTasks(@Param("taskId") Long taskId);

    /**
     * 获取后续任务列表
     *
     * @param taskId 任务ID
     * @return 后续任务列表
     */
    List<TaskAssignment> selectSuccessorTasks(@Param("taskId") Long taskId);
}
