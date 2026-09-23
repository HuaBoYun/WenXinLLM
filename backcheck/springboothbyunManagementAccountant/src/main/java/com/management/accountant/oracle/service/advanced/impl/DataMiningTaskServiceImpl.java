package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.DataMiningTask;
import com.management.accountant.oracle.mapper.advanced.DataMiningTaskMapper;
import com.management.accountant.oracle.service.advanced.DataMiningTaskService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("dataMiningTaskServiceOracle")
public class DataMiningTaskServiceImpl implements DataMiningTaskService {

    @Resource
    private DataMiningTaskMapper taskMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<DataMiningTask> selectList(Map<String, Object> params) {
        return taskMapper.selectList(buildWrapper(params));
    }

    @Override
    public Page<DataMiningTask> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        return taskMapper.selectPage(new Page<>(pageNum, pageSize), buildWrapper(params));
    }

    @Override
    public DataMiningTask selectById(String taskId) { return taskMapper.selectById(taskId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(DataMiningTask task) {
        task.setTaskId("DM" + idWorker.nextId());
        task.setCreateTime(new Date());
        task.setDelFlag(0);
        return taskMapper.insert(task) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(DataMiningTask task) {
        task.setUpdateTime(new Date());
        return taskMapper.updateById(task) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        DataMiningTask t = taskMapper.selectById(id);
        if (t != null) { t.setDelFlag(1); t.setUpdateTime(new Date()); return taskMapper.updateById(t) > 0; }
        return false;
    }

    @Override
    public boolean runTask(String id) {
        DataMiningTask t = taskMapper.selectById(id);
        if (t == null) return false;
        t.setTaskStatus("RUNNING");
        t.setUpdateTime(new Date());
        return taskMapper.updateById(t) > 0;
    }

    @Override
    public boolean stopTask(String id) {
        DataMiningTask t = taskMapper.selectById(id);
        if (t == null) return false;
        t.setTaskStatus("STOPPED");
        t.setUpdateTime(new Date());
        return taskMapper.updateById(t) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyTask(String id) {
        DataMiningTask src = taskMapper.selectById(id);
        if (src == null) return false;
        DataMiningTask copy = new DataMiningTask();
        copy.setTaskId("DM" + idWorker.nextId());
        copy.setTaskName(src.getTaskName() + " - 副本");
        copy.setMiningType(src.getMiningType());
        copy.setAlgorithm(src.getAlgorithm());
        copy.setDataSource(src.getDataSource());
        copy.setTaskStatus("PENDING");
        copy.setDescription(src.getDescription());
        copy.setCreateBy(src.getCreateBy());
        copy.setCreateTime(new Date());
        copy.setDelFlag(0);
        return taskMapper.insert(copy) > 0;
    }

    @Override
    public Map<String, Object> getResults(String taskId) {
        Map<String, Object> results = new HashMap<>();
        results.put("taskId", taskId);
        results.put("data", new ArrayList<>());
        return results;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // 总任务数
        QueryWrapper<DataMiningTask> totalW = new QueryWrapper<>();
        totalW.eq("DEL_FLAG", 0);
        int totalTasks = taskMapper.selectCount(totalW).intValue();
        stats.put("totalTasks", totalTasks);

        // 运行中任务数
        QueryWrapper<DataMiningTask> runningW = new QueryWrapper<>();
        runningW.eq("DEL_FLAG", 0).eq("TASK_STATUS", "RUNNING");
        stats.put("runningTasks", taskMapper.selectCount(runningW));

        // 已完成任务数
        QueryWrapper<DataMiningTask> completedW = new QueryWrapper<>();
        completedW.eq("DEL_FLAG", 0).eq("TASK_STATUS", "COMPLETED");
        stats.put("completedTasks", taskMapper.selectCount(completedW));

        // 发现模式总数：SUM(PATTERNS_FOUND) from COMPLETED tasks
        QueryWrapper<DataMiningTask> patternW = new QueryWrapper<>();
        patternW.eq("DEL_FLAG", 0).eq("TASK_STATUS", "COMPLETED").select("PATTERNS_FOUND");
        List<DataMiningTask> completedTasks = taskMapper.selectList(patternW);
        int discoveredPatterns = completedTasks.stream()
                .mapToInt(t -> t.getPatternsFound() != null ? t.getPatternsFound() : 0)
                .sum();
        stats.put("discoveredPatterns", discoveredPatterns);

        // 平均准确率：AVG(ACCURACY_RATE) from COMPLETED tasks with accuracy > 0
        double accuracy = completedTasks.stream()
                .filter(t -> t.getAccuracyRate() != null && t.getAccuracyRate() > 0)
                .mapToDouble(DataMiningTask::getAccuracyRate)
                .average()
                .orElse(0.0);
        stats.put("accuracy", Math.round(accuracy * 10.0) / 10.0);

        return stats;
    }

    private QueryWrapper<DataMiningTask> buildWrapper(Map<String, Object> params) {
        QueryWrapper<DataMiningTask> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("TASK_NAME", keyword);
            String type = (String) params.get("type");
            if (StringUtils.hasText(type)) w.eq("MINING_TYPE", type);
        }
        w.orderByDesc("CREATE_TIME");
        return w;
    }
}
