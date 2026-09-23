package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetTask;
import com.management.accountant.oracle.mapper.budget.BudgetTaskMapper;
import com.management.accountant.service.BudgetTaskService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 预算任务管理Service实现类
 * 
 * @description 预算任务管理业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetTaskServiceImpl implements BudgetTaskService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetTaskMapper taskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetTask create(BudgetTask task) {
        // 1. 参数校验
        if (task == null) {
            throw new ServiceException("任务信息不能为空");
        }
        if (!StringUtils.hasText(task.getTaskCode())) {
            throw new ServiceException("任务编码不能为空");
        }
        if (!StringUtils.hasText(task.getTaskName())) {
            throw new ServiceException("任务名称不能为空");
        }

        // 2. 检查编码是否重复
        if (checkCodeExists(task.getTaskCode())) {
            throw new ServiceException("任务编码已存在");
        }

        // 3. 设置默认值
        if (task.getDelFlag() == null) {
            task.setDelFlag(0);
        }
        if (task.getIsEnabled() == null) {
            task.setIsEnabled(1);
        }
        if (task.getTaskStatus() == null) {
            task.setTaskStatus("PENDING");
        }
        if (task.getProgress() == null) {
            task.setProgress(BigDecimal.ZERO);
        }
        if (task.getPriority() == null) {
            task.setPriority("MEDIUM");
        }
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = taskMapper.insert(task);
        if (result <= 0) {
            throw new ServiceException("创建任务失败");
        }

        log.info("创建任务成功，ID: {}, 编码: {}", task.getTaskId(), task.getTaskCode());
        return task;
    }

    @Override
    public BudgetTask getById(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        
        QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
        wrapper.eq("TASK_ID", taskId)
               .eq("DEL_FLAG", 0);
        
        return taskMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetTask update(BudgetTask task) {
        // 1. 参数校验
        if (task == null || !StringUtils.hasText(task.getTaskId())) {
            throw new ServiceException("任务ID不能为空");
        }

        // 2. 检查是否存在
        BudgetTask existing = getById(task.getTaskId());
        if (existing == null) {
            throw new ServiceException("任务不存在");
        }

        // 3. 如果修改了编码，检查新编码是否重复
        if (StringUtils.hasText(task.getTaskCode()) 
                && !task.getTaskCode().equals(existing.getTaskCode())) {
            if (checkCodeExists(task.getTaskCode())) {
                throw new ServiceException("任务编码已存在");
            }
        }

        // 4. 更新时间
        task.setUpdateTime(new Date());

        // 5. 更新数据库
        int result = taskMapper.updateById(task);
        if (result <= 0) {
            throw new ServiceException("更新任务失败");
        }

        log.info("更新任务成功，ID: {}", task.getTaskId());
        return getById(task.getTaskId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        // 检查是否存在
        BudgetTask existing = getById(taskId);
        if (existing == null) {
            throw new ServiceException("任务不存在");
        }

        // 逻辑删除
        BudgetTask task = new BudgetTask();
        task.setTaskId(taskId);
        task.setDelFlag(1);
        task.setUpdateTime(new Date());

        int result = taskMapper.updateById(task);
        if (result <= 0) {
            throw new ServiceException("删除任务失败");
        }

        log.info("删除任务成功，ID: {}", taskId);
    }

    @Override
    public PageResult<BudgetTask> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 任务名称模糊查询
        if (hasValue(params.get("taskName"))) {
            wrapper.like("TASK_NAME", params.get("taskName"));
        }

        // 任务类型
        if (hasValue(params.get("taskType"))) {
            wrapper.eq("TASK_TYPE", params.get("taskType"));
        }

        // 任务状态
        if (hasValue(params.get("taskStatus"))) {
            wrapper.eq("TASK_STATUS", params.get("taskStatus"));
        }

        // 优先级
        if (hasValue(params.get("priority"))) {
            wrapper.eq("PRIORITY", params.get("priority"));
        }

        // 分配人
        if (hasValue(params.get("assigneeId"))) {
            wrapper.eq("ASSIGNEE_ID", params.get("assigneeId"));
        }

        // 预算年度
        if (hasValue(params.get("budgetYear"))) {
            wrapper.eq("BUDGET_YEAR", params.get("budgetYear"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetTask> page = new Page<>(pageNum, pageSize);
        IPage<BudgetTask> pageResult = taskMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetTask> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的数据");
        }

        for (String id : ids) {
            delete(id);
        }

        log.info("批量删除任务成功，数量: {}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assign(String taskId, String assigneeId, String assigneeName) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        if (!StringUtils.hasText(assigneeId)) {
            throw new ServiceException("分配人ID不能为空");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setAssigneeId(assigneeId);
        update.setAssigneeName(assigneeName);
        update.setUpdateTime(new Date());

        taskMapper.updateById(update);
        log.info("分配任务成功，任务ID: {}, 分配给: {}", taskId, assigneeName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void start(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        if (!"PENDING".equals(task.getTaskStatus()) && !"PAUSED".equals(task.getTaskStatus())) {
            throw new ServiceException("只有待开始或已暂停的任务才能启动");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setTaskStatus("IN_PROGRESS");
        update.setUpdateTime(new Date());

        taskMapper.updateById(update);
        log.info("启动任务成功，ID: {}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        if (!"IN_PROGRESS".equals(task.getTaskStatus())) {
            throw new ServiceException("只有进行中的任务才能暂停");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setTaskStatus("PAUSED");
        update.setUpdateTime(new Date());

        taskMapper.updateById(update);
        log.info("暂停任务成功，ID: {}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setTaskStatus("COMPLETED");
        update.setProgress(new BigDecimal("100"));
        update.setActualCompletionDate(new Date());
        update.setUpdateTime(new Date());

        taskMapper.updateById(update);
        log.info("完成任务成功，ID: {}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setTaskStatus("CANCELLED");
        update.setUpdateTime(new Date());

        taskMapper.updateById(update);
        log.info("取消任务成功，ID: {}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProgress(String taskId, Integer progress) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        if (progress == null || progress < 0 || progress > 100) {
            throw new ServiceException("进度值必须在0-100之间");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setProgress(new BigDecimal(progress));
        update.setUpdateTime(new Date());

        // 如果进度达到100%，自动完成任务
        if (progress == 100) {
            update.setTaskStatus("COMPLETED");
            update.setActualCompletionDate(new Date());
        }

        taskMapper.updateById(update);
        log.info("更新任务进度成功，ID: {}, 进度: {}%", taskId, progress);
    }

    @Override
    public List<BudgetTask> getMyTasks(String userId, Map<String, Object> params) {
        if (!StringUtils.hasText(userId)) {
            throw new ServiceException("用户ID不能为空");
        }

        QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0)
               .eq("ASSIGNEE_ID", userId);

        // 任务状态
        if (params != null && params.get("taskStatus") != null) {
            wrapper.eq("TASK_STATUS", params.get("taskStatus"));
        }

        wrapper.orderByDesc("CREATE_TIME");
        return taskMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getStatistics(Map<String, Object> params) {
        Map<String, Object> statistics = new HashMap<>();

        QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 总任务数
        Integer totalCount = taskMapper.selectCount(wrapper).intValue();
        statistics.put("totalCount", totalCount);

        // 待开始
        QueryWrapper<BudgetTask> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("DEL_FLAG", 0).eq("TASK_STATUS", "PENDING");
        Integer pendingCount = taskMapper.selectCount(pendingWrapper).intValue();
        statistics.put("pendingCount", pendingCount);

        // 进行中
        QueryWrapper<BudgetTask> inProgressWrapper = new QueryWrapper<>();
        inProgressWrapper.eq("DEL_FLAG", 0).eq("TASK_STATUS", "IN_PROGRESS");
        Integer inProgressCount = taskMapper.selectCount(inProgressWrapper).intValue();
        statistics.put("inProgressCount", inProgressCount);

        // 已完成
        QueryWrapper<BudgetTask> completedWrapper = new QueryWrapper<>();
        completedWrapper.eq("DEL_FLAG", 0).eq("TASK_STATUS", "COMPLETED");
        Integer completedCount = taskMapper.selectCount(completedWrapper).intValue();
        statistics.put("completedCount", completedCount);

        // 已暂停
        QueryWrapper<BudgetTask> pausedWrapper = new QueryWrapper<>();
        pausedWrapper.eq("DEL_FLAG", 0).eq("TASK_STATUS", "PAUSED");
        Integer pausedCount = taskMapper.selectCount(pausedWrapper).intValue();
        statistics.put("pausedCount", pausedCount);

        // 已取消
        QueryWrapper<BudgetTask> cancelledWrapper = new QueryWrapper<>();
        cancelledWrapper.eq("DEL_FLAG", 0).eq("TASK_STATUS", "CANCELLED");
        Integer cancelledCount = taskMapper.selectCount(cancelledWrapper).intValue();
        statistics.put("cancelledCount", cancelledCount);

        return statistics;
    }

    @Override
    public boolean checkCodeExists(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }

        QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
        wrapper.eq("TASK_CODE", code)
               .eq("DEL_FLAG", 0);

        return taskMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(String taskId, String comment) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        if (!"COMPLETED".equals(task.getTaskStatus())) {
            throw new ServiceException("只有已完成的任务才能提交审批");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setTaskStatus("PENDING_APPROVAL");
        update.setUpdateTime(new Date());

        taskMapper.updateById(update);
        log.info("提交任务审批成功，ID: {}, 说明: {}", taskId, comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(String taskId, String comment) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        if (!"PENDING_APPROVAL".equals(task.getTaskStatus())) {
            throw new ServiceException("只有待审批的任务才能审批");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setTaskStatus("APPROVED");
        update.setUpdateTime(new Date());

        taskMapper.updateById(update);
        log.info("审批任务通过，ID: {}, 意见: {}", taskId, comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(String taskId, String comment) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        if (!StringUtils.hasText(comment)) {
            throw new ServiceException("拒绝原因不能为空");
        }

        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        if (!"PENDING_APPROVAL".equals(task.getTaskStatus())) {
            throw new ServiceException("只有待审批的任务才能拒绝");
        }

        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setTaskStatus("REJECTED");
        update.setUpdateTime(new Date());

        taskMapper.updateById(update);
        log.info("拒绝任务，ID: {}, 原因: {}", taskId, comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetTask copy(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }

        BudgetTask source = getById(taskId);
        if (source == null) {
            throw new ServiceException("任务不存在");
        }

        // 创建新任务
        BudgetTask copy = new BudgetTask();
        copy.setTaskId(UUID.randomUUID().toString().replace("-", ""));
        copy.setTaskCode("TASK-" + System.currentTimeMillis());
        copy.setTaskName(source.getTaskName() + "-副本");
        copy.setTaskType(source.getTaskType());
        copy.setDescription(source.getDescription());
        copy.setBudgetYear(source.getBudgetYear());
        copy.setStartDate(source.getStartDate());
        copy.setDueDate(source.getDueDate());
        copy.setPriority(source.getPriority());
        copy.setTaskStatus("PENDING");
        copy.setProgress(new BigDecimal("0"));
        copy.setIsEnabled(1);
        copy.setDelFlag(0);
        copy.setCreateTime(new Date());
        copy.setUpdateTime(new Date());

        taskMapper.insert(copy);
        log.info("复制任务成功，源任务ID: {}, 新任务ID: {}", taskId, copy.getTaskId());

        return copy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setIsEnabled(1);
        update.setUpdateTime(new Date());
        taskMapper.updateById(update);
        log.info("启用任务成功，ID: {}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        BudgetTask task = getById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        BudgetTask update = new BudgetTask();
        update.setTaskId(taskId);
        update.setIsEnabled(0);
        update.setUpdateTime(new Date());
        taskMapper.updateById(update);
        log.info("禁用任务成功，ID: {}", taskId);
    }

    @Override
    public List<BudgetTask> getMyCreatedTasks(Map<String, Object> params) {
        QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);
        if (params != null && params.get("creatorId") != null) {
            wrapper.eq("CREATOR_ID", params.get("creatorId"));
        }
        if (params != null && params.get("taskStatus") != null) {
            wrapper.eq("TASK_STATUS", params.get("taskStatus"));
        }
        wrapper.orderByDesc("CREATE_TIME");
        return taskMapper.selectList(wrapper);
    }

    private boolean hasValue(Object val) {
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }
}

