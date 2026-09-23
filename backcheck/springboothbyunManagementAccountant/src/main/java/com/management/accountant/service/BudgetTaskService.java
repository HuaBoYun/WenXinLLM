package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetTask;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算任务管理Service接口
 * 
 * @description 预算任务管理业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetTaskService {

    /**
     * 创建任务
     * 
     * @param task 任务对象
     * @return 创建后的任务
     */
    BudgetTask create(BudgetTask task);

    /**
     * 根据ID查询任务
     * 
     * @param taskId 任务ID
     * @return 任务对象
     */
    BudgetTask getById(String taskId);

    /**
     * 更新任务
     * 
     * @param task 任务对象
     * @return 更新后的任务
     */
    BudgetTask update(BudgetTask task);

    /**
     * 删除任务
     * 
     * @param taskId 任务ID
     */
    void delete(String taskId);

    /**
     * 分页查询任务列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetTask> getPage(Map<String, Object> params);

    /**
     * 批量删除任务
     * 
     * @param ids ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 分配任务
     * 
     * @param taskId 任务ID
     * @param assigneeId 分配人ID
     * @param assigneeName 分配人姓名
     */
    void assign(String taskId, String assigneeId, String assigneeName);

    /**
     * 启动任务
     * 
     * @param taskId 任务ID
     */
    void start(String taskId);

    /**
     * 暂停任务
     * 
     * @param taskId 任务ID
     */
    void pause(String taskId);

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     */
    void complete(String taskId);

    /**
     * 取消任务
     * 
     * @param taskId 任务ID
     */
    void cancel(String taskId);

    /**
     * 更新任务进度
     * 
     * @param taskId 任务ID
     * @param progress 进度(0-100)
     */
    void updateProgress(String taskId, Integer progress);

    /**
     * 获取我的任务列表
     * 
     * @param userId 用户ID
     * @param params 查询参数
     * @return 任务列表
     */
    List<BudgetTask> getMyTasks(String userId, Map<String, Object> params);

    /**
     * 获取任务统计信息
     * 
     * @param params 查询参数
     * @return 统计信息
     */
    Map<String, Object> getStatistics(Map<String, Object> params);

    /**
     * 检查任务编码是否存在
     *
     * @param code 任务编码
     * @return true-存在，false-不存在
     */
    boolean checkCodeExists(String code);

    /**
     * 提交任务审批
     *
     * @param taskId 任务ID
     * @param comment 提交说明
     */
    void submit(String taskId, String comment);

    /**
     * 审批任务通过
     *
     * @param taskId 任务ID
     * @param comment 审批意见
     */
    void approve(String taskId, String comment);

    /**
     * 审批任务拒绝
     *
     * @param taskId 任务ID
     * @param comment 拒绝原因
     */
    void reject(String taskId, String comment);

    /**
     * 复制任务
     *
     * @param taskId 任务ID
     * @return 复制后的任务
     */
    BudgetTask copy(String taskId);

    /**
     * 启用任务
     *
     * @param taskId 任务ID
     */
    void enable(String taskId);

    /**
     * 禁用任务
     *
     * @param taskId 任务ID
     */
    void disable(String taskId);

    /**
     * 获取我创建的任务列表
     *
     * @param params 查询参数
     * @return 任务列表
     */
    List<BudgetTask> getMyCreatedTasks(Map<String, Object> params);
}

