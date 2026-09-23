package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.DataSubmissionTask;
import com.huabo.cybermonitor.mapper.DataSubmissionTaskMapper;
import com.huabo.cybermonitor.service.IDataSubmissionTaskService;
import com.huabo.cybermonitor.vo.DataSubmissionTaskQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 数据报送任务Service实现类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Slf4j
@Service
public class DataSubmissionTaskServiceImpl extends ServiceImpl<DataSubmissionTaskMapper, DataSubmissionTask> 
    implements IDataSubmissionTaskService {

    @Autowired
    private DataSubmissionTaskMapper dataSubmissionTaskMapper;

    @Override
    public IPage<DataSubmissionTask> selectDataSubmissionTaskPage(Page<DataSubmissionTask> page, DataSubmissionTaskQueryVo queryVo) {
        try {
            return dataSubmissionTaskMapper.selectDataSubmissionTaskPage(page, queryVo);
        } catch (Exception e) {
            log.error("分页查询数据报送任务列表失败", e);
            throw new RuntimeException("分页查询数据报送任务列表失败", e);
        }
    }

    @Override
    public List<DataSubmissionTask> selectDataSubmissionTaskList(DataSubmissionTaskQueryVo queryVo) {
        try {
            return dataSubmissionTaskMapper.selectDataSubmissionTaskList(queryVo);
        } catch (Exception e) {
            log.error("查询数据报送任务列表失败", e);
            throw new RuntimeException("查询数据报送任务列表失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDataSubmissionTask(DataSubmissionTask dataSubmissionTask) {
        try {
            dataSubmissionTask.setTaskId(UUID.randomUUID().toString());
            dataSubmissionTask.setCreateTime(LocalDateTime.now());
            dataSubmissionTask.setUpdateTime(LocalDateTime.now());
            dataSubmissionTask.setDelFlag("0");
            
            // 设置默认值
            if (dataSubmissionTask.getTaskStatus() == null) {
                dataSubmissionTask.setTaskStatus("待开始");
            }
            if (dataSubmissionTask.getCompletionProgress() == null) {
                dataSubmissionTask.setCompletionProgress(BigDecimal.ZERO);
            }
            
            boolean result = save(dataSubmissionTask);
            log.info("新增数据报送任务成功，任务ID：{}", dataSubmissionTask.getTaskId());
            return result;
        } catch (Exception e) {
            log.error("新增数据报送任务失败，任务名称：{}", dataSubmissionTask.getTaskName(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDataSubmissionTask(DataSubmissionTask dataSubmissionTask) {
        try {
            dataSubmissionTask.setUpdateTime(LocalDateTime.now());
            boolean result = updateById(dataSubmissionTask);
            log.info("修改数据报送任务成功，任务ID：{}", dataSubmissionTask.getTaskId());
            return result;
        } catch (Exception e) {
            log.error("修改数据报送任务失败，任务ID：{}", dataSubmissionTask.getTaskId(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDataSubmissionTask(String taskId) {
        try {
            DataSubmissionTask dataSubmissionTask = new DataSubmissionTask();
            dataSubmissionTask.setTaskId(taskId);
            dataSubmissionTask.setDelFlag("1");
            dataSubmissionTask.setUpdateTime(LocalDateTime.now());
            boolean result = updateById(dataSubmissionTask);
            log.info("删除数据报送任务成功，任务ID：{}", taskId);
            return result;
        } catch (Exception e) {
            log.error("删除数据报送任务失败，任务ID：{}", taskId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteDataSubmissionTasks(List<String> taskIds) {
        try {
            int result = dataSubmissionTaskMapper.batchDeleteTasks(taskIds, "system");
            log.info("批量删除数据报送任务成功，删除数量：{}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量删除数据报送任务失败", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getTaskStatisticsByEnterpriseId(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskStatisticsByEnterpriseId(enterpriseId);
        } catch (Exception e) {
            log.error("查询报送任务统计失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询报送任务统计失败", e);
        }
    }

    @Override
    public List<DataSubmissionTask> getUpcomingTasks(String enterpriseId, Integer days) {
        try {
            return dataSubmissionTaskMapper.selectUpcomingTasks(enterpriseId, days);
        } catch (Exception e) {
            log.error("查询即将到期的报送任务失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询即将到期的报送任务失败", e);
        }
    }

    @Override
    public List<DataSubmissionTask> getOverdueTasks(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectOverdueTasks(enterpriseId);
        } catch (Exception e) {
            log.error("查询逾期的报送任务失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询逾期的报送任务失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getTaskCountByStatus(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskCountByStatus(enterpriseId);
        } catch (Exception e) {
            log.error("查询任务状态统计失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询任务状态统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getTaskCountByPriority(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskCountByPriority(enterpriseId);
        } catch (Exception e) {
            log.error("查询任务优先级统计失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询任务优先级统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getTaskCountBySubmissionType(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskCountBySubmissionType(enterpriseId);
        } catch (Exception e) {
            log.error("查询报送类型统计失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询报送类型统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getTaskCompletionTrend(String enterpriseId, Integer months) {
        try {
            return dataSubmissionTaskMapper.selectTaskCompletionTrend(enterpriseId, months);
        } catch (Exception e) {
            log.error("查询任务完成率趋势失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询任务完成率趋势失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getQualityScoreDistribution(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectQualityScoreDistribution(enterpriseId);
        } catch (Exception e) {
            log.error("查询任务质量评分分布失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询任务质量评分分布失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateTaskStatus(List<String> taskIds, String status, String updateBy) {
        try {
            int result = dataSubmissionTaskMapper.batchUpdateTaskStatus(taskIds, status, updateBy);
            log.info("批量更新任务状态成功，更新数量：{}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新任务状态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTask(String taskId, String updateBy) {
        try {
            DataSubmissionTask task = getById(taskId);
            if (task != null) {
                task.setTaskStatus("进行中");
                task.setActualStartTime(LocalDateTime.now());
                task.setUpdateBy(updateBy);
                task.setUpdateTime(LocalDateTime.now());
                boolean result = updateById(task);
                log.info("启动任务成功，任务ID：{}", taskId);
                return result;
            }
            return false;
        } catch (Exception e) {
            log.error("启动任务失败，任务ID：{}", taskId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(String taskId, String completionDescription, String qualityScore, String updateBy) {
        try {
            DataSubmissionTask task = getById(taskId);
            if (task != null) {
                task.setTaskStatus("已完成");
                task.setActualEndTime(LocalDateTime.now());
                task.setCompletionProgress(new BigDecimal("100"));
                task.setCompletionDescription(completionDescription);
                if (qualityScore != null && !qualityScore.isEmpty()) {
                    task.setQualityScore(new BigDecimal(qualityScore));
                }
                task.setUpdateBy(updateBy);
                task.setUpdateTime(LocalDateTime.now());
                boolean result = updateById(task);
                log.info("完成任务成功，任务ID：{}", taskId);
                return result;
            }
            return false;
        } catch (Exception e) {
            log.error("完成任务失败，任务ID：{}", taskId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pauseTask(String taskId, String updateBy) {
        try {
            DataSubmissionTask task = getById(taskId);
            if (task != null) {
                task.setTaskStatus("已暂停");
                task.setUpdateBy(updateBy);
                task.setUpdateTime(LocalDateTime.now());
                boolean result = updateById(task);
                log.info("暂停任务成功，任务ID：{}", taskId);
                return result;
            }
            return false;
        } catch (Exception e) {
            log.error("暂停任务失败，任务ID：{}", taskId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTask(String taskId, String updateBy) {
        try {
            DataSubmissionTask task = getById(taskId);
            if (task != null) {
                task.setTaskStatus("已取消");
                task.setUpdateBy(updateBy);
                task.setUpdateTime(LocalDateTime.now());
                boolean result = updateById(task);
                log.info("取消任务成功，任务ID：{}", taskId);
                return result;
            }
            return false;
        } catch (Exception e) {
            log.error("取消任务失败，任务ID：{}", taskId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean restartTask(String taskId, String updateBy) {
        try {
            DataSubmissionTask task = getById(taskId);
            if (task != null) {
                task.setTaskStatus("进行中");
                task.setActualStartTime(LocalDateTime.now());
                task.setActualEndTime(null);
                task.setUpdateBy(updateBy);
                task.setUpdateTime(LocalDateTime.now());
                boolean result = updateById(task);
                log.info("重启任务成功，任务ID：{}", taskId);
                return result;
            }
            return false;
        } catch (Exception e) {
            log.error("重启任务失败，任务ID：{}", taskId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitFeedback(String taskId, String feedbackInfo, String feedbackStatus, String updateBy) {
        try {
            DataSubmissionTask task = getById(taskId);
            if (task != null) {
                task.setFeedbackInfo(feedbackInfo);
                task.setFeedbackStatus(feedbackStatus);
                task.setFeedbackTime(LocalDateTime.now());
                task.setUpdateBy(updateBy);
                task.setUpdateTime(LocalDateTime.now());
                boolean result = updateById(task);
                log.info("提交反馈成功，任务ID：{}", taskId);
                return result;
            }
            return false;
        } catch (Exception e) {
            log.error("提交反馈失败，任务ID：{}", taskId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processFeedback(String taskId, String rectificationPlan, String updateBy) {
        try {
            DataSubmissionTask task = getById(taskId);
            if (task != null) {
                task.setRectificationPlan(rectificationPlan);
                task.setFeedbackStatus("处理中");
                task.setUpdateBy(updateBy);
                task.setUpdateTime(LocalDateTime.now());
                boolean result = updateById(task);
                log.info("处理反馈成功，任务ID：{}", taskId);
                return result;
            }
            return false;
        } catch (Exception e) {
            log.error("处理反馈失败，任务ID：{}", taskId, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getTaskProgressStatistics(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskProgressStatistics(enterpriseId);
        } catch (Exception e) {
            log.error("查询任务执行进度统计失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询任务执行进度统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getTaskDistributionByDepartment(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskDistributionByDepartment(enterpriseId);
        } catch (Exception e) {
            log.error("查询部门任务分布失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询部门任务分布失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getTaskDistributionByPerson(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskDistributionByPerson(enterpriseId);
        } catch (Exception e) {
            log.error("查询负责人任务分布失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询负责人任务分布失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getTaskDistributionByRegulator(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskDistributionByRegulator(enterpriseId);
        } catch (Exception e) {
            log.error("查询监管部门任务分布失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询监管部门任务分布失败", e);
        }
    }

    @Override
    public Map<String, Object> getFeedbackProcessingStatistics(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectFeedbackProcessingStatistics(enterpriseId);
        } catch (Exception e) {
            log.error("查询任务反馈处理统计失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询任务反馈处理统计失败", e);
        }
    }

    @Override
    public Map<String, Object> getTaskTimelinessAnalysis(String enterpriseId) {
        try {
            return dataSubmissionTaskMapper.selectTaskTimelinessAnalysis(enterpriseId);
        } catch (Exception e) {
            log.error("查询任务时效性分析失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("查询任务时效性分析失败", e);
        }
    }

    @Override
    public List<DataSubmissionTask> exportTaskList(DataSubmissionTaskQueryVo queryVo) {
        try {
            return selectDataSubmissionTaskList(queryVo);
        } catch (Exception e) {
            log.error("导出任务列表失败", e);
            throw new RuntimeException("导出任务列表失败", e);
        }
    }

    @Override
    public Map<String, Object> generateTaskReport(String enterpriseId, String reportType) {
        try {
            // 根据报告类型生成不同的报告数据
            // 这里可以实现具体的报告生成逻辑
            log.info("生成任务报告，企业ID：{}，报告类型：{}", enterpriseId, reportType);
            return null; // 返回报告数据
        } catch (Exception e) {
            log.error("生成任务报告失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("生成任务报告失败", e);
        }
    }
}
