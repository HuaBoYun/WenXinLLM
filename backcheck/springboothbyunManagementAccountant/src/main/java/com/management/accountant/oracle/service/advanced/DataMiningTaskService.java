package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.DataMiningTask;

import java.util.List;
import java.util.Map;

public interface DataMiningTaskService {
    List<DataMiningTask> selectList(Map<String, Object> params);
    Page<DataMiningTask> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);
    DataMiningTask selectById(String taskId);
    boolean insert(DataMiningTask task);
    boolean update(DataMiningTask task);
    boolean deleteById(String taskId);
    boolean runTask(String taskId);
    boolean stopTask(String taskId);
    boolean copyTask(String taskId);
    Map<String, Object> getResults(String taskId);
    Map<String, Object> getStats();
}
