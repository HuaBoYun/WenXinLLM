package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetPreparationTask;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算编制任务服务接口
 * 
 * @description 预算编制任务业务逻辑接口，支持编制任务的完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
public interface IBudgetPreparationTaskService extends IService<BudgetPreparationTask> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建编制任务
     * @param task 编制任务信息
     * @return 是否创建成功
     */
    boolean createPreparationTask(BudgetPreparationTask task);

    /**
     * 更新编制任务
     * @param task 编制任务信息
     * @return 是否更新成功
     */
    boolean updatePreparationTask(BudgetPreparationTask task);

    /**
     * 删除编制任务
     * @param id 编制任务ID
     * @return 是否删除成功
     */
    boolean deletePreparationTask(String id);

    /**
     * 批量删除编制任务
     * @param ids 编制任务ID列表
     * @return 是否删除成功
     */
    boolean batchDeletePreparationTasks(List<String> ids);

    /**
     * 根据ID查询编制任务
     * @param id 编制任务ID
     * @return 编制任务信息
     */
    BudgetPreparationTask getPreparationTaskById(String id);

    /**
     * 根据编码查询编制任务
     * @param taskCode 任务编码
     * @return 编制任务信息
     */
    BudgetPreparationTask getPreparationTaskByCode(String taskCode);

    // ==================== 查询操作 ====================

    /**
     * 分页查询编制任务
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetPreparationTask> getPreparationTaskPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据任务类型查询编制任务
     * @param taskType 任务类型
     * @return 编制任务列表
     */
    List<BudgetPreparationTask> getPreparationTasksByType(String taskType);

    /**
     * 根据预算年度查询编制任务
     * @param fiscalYear 预算年度
     * @return 编制任务列表
     */
    List<BudgetPreparationTask> getPreparationTasksByFiscalYear(Integer fiscalYear);

    /**
     * 根据任务状态查询编制任务
     * @param taskStatus 任务状态
     * @return 编制任务列表
     */
    List<BudgetPreparationTask> getPreparationTasksByStatus(String taskStatus);

    /**
     * 根据负责人查询编制任务
     * @param ownerId 负责人ID
     * @return 编制任务列表
     */
    List<BudgetPreparationTask> getPreparationTasksByOwner(String ownerId);

    /**
     * 查询我的编制任务
     * @param userId 用户ID
     * @return 编制任务列表
     */
    List<BudgetPreparationTask> getMyPreparationTasks(String userId);

    /**
     * 查询待处理的编制任务
     * @param userId 用户ID
     * @return 编制任务列表
     */
    List<BudgetPreparationTask> getPendingPreparationTasks(String userId);

    /**
     * 查询即将到期的编制任务
     * @param days 天数
     * @return 即将到期的编制任务列表
     */
    List<BudgetPreparationTask> getExpiringPreparationTasks(Integer days);

    /**
     * 查询已过期的编制任务
     * @return 已过期的编制任务列表
     */
    List<BudgetPreparationTask> getExpiredPreparationTasks();

    // ==================== 业务操作 ====================

    /**
     * 启动编制任务
     * @param taskId 任务ID
     * @return 是否启动成功
     */
    boolean startPreparationTask(String taskId);

    /**
     * 暂停编制任务
     * @param taskId 任务ID
     * @return 是否暂停成功
     */
    boolean pausePreparationTask(String taskId);

    /**
     * 恢复编制任务
     * @param taskId 任务ID
     * @return 是否恢复成功
     */
    boolean resumePreparationTask(String taskId);

    /**
     * 完成编制任务
     * @param taskId 任务ID
     * @return 是否完成成功
     */
    boolean completePreparationTask(String taskId);

    /**
     * 取消编制任务
     * @param taskId 任务ID
     * @return 是否取消成功
     */
    boolean cancelPreparationTask(String taskId);

    /**
     * 重置编制任务
     * @param taskId 任务ID
     * @return 是否重置成功
     */
    boolean resetPreparationTask(String taskId);

    /**
     * 分配编制任务
     * @param taskId 任务ID
     * @param ownerId 负责人ID
     * @param participantIds 参与人员ID列表
     * @return 是否分配成功
     */
    boolean assignPreparationTask(String taskId, String ownerId, List<String> participantIds);

    /**
     * 提交审批
     * @param taskId 任务ID
     * @param workflowId 审批流程ID
     * @return 是否提交成功
     */
    boolean submitForApproval(String taskId, String workflowId);

    /**
     * 审批通过
     * @param taskId 任务ID
     * @param approvalComments 审批意见
     * @return 是否审批成功
     */
    boolean approvePreparationTask(String taskId, String approvalComments);

    /**
     * 审批拒绝
     * @param taskId 任务ID
     * @param rejectionReason 拒绝原因
     * @return 是否拒绝成功
     */
    boolean rejectPreparationTask(String taskId, String rejectionReason);

    /**
     * 复制编制任务
     * @param sourceTaskId 源任务ID
     * @param newTaskCode 新任务编码
     * @param newTaskName 新任务名称
     * @return 新任务ID
     */
    String copyPreparationTask(String sourceTaskId, String newTaskCode, String newTaskName);

    /**
     * 批量启动编制任务
     * @param taskIds 任务ID列表
     * @return 启动成功的任务数量
     */
    int batchStartPreparationTasks(List<String> taskIds);

    /**
     * 批量完成编制任务
     * @param taskIds 任务ID列表
     * @return 完成成功的任务数量
     */
    int batchCompletePreparationTasks(List<String> taskIds);

    /**
     * 批量取消编制任务
     * @param taskIds 任务ID列表
     * @return 取消成功的任务数量
     */
    int batchCancelPreparationTasks(List<String> taskIds);

    /**
     * 更新任务进度
     * @param taskId 任务ID
     * @param progress 进度百分比
     * @return 是否更新成功
     */
    boolean updateTaskProgress(String taskId, Integer progress);

    /**
     * 批量更新任务进度
     * @param taskProgressMap 任务进度映射（任务ID -> 进度）
     * @return 更新成功的任务数量
     */
    int batchUpdateTaskProgress(Map<String, Integer> taskProgressMap);

    // ==================== 统计分析 ====================

    /**
     * 获取编制任务统计信息
     * @return 统计信息
     */
    Map<String, Object> getPreparationTaskStatistics();

    /**
     * 按状态统计编制任务数量
     * @return 统计结果
     */
    List<Map<String, Object>> getPreparationTaskCountByStatus();

    /**
     * 按类型统计编制任务数量
     * @return 统计结果
     */
    List<Map<String, Object>> getPreparationTaskCountByType();

    /**
     * 按年度统计编制任务数量
     * @return 统计结果
     */
    List<Map<String, Object>> getPreparationTaskCountByYear();

    /**
     * 获取用户的编制任务统计信息
     * @param userId 用户ID
     * @return 统计信息
     */
    Map<String, Object> getUserPreparationTaskStatistics(String userId);

    /**
     * 获取编制任务完成率统计
     * @return 完成率统计
     */
    Map<String, Object> getPreparationTaskCompletionRate();

    /**
     * 获取编制任务趋势分析
     * @param months 月份数
     * @return 趋势分析数据
     */
    List<Map<String, Object>> getPreparationTaskTrend(Integer months);

    // ==================== 数据导入导出 ====================

    /**
     * 导出编制任务数据
     * @param params 导出参数
     * @return 导出文件路径
     */
    String exportPreparationTasks(Map<String, Object> params);

    /**
     * 导入编制任务数据
     * @param filePath 文件路径
     * @return 导入结果
     */
    Map<String, Object> importPreparationTasks(String filePath);

    // ==================== 数据清理 ====================

    /**
     * 清理过期的编制任务
     * @param days 过期天数
     * @return 清理数量
     */
    int cleanupExpiredPreparationTasks(Integer days);

    /**
     * 归档已完成的编制任务
     * @param days 完成天数
     * @return 归档数量
     */
    int archiveCompletedPreparationTasks(Integer days);

    // ==================== 验证方法 ====================

    /**
     * 检查任务编码是否存在
     * @param taskCode 任务编码
     * @param excludeId 排除的任务ID
     * @return 是否存在
     */
    boolean checkTaskCodeExists(String taskCode, String excludeId);

    /**
     * 验证任务状态转换是否有效
     * @param fromStatus 原状态
     * @param toStatus 目标状态
     * @return 是否有效
     */
    boolean isValidStatusTransition(String fromStatus, String toStatus);

    /**
     * 检查用户是否有任务操作权限
     * @param taskId 任务ID
     * @param userId 用户ID
     * @param operation 操作类型
     * @return 是否有权限
     */
    boolean hasTaskPermission(String taskId, String userId, String operation);
}
