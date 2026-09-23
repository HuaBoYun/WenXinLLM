package com.financial.sharing.service.impl;

import com.financial.sharing.service.FinanceScheduledService;
import com.financial.sharing.oracle.mapper.FinanceScheduledMapper;
import com.financial.sharing.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调度服务实现类
 */
@Service
public class FinanceScheduledServiceImpl implements FinanceScheduledService {

    @Autowired
    private FinanceScheduledMapper financeScheduledMapper;

    @Override
    public List<Map<String, Object>> getScheduledList(Map<String, Object> params) {
        return financeScheduledMapper.selectScheduledList(params);
    }

    @Override
    public Map<String, Object> getScheduledDetail(String scheduledId) {
        return financeScheduledMapper.selectScheduledById(scheduledId);
    }

    @Override
    public int createScheduled(Map<String, Object> scheduled) {
        return financeScheduledMapper.insertScheduled(scheduled);
    }

    @Override
    public int updateScheduled(Map<String, Object> scheduled) {
        return financeScheduledMapper.updateScheduled(scheduled);
    }

    @Override
    public int deleteScheduled(String scheduledId) {
        return financeScheduledMapper.deleteScheduled(scheduledId);
    }

    @Override
    public int startScheduled(String scheduledId) {
        Map<String, Object> params = new HashMap<>();
        params.put("scheduledId", scheduledId);
        params.put("status", "RUNNING");
        return financeScheduledMapper.updateScheduledStatus(params);
    }

    @Override
    public int stopScheduled(String scheduledId) {
        Map<String, Object> params = new HashMap<>();
        params.put("scheduledId", scheduledId);
        params.put("status", "STOPPED");
        return financeScheduledMapper.updateScheduledStatus(params);
    }

    @Override
    public List<Map<String, Object>> getScheduledHistory(Map<String, Object> params) {
        return financeScheduledMapper.selectScheduledHistory(params);
    }

    // ==================== Controller 需要的方法实现 ====================

    @Override
    public PageResult<Map<String, Object>> getScheduledTaskList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeScheduledMapper.selectScheduledTaskList(params);
        int total = financeScheduledMapper.countScheduledTaskList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public boolean deleteScheduledTask(Map<String, Object> params) {
        return financeScheduledMapper.deleteScheduledTask(params) > 0;
    }

    @Override
    public boolean changeScheduledTaskStatus(Map<String, Object> data) {
        return financeScheduledMapper.updateScheduledTaskStatus(data) > 0;
    }

    @Override
    public Map<String, Object> saveScheduledTask(Map<String, Object> data) {
        financeScheduledMapper.insertScheduledTask(data);
        return data;
    }

    @Override
    public Map<String, Object> updateScheduledTask(Map<String, Object> data) {
        financeScheduledMapper.updateScheduledTaskInfo(data);
        return data;
    }

    @Override
    public Map<String, Object> getScheduledTaskDetail(Map<String, Object> params) {
        return financeScheduledMapper.selectScheduledTaskDetail(params);
    }
}