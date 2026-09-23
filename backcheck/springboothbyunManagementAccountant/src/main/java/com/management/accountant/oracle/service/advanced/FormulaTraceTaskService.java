package com.management.accountant.oracle.service.advanced;

import com.management.accountant.oracle.entity.advanced.FormulaTraceTask;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 公式追踪任务Service接口
 * 
 * @author AI Agent
 * @date 2026-02-06
 */
public interface FormulaTraceTaskService {

    /**
     * 获取公式追踪统计数据
     * 
     * @param companyId 公司ID
     * @return 统计数据
     */
    Map<String, Object> getStats(String companyId);

    /**
     * 获取公式追踪任务列表（分页）
     * 
     * @param keyword 关键字
     * @param status 状态
     * @param companyId 公司ID
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    PageResult<FormulaTraceTask> getTaskList(String keyword, String status, String companyId, Integer pageNo, Integer pageSize);

    /**
     * 获取可用公式列表
     * 
     * @param companyId 公司ID
     * @return 公式列表
     */
    List<Map<String, Object>> getAvailableFormulas(String companyId);

    /**
     * 创建公式追踪任务
     * 
     * @param task 任务对象
     * @param companyId 公司ID
     * @param userId 用户ID
     * @param userName 用户名称
     * @return 任务ID
     */
    String createTask(FormulaTraceTask task, String companyId, String userId, String userName);

    /**
     * 更新公式追踪任务
     * 
     * @param taskId 任务ID
     * @param task 任务对象
     * @param userId 用户ID
     * @param userName 用户名称
     * @return 是否成功
     */
    boolean updateTask(String taskId, FormulaTraceTask task, String userId, String userName);

    /**
     * 删除公式追踪任务
     * 
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean deleteTask(String taskId);

    /**
     * 批量删除公式追踪任务
     * 
     * @param taskIds 任务ID列表
     * @return 是否成功
     */
    boolean batchDeleteTasks(List<String> taskIds);

    /**
     * 执行公式追踪任务
     * 
     * @param taskId 任务ID
     * @param companyId 公司ID
     * @return 执行结果
     */
    Map<String, Object> executeTask(String taskId, String companyId);

    /**
     * 根据ID查询任务
     * 
     * @param taskId 任务ID
     * @return 任务对象
     */
    FormulaTraceTask getTaskById(String taskId);
}

