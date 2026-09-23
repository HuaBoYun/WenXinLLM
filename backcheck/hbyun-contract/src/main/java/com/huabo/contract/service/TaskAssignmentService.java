package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.TaskAssignment;
import com.huabo.contract.vo.TaskAssignmentQueryParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 任务书表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface TaskAssignmentService extends IService<TaskAssignment> {

    /**
     * 分页查询任务书列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TaskAssignment> getTaskAssignmentList(TaskAssignmentQueryParam param);

    /**
     * 保存任务书
     *
     * @param taskAssignment 任务书
     * @return 保存结果
     */
    boolean saveTaskAssignment(TaskAssignment taskAssignment);

    /**
     * 根据ID获取任务书详情
     *
     * @param id 主键ID
     * @return 任务书详情
     */
    TaskAssignment getTaskAssignmentById(Long id);

    /**
     * 根据任务书编号获取任务书
     *
     * @param taskNo 任务书编号
     * @return 任务书
     */
    TaskAssignment getTaskAssignmentByTaskNo(String taskNo);

    /**
     * 删除任务书
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteTaskAssignment(Long id);

    /**
     * 批量删除任务书
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteTaskAssignment(List<Long> ids);

    /**
     * 生成任务书编号
     *
     * @return 任务书编号
     */
    String generateTaskNo();

    /**
     * 检查任务书编号是否存在
     *
     * @param taskNo 任务书编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsTaskNo(String taskNo, Long excludeId);

    /**
     * 更新任务状态
     *
     * @param id 任务ID
     * @param taskStatus 任务状态
     * @return 更新结果
     */
    boolean updateTaskStatus(Long id, Integer taskStatus);

    /**
     * 批量更新任务状态
     *
     * @param ids 任务ID列表
     * @param taskStatus 任务状态
     * @return 更新结果
     */
    boolean batchUpdateTaskStatus(List<Long> ids, Integer taskStatus);

    /**
     * 更新完成度
     *
     * @param id 任务ID
     * @param completionRate 完成度
     * @return 更新结果
     */
    boolean updateCompletionRate(Long id, BigDecimal completionRate);

    /**
     * 批量更新完成度
     *
     * @param ids 任务ID列表
     * @param completionRate 完成度
     * @return 更新结果
     */
    boolean batchUpdateCompletionRate(List<Long> ids, BigDecimal completionRate);

    /**
     * 审核任务书
     *
     * @param id 任务ID
     * @param reviewerId 审核人ID
     * @param reviewerName 审核人姓名
     * @param reviewComments 审核意见
     * @param approved 是否通过
     * @return 审核结果
     */
    boolean reviewTask(Long id, Long reviewerId, String reviewerName, String reviewComments, boolean approved);

    /**
     * 下达任务书
     *
     * @param id 任务ID
     * @param issuerId 下达人ID
     * @param issuerName 下达人姓名
     * @param receiverId 接收人ID
     * @param receiverName 接收人姓名
     * @return 下达结果
     */
    boolean issueTask(Long id, Long issuerId, String issuerName, Long receiverId, String receiverName);

    /**
     * 接收任务书
     *
     * @param id 任务ID
     * @param receiverId 接收人ID
     * @param receiverName 接收人姓名
     * @return 接收结果
     */
    boolean receiveTask(Long id, Long receiverId, String receiverName);

    /**
     * 开始执行任务
     *
     * @param id 任务ID
     * @return 执行结果
     */
    boolean startTask(Long id);

    /**
     * 完成任务
     *
     * @param id 任务ID
     * @return 完成结果
     */
    boolean completeTask(Long id);

    /**
     * 暂停任务
     *
     * @param id 任务ID
     * @return 暂停结果
     */
    boolean pauseTask(Long id);

    /**
     * 取消任务
     *
     * @param id 任务ID
     * @return 取消结果
     */
    boolean cancelTask(Long id);

    /**
     * 获取待审核的任务列表
     *
     * @return 待审核的任务列表
     */
    List<TaskAssignment> getPendingReviewTasks();

    /**
     * 获取已下达的任务列表
     *
     * @return 已下达的任务列表
     */
    List<TaskAssignment> getIssuedTasks();

    /**
     * 获取执行中的任务列表
     *
     * @return 执行中的任务列表
     */
    List<TaskAssignment> getInProgressTasks();

    /**
     * 获取已完成的任务列表
     *
     * @return 已完成的任务列表
     */
    List<TaskAssignment> getCompletedTasks();

    /**
     * 获取延期的任务列表
     *
     * @return 延期的任务列表
     */
    List<TaskAssignment> getDelayedTasks();

    /**
     * 获取即将到期的任务列表
     *
     * @param days 天数
     * @return 即将到期的任务列表
     */
    List<TaskAssignment> getExpiringSoonTasks(Integer days);

    /**
     * 获取紧急任务列表
     *
     * @return 紧急任务列表
     */
    List<TaskAssignment> getUrgentTasks();

    /**
     * 获取重要任务列表
     *
     * @return 重要任务列表
     */
    List<TaskAssignment> getImportantTasks();

    /**
     * 获取大额任务列表
     *
     * @param minAmount 最小金额
     * @return 大额任务列表
     */
    List<TaskAssignment> getLargeTasks(BigDecimal minAmount);

    /**
     * 获取我负责的任务列表
     *
     * @param userId 用户ID
     * @return 我负责的任务列表
     */
    List<TaskAssignment> getMyResponsibleTasks(Long userId);

    /**
     * 获取我接收的任务列表
     *
     * @param userId 用户ID
     * @return 我接收的任务列表
     */
    List<TaskAssignment> getMyReceivedTasks(Long userId);

    /**
     * 获取我下达的任务列表
     *
     * @param userId 用户ID
     * @return 我下达的任务列表
     */
    List<TaskAssignment> getMyIssuedTasks(Long userId);

    /**
     * 根据项目ID查询任务列表
     *
     * @param projectId 项目ID
     * @return 任务列表
     */
    List<TaskAssignment> getTaskAssignmentByProjectId(Long projectId);

    /**
     * 统计任务书数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getTaskAssignmentStatistics(TaskAssignmentQueryParam param);

    /**
     * 获取任务类型分布统计
     *
     * @param param 查询参数
     * @return 任务类型分布
     */
    List<Map<String, Object>> getTaskTypeDistribution(TaskAssignmentQueryParam param);

    /**
     * 获取任务状态分布统计
     *
     * @param param 查询参数
     * @return 任务状态分布
     */
    List<Map<String, Object>> getTaskStatusDistribution(TaskAssignmentQueryParam param);

    /**
     * 获取任务等级分布统计
     *
     * @param param 查询参数
     * @return 任务等级分布
     */
    List<Map<String, Object>> getTaskLevelDistribution(TaskAssignmentQueryParam param);

    /**
     * 获取优先级分布统计
     *
     * @param param 查询参数
     * @return 优先级分布
     */
    List<Map<String, Object>> getPriorityDistribution(TaskAssignmentQueryParam param);

    /**
     * 获取月度任务趋势
     *
     * @param param 查询参数
     * @return 月度任务趋势
     */
    List<Map<String, Object>> getMonthlyTaskTrend(TaskAssignmentQueryParam param);

    /**
     * 模糊搜索任务书
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 任务书列表
     */
    List<TaskAssignment> searchTaskAssignments(String keyword, Integer limit);

    /**
     * 获取今日到期的任务列表
     *
     * @return 今日到期的任务列表
     */
    List<TaskAssignment> getTodayExpiringTasks();

    /**
     * 获取本周到期的任务列表
     *
     * @return 本周到期的任务列表
     */
    List<TaskAssignment> getThisWeekExpiringTasks();

    /**
     * 获取本月到期的任务列表
     *
     * @return 本月到期的任务列表
     */
    List<TaskAssignment> getThisMonthExpiringTasks();

    /**
     * 导入任务书
     *
     * @param taskList 任务列表
     * @return 导入结果
     */
    Map<String, Object> importTaskAssignments(List<TaskAssignment> taskList);

    /**
     * 导出任务书
     *
     * @param param 查询参数
     * @return 任务列表
     */
    List<TaskAssignment> exportTaskAssignments(TaskAssignmentQueryParam param);

    /**
     * 自动更新过期任务状态
     *
     * @return 更新数量
     */
    int autoUpdateExpiredTasks();

    /**
     * 发送任务提醒通知
     *
     * @param days 提前天数
     * @return 发送数量
     */
    int sendTaskReminders(Integer days);
}
