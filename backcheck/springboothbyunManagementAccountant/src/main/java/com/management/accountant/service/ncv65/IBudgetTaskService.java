package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetTask;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算任务服务接口
 * 
 * @description 预算任务业务逻辑接口，支持任务的完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
public interface IBudgetTaskService extends IService<BudgetTask> {

    /**
     * 创建任务
     * 
     * @param task 任务信息
     * @return 创建结果
     */
    boolean createTask(BudgetTask task);

    /**
     * 更新任务
     * 
     * @param task 任务信息
     * @return 更新结果
     */
    boolean updateTask(BudgetTask task);

    /**
     * 删除任务
     * 
     * @param id 任务ID
     * @return 删除结果
     */
    boolean deleteTask(String id);

    /**
     * 批量删除任务
     * 
     * @param ids 任务ID列表
     * @return 删除结果
     */
    boolean batchDeleteTasks(List<String> ids);

    /**
     * 根据ID查询任务
     * 
     * @param id 任务ID
     * @return 任务信息
     */
    BudgetTask getTaskById(String id);

    /**
     * 根据任务编码查询任务
     * 
     * @param taskCode 任务编码
     * @return 任务信息
     */
    BudgetTask getTaskByCode(String taskCode);

    /**
     * 分页查询任务列表
     * 
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetTask> getTaskPage(Long current, Long size, Map<String, Object> params);

    /**
     * 根据任务类型查询任务列表
     * 
     * @param taskType 任务类型
     * @return 任务列表
     */
    List<BudgetTask> getTasksByType(String taskType);

    /**
     * 根据任务状态查询任务列表
     * 
     * @param taskStatus 任务状态
     * @return 任务列表
     */
    List<BudgetTask> getTasksByStatus(String taskStatus);

    /**
     * 查询启用的任务列表
     * 
     * @return 任务列表
     */
    List<BudgetTask> getEnabledTasks();

    /**
     * 根据预算年度查询任务列表
     * 
     * @param budgetYear 预算年度
     * @return 任务列表
     */
    List<BudgetTask> getTasksByBudgetYear(Integer budgetYear);

    /**
     * 根据组织体系ID查询任务列表
     * 
     * @param structureId 组织体系ID
     * @return 任务列表
     */
    List<BudgetTask> getTasksByStructureId(String structureId);

    /**
     * 查询待审批的任务列表
     * 
     * @return 任务列表
     */
    List<BudgetTask> getPendingApprovalTasks();

    /**
     * 查询已完成的任务列表
     * 
     * @return 任务列表
     */
    List<BudgetTask> getCompletedTasks();

    /**
     * 查询超期的任务列表
     * 
     * @return 任务列表
     */
    List<BudgetTask> getOverdueTasks();

    /**
     * 启用任务
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean enableTask(String id);

    /**
     * 禁用任务
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean disableTask(String id);

    /**
     * 批量更新任务状态
     * 
     * @param ids 任务ID列表
     * @param taskStatus 任务状态
     * @return 操作结果
     */
    boolean batchUpdateTaskStatus(List<String> ids, String taskStatus);

    /**
     * 批量更新启用状态
     * 
     * @param ids 任务ID列表
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    boolean batchUpdateEnabled(List<String> ids, Boolean isEnabled);

    /**
     * 复制任务
     * 
     * @param id 源任务ID
     * @param targetName 目标任务名称
     * @param targetCode 目标任务编码
     * @return 新任务信息
     */
    BudgetTask copyTask(String id, String targetName, String targetCode);

    /**
     * 检查任务编码是否存在
     * 
     * @param taskCode 任务编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkTaskCodeExists(String taskCode, String excludeId);

    /**
     * 启动任务
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean startTask(String id);

    /**
     * 完成任务
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean completeTask(String id);

    /**
     * 取消任务
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean cancelTask(String id);

    /**
     * 重置任务
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean resetTask(String id);

    /**
     * 分配任务
     * 
     * @param id 任务ID
     * @param assigneeId 分配人ID
     * @param assigneeName 分配人姓名
     * @return 操作结果
     */
    boolean assignTask(String id, String assigneeId, String assigneeName);

    /**
     * 提交审批
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean submitForApproval(String id);

    /**
     * 审批通过
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean approveTask(String id);

    /**
     * 审批拒绝
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean rejectTask(String id);

    /**
     * 更新任务进度
     * 
     * @param id 任务ID
     * @param progress 进度百分比
     * @return 操作结果
     */
    boolean updateTaskProgress(String id, Integer progress);

    /**
     * 查询我的任务（创建的任务）
     * 
     * @param userId 用户ID
     * @return 任务列表
     */
    List<BudgetTask> getMyCreatedTasks(String userId);

    /**
     * 查询分配给我的任务
     * 
     * @param userId 用户ID
     * @return 任务列表
     */
    List<BudgetTask> getMyAssignedTasks(String userId);

    /**
     * 查询我参与的任务
     * 
     * @param userId 用户ID
     * @return 任务列表
     */
    List<BudgetTask> getMyParticipatedTasks(String userId);

    /**
     * 同步任务数据
     * 
     * @param id 任务ID
     * @return 操作结果
     */
    boolean syncTaskData(String id);

    /**
     * 统计各任务类型的数量
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> countByTaskType();

    /**
     * 统计各任务状态的数量
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> countByTaskStatus();

    /**
     * 统计各预算年度的任务数量
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> countByBudgetYear();

    /**
     * 导出任务配置
     * 
     * @param ids 任务ID列表
     * @return 导出文件路径
     */
    String exportTaskConfig(List<String> ids);

    /**
     * 导入任务配置
     * 
     * @param filePath 文件路径
     * @return 导入结果
     */
    Map<String, Object> importTaskConfig(String filePath);

    /**
     * 验证任务配置
     * 
     * @param task 任务信息
     * @return 验证结果
     */
    Map<String, Object> validateTaskConfig(BudgetTask task);

    /**
     * 刷新任务缓存
     * 
     * @param taskId 任务ID，为空则刷新全部
     * @return 操作结果
     */
    boolean refreshTaskCache(String taskId);

    /**
     * 获取任务执行统计
     * 
     * @param taskId 任务ID
     * @return 执行统计
     */
    Map<String, Object> getTaskExecutionStats(String taskId);

    /**
     * 获取任务时间线
     * 
     * @param taskId 任务ID
     * @return 时间线数据
     */
    List<Map<String, Object>> getTaskTimeline(String taskId);

    /**
     * 获取任务依赖关系
     * 
     * @param taskId 任务ID
     * @return 依赖关系
     */
    List<Map<String, Object>> getTaskDependencies(String taskId);

    /**
     * 获取任务影响范围
     * 
     * @param taskId 任务ID
     * @return 影响范围
     */
    List<Map<String, Object>> getTaskImpacts(String taskId);
}
