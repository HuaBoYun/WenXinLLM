package com.management.accountant.service.ncv65.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ncv65.BudgetTask;
import com.management.accountant.mapper.ncv65.BudgetTaskMapper;
import com.management.accountant.service.ncv65.IBudgetTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * NCV65全面预算系统 - 预算任务服务实现类
 * 
 * @description 预算任务业务逻辑实现，支持任务的完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Slf4j
@Service
public class BudgetTaskServiceImpl extends ServiceImpl<BudgetTaskMapper, BudgetTask> 
        implements IBudgetTaskService {

    @Resource
    private BudgetTaskMapper budgetTaskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTask(BudgetTask task) {
        try {
            log.info("开始创建预算任务：{}", task.getTaskName());
            
            // 1. 业务验证
            if (checkTaskCodeExists(task.getTaskCode(), null)) {
                throw new RuntimeException("任务编码已存在：" + task.getTaskCode());
            }
            
            // 2. 设置默认值
            if (task.getIsEnabled() == null) {
                task.setIsEnabled(true);
            }
            if (!StringUtils.hasText(task.getTaskStatus())) {
                task.setTaskStatus(BudgetTask.TASK_STATUS_DRAFT);
            }
            if (!StringUtils.hasText(task.getStatus())) {
                task.setStatus(BudgetTask.STATUS_ACTIVE);
            }
            if (task.getProgress() == null) {
                task.setProgress(0);
            }
            
            // 3. 设置任务优先级
            if (!StringUtils.hasText(task.getPriority())) {
                task.setPriority(BudgetTask.PRIORITY_MEDIUM);
            }
            
            // 4. 保存任务
            boolean result = save(task);
            
            if (result) {
                log.info("创建预算任务成功：{}", task.getTaskName());
            } else {
                log.error("创建预算任务失败：{}", task.getTaskName());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("创建预算任务异常：{}", e.getMessage(), e);
            throw new RuntimeException("创建预算任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(BudgetTask task) {
        try {
            log.info("开始更新预算任务：{}", task.getId());
            
            // 1. 检查任务是否存在
            BudgetTask existingTask = getById(task.getId());
            if (existingTask == null) {
                throw new RuntimeException("任务不存在");
            }
            
            // 2. 检查编码唯一性
            if (!existingTask.getTaskCode().equals(task.getTaskCode())) {
                if (checkTaskCodeExists(task.getTaskCode(), task.getId())) {
                    throw new RuntimeException("任务编码已存在：" + task.getTaskCode());
                }
            }
            
            // 3. 检查任务状态变更的合法性
            if (!isValidStatusTransition(existingTask.getTaskStatus(), task.getTaskStatus())) {
                throw new RuntimeException("任务状态变更不合法");
            }
            
            // 4. 更新任务
            boolean result = updateById(task);
            
            if (result) {
                log.info("更新预算任务成功：{}", task.getId());
            } else {
                log.error("更新预算任务失败：{}", task.getId());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("更新预算任务异常：{}", e.getMessage(), e);
            throw new RuntimeException("更新预算任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(String id) {
        try {
            log.info("开始删除预算任务：{}", id);
            
            // 1. 检查任务状态
            BudgetTask task = getById(id);
            if (task != null && BudgetTask.TASK_STATUS_IN_PROGRESS.equals(task.getTaskStatus())) {
                throw new RuntimeException("进行中的任务无法删除");
            }
            
            // 2. 删除任务
            boolean result = removeById(id);
            
            if (result) {
                log.info("删除预算任务成功：{}", id);
            } else {
                log.error("删除预算任务失败：{}", id);
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("删除预算任务异常：{}", e.getMessage(), e);
            throw new RuntimeException("删除预算任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTasks(List<String> ids) {
        try {
            log.info("开始批量删除预算任务：{}", ids);
            
            for (String id : ids) {
                deleteTask(id);
            }
            
            log.info("批量删除预算任务成功");
            return true;
            
        } catch (Exception e) {
            log.error("批量删除预算任务异常：{}", e.getMessage(), e);
            throw new RuntimeException("批量删除预算任务失败：" + e.getMessage());
        }
    }

    @Override
    public BudgetTask getTaskById(String id) {
        return getById(id);
    }

    @Override
    public BudgetTask getTaskByCode(String taskCode) {
        return budgetTaskMapper.selectByTaskCode(taskCode, getCurrentTenantId());
    }

    @Override
    public IPage<BudgetTask> getTaskPage(Long current, Long size, Map<String, Object> params) {
        Page<BudgetTask> page = new Page<>(current, size);
        
        // 添加租户ID
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("tenantId", getCurrentTenantId());
        
        return budgetTaskMapper.selectPageWithConditions(page, params);
    }

    @Override
    public List<BudgetTask> getTasksByType(String taskType) {
        return budgetTaskMapper.selectByTaskType(taskType, getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getTasksByStatus(String taskStatus) {
        return budgetTaskMapper.selectByTaskStatus(taskStatus, getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getEnabledTasks() {
        return budgetTaskMapper.selectEnabledTasks(getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getTasksByBudgetYear(Integer budgetYear) {
        return budgetTaskMapper.selectByBudgetYear(budgetYear, getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getTasksByStructureId(String structureId) {
        return budgetTaskMapper.selectByStructureId(structureId, getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getPendingApprovalTasks() {
        return budgetTaskMapper.selectPendingApprovalTasks(getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getCompletedTasks() {
        return budgetTaskMapper.selectCompletedTasks(getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getOverdueTasks() {
        return budgetTaskMapper.selectOverdueTasks(getCurrentTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableTask(String id) {
        BudgetTask task = new BudgetTask();
        task.setId(id);
        task.setIsEnabled(true);
        task.setUpdateTime(LocalDateTime.now());
        return updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableTask(String id) {
        BudgetTask task = new BudgetTask();
        task.setId(id);
        task.setIsEnabled(false);
        task.setUpdateTime(LocalDateTime.now());
        return updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateTaskStatus(List<String> ids, String taskStatus) {
        return budgetTaskMapper.batchUpdateTaskStatus(ids, taskStatus, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateEnabled(List<String> ids, Boolean isEnabled) {
        return budgetTaskMapper.batchUpdateEnabled(ids, isEnabled, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetTask copyTask(String id, String targetName, String targetCode) {
        try {
            log.info("开始复制预算任务：{}", id);
            
            // 1. 获取源任务
            BudgetTask sourceTask = getById(id);
            if (sourceTask == null) {
                throw new RuntimeException("源任务不存在");
            }
            
            // 2. 检查目标编码是否存在
            if (checkTaskCodeExists(targetCode, null)) {
                throw new RuntimeException("目标任务编码已存在：" + targetCode);
            }
            
            // 3. 创建新任务
            BudgetTask newTask = new BudgetTask();
            // 复制属性（排除ID、编码、名称、创建时间等）
            newTask.setTaskCode(targetCode);
            newTask.setTaskName(targetName);
            newTask.setTaskType(sourceTask.getTaskType());
            newTask.setDescription(sourceTask.getDescription());
            newTask.setBudgetYear(sourceTask.getBudgetYear());
            newTask.setStructureId(sourceTask.getStructureId());
            newTask.setWorkflowId(sourceTask.getWorkflowId());
            newTask.setPriority(sourceTask.getPriority());
            newTask.setStartDate(sourceTask.getStartDate());
            newTask.setEndDate(sourceTask.getEndDate());
            newTask.setIsEnabled(true);
            newTask.setTaskStatus(BudgetTask.TASK_STATUS_DRAFT);
            newTask.setProgress(0);
            newTask.setStatus(BudgetTask.STATUS_ACTIVE);
            newTask.setRemark("复制自：" + sourceTask.getTaskName());
            
            // 4. 保存新任务
            boolean result = createTask(newTask);
            
            if (result) {
                log.info("复制预算任务成功：{} -> {}", sourceTask.getTaskName(), targetName);
                return newTask;
            } else {
                throw new RuntimeException("保存新任务失败");
            }
            
        } catch (Exception e) {
            log.error("复制预算任务异常：{}", e.getMessage(), e);
            throw new RuntimeException("复制预算任务失败：" + e.getMessage());
        }
    }

    @Override
    public boolean checkTaskCodeExists(String taskCode, String excludeId) {
        return budgetTaskMapper.checkTaskCodeExists(taskCode, excludeId, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTask(String id) {
        return budgetTaskMapper.startTask(id, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(String id) {
        return budgetTaskMapper.completeTask(id, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTask(String id) {
        return budgetTaskMapper.cancelTask(id, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetTask(String id) {
        return budgetTaskMapper.resetTask(id, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignTask(String id, String assigneeId, String assigneeName) {
        return budgetTaskMapper.assignTask(id, assigneeId, assigneeName, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitForApproval(String id) {
        return budgetTaskMapper.submitForApproval(id, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveTask(String id) {
        return budgetTaskMapper.approveTask(id, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectTask(String id) {
        return budgetTaskMapper.rejectTask(id, getCurrentTenantId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTaskProgress(String id, Integer progress) {
        return budgetTaskMapper.updateTaskProgress(id, progress, getCurrentTenantId()) > 0;
    }

    @Override
    public List<BudgetTask> getMyCreatedTasks(String userId) {
        return budgetTaskMapper.selectMyCreatedTasks(userId, getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getMyAssignedTasks(String userId) {
        return budgetTaskMapper.selectMyAssignedTasks(userId, getCurrentTenantId());
    }

    @Override
    public List<BudgetTask> getMyParticipatedTasks(String userId) {
        return budgetTaskMapper.selectMyParticipatedTasks(userId, getCurrentTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncTaskData(String id) {
        return budgetTaskMapper.syncTaskData(id, "success", getCurrentTenantId()) > 0;
    }

    @Override
    public List<Map<String, Object>> countByTaskType() {
        return budgetTaskMapper.countByTaskType(getCurrentTenantId());
    }

    @Override
    public List<Map<String, Object>> countByTaskStatus() {
        return budgetTaskMapper.countByTaskStatus(getCurrentTenantId());
    }

    @Override
    public List<Map<String, Object>> countByBudgetYear() {
        return budgetTaskMapper.countByBudgetYear(getCurrentTenantId());
    }

    @Override
    public String exportTaskConfig(List<String> ids) {
        // TODO: 实现导出功能
        return "";
    }

    @Override
    public Map<String, Object> importTaskConfig(String filePath) {
        // TODO: 实现导入功能
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> validateTaskConfig(BudgetTask task) {
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("errors", new ArrayList<>());
        return result;
    }

    @Override
    public boolean refreshTaskCache(String taskId) {
        // TODO: 实现缓存刷新
        return true;
    }

    @Override
    public Map<String, Object> getTaskExecutionStats(String taskId) {
        // TODO: 实现执行统计
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> getTaskTimeline(String taskId) {
        // TODO: 实现时间线查询
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getTaskDependencies(String taskId) {
        // TODO: 实现依赖关系查询
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getTaskImpacts(String taskId) {
        // TODO: 实现影响范围查询
        return new ArrayList<>();
    }

    /**
     * 检查任务状态变更的合法性
     */
    private boolean isValidStatusTransition(String fromStatus, String toStatus) {
        if (fromStatus == null || toStatus == null) {
            return true;
        }
        
        // 定义状态转换规则
        Map<String, List<String>> validTransitions = new HashMap<>();
        validTransitions.put(BudgetTask.TASK_STATUS_DRAFT, Arrays.asList(
            BudgetTask.TASK_STATUS_ASSIGNED, BudgetTask.TASK_STATUS_IN_PROGRESS, BudgetTask.TASK_STATUS_CANCELLED));
        validTransitions.put(BudgetTask.TASK_STATUS_ASSIGNED, Arrays.asList(
            BudgetTask.TASK_STATUS_IN_PROGRESS, BudgetTask.TASK_STATUS_CANCELLED));
        validTransitions.put(BudgetTask.TASK_STATUS_IN_PROGRESS, Arrays.asList(
            BudgetTask.TASK_STATUS_PENDING_APPROVAL, BudgetTask.TASK_STATUS_COMPLETED, BudgetTask.TASK_STATUS_CANCELLED));
        validTransitions.put(BudgetTask.TASK_STATUS_PENDING_APPROVAL, Arrays.asList(
            BudgetTask.TASK_STATUS_APPROVED, BudgetTask.TASK_STATUS_REJECTED));
        validTransitions.put(BudgetTask.TASK_STATUS_REJECTED, Arrays.asList(
            BudgetTask.TASK_STATUS_IN_PROGRESS, BudgetTask.TASK_STATUS_CANCELLED));
        
        List<String> allowedStatuses = validTransitions.get(fromStatus);
        return allowedStatuses != null && allowedStatuses.contains(toStatus);
    }

    /**
     * 获取当前租户ID
     */
    private String getCurrentTenantId() {
        // TODO: 从上下文获取租户ID
        return "default";
    }
}
