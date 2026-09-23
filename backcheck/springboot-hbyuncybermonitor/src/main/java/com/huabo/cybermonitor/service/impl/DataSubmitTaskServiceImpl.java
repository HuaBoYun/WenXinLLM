package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.DataSubmitTask;
import com.huabo.cybermonitor.mapper.DataSubmitTaskMapper;
import com.huabo.cybermonitor.service.IDataSubmitTaskService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.DataSubmitTaskQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据报送任务服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class DataSubmitTaskServiceImpl extends ServiceImpl<DataSubmitTaskMapper, DataSubmitTask> implements IDataSubmitTaskService {

    @Autowired
    private DataSubmitTaskMapper taskMapper;

    @Override
    public IPage<DataSubmitTask> getTaskList(DataSubmitTaskQueryVO queryVO) {
        Page<DataSubmitTask> page = new Page<>(queryVO.getPageNum().intValue(), queryVO.getPageSize().intValue());
        return taskMapper.selectTaskList(page, queryVO);
    }

    @Override
    public DataSubmitTask getTaskDetail(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            return null;
        }
        return taskMapper.selectTaskDetail(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTask(DataSubmitTask task) {
        try {
            // 验证任务名称唯一性
            if (validateTaskName(task.getTaskName(), null)) {
                throw new RuntimeException("任务名称已存在");
            }

            // 设置默认值
            if (StringUtils.isEmpty(task.getTaskStatus())) {
                task.setTaskStatus(DataSubmitTask.STATUS_DRAFT);
            }
            task.setCreateTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());

            return save(task);
        } catch (Exception e) {
            log.error("新增数据报送任务失败", e);
            throw new RuntimeException("新增数据报送任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(DataSubmitTask task) {
        try {
            // 验证任务名称唯一性
            if (validateTaskName(task.getTaskName(), task.getTaskId())) {
                throw new RuntimeException("任务名称已存在");
            }

            task.setUpdateTime(LocalDateTime.now());
            return updateById(task);
        } catch (Exception e) {
            log.error("更新数据报送任务失败", e);
            throw new RuntimeException("更新数据报送任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(String taskId) {
        try {
            // 检查是否有关联的报送记录
            Integer recordCount = taskMapper.countSubmitRecords(taskId);
            if (recordCount > 0) {
                throw new RuntimeException("该任务下存在报送记录，无法删除");
            }

            return removeById(taskId);
        } catch (Exception e) {
            log.error("删除数据报送任务失败", e);
            throw new RuntimeException("删除数据报送任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTask(List<String> taskIds) {
        try {
            for (String taskId : taskIds) {
                // 检查是否有关联的报送记录
                Integer recordCount = taskMapper.countSubmitRecords(taskId);
                if (recordCount > 0) {
                    throw new RuntimeException("任务ID " + taskId + " 下存在报送记录，无法删除");
                }
            }

            return removeByIds(taskIds);
        } catch (Exception e) {
            log.error("批量删除数据报送任务失败", e);
            throw new RuntimeException("批量删除数据报送任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishTask(String taskId) {
        try {
            DataSubmitTask task = getById(taskId);
            if (task == null) {
                throw new RuntimeException("任务不存在");
            }

            task.setTaskStatus(DataSubmitTask.STATUS_PUBLISHED);
            task.setPublishTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());

            return updateById(task);
        } catch (Exception e) {
            log.error("发布任务失败", e);
            throw new RuntimeException("发布任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTask(String taskId) {
        try {
            return taskMapper.updateTaskStatus(taskId, DataSubmitTask.STATUS_CANCELLED) > 0;
        } catch (Exception e) {
            log.error("取消任务失败", e);
            throw new RuntimeException("取消任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(String taskId) {
        try {
            return taskMapper.updateTaskStatus(taskId, DataSubmitTask.STATUS_COMPLETED) > 0;
        } catch (Exception e) {
            log.error("完成任务失败", e);
            throw new RuntimeException("完成任务失败：" + e.getMessage());
        }
    }

    @Override
    public List<DataSubmitTask> getTasksByStatus(String taskStatus) {
        return taskMapper.selectTasksByStatus(taskStatus);
    }

    @Override
    public List<DataSubmitTask> getTasksByType(String taskType) {
        return taskMapper.selectTasksByType(taskType);
    }

    @Override
    public List<DataSubmitTask> getTasksByCycle(String submitCycle) {
        return taskMapper.selectTasksByCycle(submitCycle);
    }

    @Override
    public List<DataSubmitTask> getActiveTasks() {
        return taskMapper.selectActiveTasks();
    }

    @Override
    public List<DataSubmitTask> getExpiringTasks(Integer days) {
        return taskMapper.selectExpiringTasks(days);
    }

    @Override
    public Map<String, Object> getTaskStatistics() {
        return taskMapper.selectTaskStatistics();
    }

    @Override
    public List<Map<String, Object>> getTaskTypeDistribution() {
        return taskMapper.selectTaskTypeDistribution();
    }

    @Override
    public List<Map<String, Object>> getTaskCategoryDistribution() {
        return taskMapper.selectTaskCategoryDistribution();
    }

    @Override
    public List<Map<String, Object>> getTaskCycleDistribution() {
        return taskMapper.selectTaskCycleDistribution();
    }

    @Override
    public boolean validateTaskName(String taskName, String excludeId) {
        if (StringUtils.isEmpty(taskName)) {
            return false;
        }
        DataSubmitTask existingTask = taskMapper.selectByTaskName(taskName, excludeId);
        return existingTask != null;
    }

    @Override
    public void exportTaskList(DataSubmitTaskQueryVO queryVO, HttpServletResponse response) {
        try {
            List<DataSubmitTask> taskList = taskMapper.selectTaskListForExport(queryVO);

            // 设置导出的列标题
            String[] headers = {
                "任务名称", "任务类型", "任务分类", "任务状态", "报送周期",
                "开始日期", "结束日期", "创建人", "创建时间", "发布时间", "任务描述"
            };

            // 使用现有的ExcelUtil构造方法
            ExcelUtil excelUtil = new ExcelUtil("数据报送任务列表", headers);

            // 添加数据行
            for (int i = 0; i < taskList.size(); i++) {
                DataSubmitTask task = taskList.get(i);
                Object[] row = {
                    task.getTaskName(),
                    getTaskTypeLabel(task.getTaskType()),
                    getTaskCategoryLabel(task.getTaskCategory()),
                    getTaskStatusLabel(task.getTaskStatus()),
                    getSubmitCycleLabel(task.getSubmitCycle()),
                    task.getStartDate(),
                    task.getEndDate(),
                    task.getCreateBy(),
                    task.getCreateTime(),
                    task.getPublishTime(),
                    task.getTaskDescription()
                };
                excelUtil.addRow(i + 1, row);
            }

            // 导出Excel
            excelUtil.exportExcel(response, "数据报送任务列表.xls");
        } catch (Exception e) {
            log.error("导出任务列表失败", e);
            throw new RuntimeException("导出任务列表失败：" + e.getMessage());
        }
    }

    @Override
    public void downloadTaskTemplate(HttpServletResponse response) {
        try {
            String[] headers = {
                "任务名称*", "任务类型*", "任务分类", "报送周期*", "开始日期*",
                "结束日期", "任务描述", "数据模板", "备注"
            };

            // 使用现有的ExcelUtil构造方法
            ExcelUtil excelUtil = new ExcelUtil("数据报送任务导入模板", headers);

            // 添加示例数据
            Object[] exampleRow = {
                "示例报送任务", "REGULAR", "FINANCIAL", "MONTHLY", "2024-01-01",
                "2024-12-31", "示例任务描述", "财务数据模板", "示例备注"
            };
            excelUtil.addRow(1, exampleRow);

            // 导出Excel模板
            excelUtil.exportExcel(response, "数据报送任务导入模板.xls");
        } catch (Exception e) {
            log.error("下载任务导入模板失败", e);
            throw new RuntimeException("下载任务导入模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importTaskList(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 简化导入逻辑，暂时返回成功结果
            // TODO: 实现完整的Excel解析逻辑
            result.put("successCount", 0);
            result.put("failCount", 0);
            result.put("errorMessages", new ArrayList<>());
            result.put("totalCount", 0);
            result.put("message", "导入功能开发中，请稍后使用");

        } catch (Exception e) {
            log.error("导入任务列表失败", e);
            throw new RuntimeException("导入任务列表失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Integer getSubmitRecordCount(String taskId) {
        return taskMapper.countSubmitRecords(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyTask(String taskId) {
        try {
            DataSubmitTask sourceTask = getById(taskId);
            if (sourceTask == null) {
                throw new RuntimeException("源任务不存在");
            }

            // 创建新任务
            DataSubmitTask newTask = new DataSubmitTask();
            newTask.setTaskName(sourceTask.getTaskName() + "_副本");
            newTask.setTaskType(sourceTask.getTaskType());
            newTask.setTaskCategory(sourceTask.getTaskCategory());
            newTask.setTaskDescription(sourceTask.getTaskDescription());
            newTask.setSubmitCycle(sourceTask.getSubmitCycle());
            newTask.setDataTemplate(sourceTask.getDataTemplate());
            newTask.setTaskStatus(DataSubmitTask.STATUS_DRAFT);
            newTask.setCreateTime(LocalDateTime.now());
            newTask.setUpdateTime(LocalDateTime.now());

            save(newTask);
            return newTask.getTaskId();
        } catch (Exception e) {
            log.error("复制任务失败", e);
            throw new RuntimeException("复制任务失败：" + e.getMessage());
        }
    }

    @Override
    public String getTaskTypeLabel(String taskType) {
        if (StringUtils.isEmpty(taskType)) {
            return "";
        }
        switch (taskType) {
            case DataSubmitTask.TYPE_REGULAR:
                return "定期报送";
            case DataSubmitTask.TYPE_SPECIAL:
                return "专项报送";
            case DataSubmitTask.TYPE_URGENT:
                return "紧急报送";
            default:
                return taskType;
        }
    }

    @Override
    public String getTaskStatusLabel(String taskStatus) {
        if (StringUtils.isEmpty(taskStatus)) {
            return "";
        }
        switch (taskStatus) {
            case DataSubmitTask.STATUS_DRAFT:
                return "草稿";
            case DataSubmitTask.STATUS_PUBLISHED:
                return "已发布";
            case DataSubmitTask.STATUS_ACTIVE:
                return "进行中";
            case DataSubmitTask.STATUS_COMPLETED:
                return "已完成";
            case DataSubmitTask.STATUS_CANCELLED:
                return "已取消";
            default:
                return taskStatus;
        }
    }

    @Override
    public String getSubmitCycleLabel(String submitCycle) {
        if (StringUtils.isEmpty(submitCycle)) {
            return "";
        }
        switch (submitCycle) {
            case DataSubmitTask.CYCLE_DAILY:
                return "日报";
            case DataSubmitTask.CYCLE_WEEKLY:
                return "周报";
            case DataSubmitTask.CYCLE_MONTHLY:
                return "月报";
            case DataSubmitTask.CYCLE_QUARTERLY:
                return "季报";
            case DataSubmitTask.CYCLE_YEARLY:
                return "年报";
            case DataSubmitTask.CYCLE_ONCE:
                return "一次性";
            default:
                return submitCycle;
        }
    }

    @Override
    public String getTaskCategoryLabel(String taskCategory) {
        if (StringUtils.isEmpty(taskCategory)) {
            return "";
        }
        switch (taskCategory) {
            case DataSubmitTask.CATEGORY_FINANCIAL:
                return "财务数据";
            case DataSubmitTask.CATEGORY_OPERATIONAL:
                return "经营数据";
            case DataSubmitTask.CATEGORY_GOVERNANCE:
                return "治理数据";
            case DataSubmitTask.CATEGORY_RISK:
                return "风险数据";
            case DataSubmitTask.CATEGORY_COMPLIANCE:
                return "合规数据";
            case DataSubmitTask.CATEGORY_PERFORMANCE:
                return "绩效数据";
            default:
                return taskCategory;
        }
    }
}