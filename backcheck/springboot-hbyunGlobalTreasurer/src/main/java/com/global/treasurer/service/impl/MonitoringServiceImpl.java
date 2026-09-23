package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblMonitoring;
import com.global.treasurer.mapper.TblMonitoringMapper;
import com.global.treasurer.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MonitoringServiceImpl implements MonitoringService {
    @Autowired
    private TblMonitoringMapper monitoringMapper;

    @Override
    public Map<String, Object> getMonitoringPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数: pageNum 和 pageSize
        Integer page = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                       (params.get("current") != null ? Integer.parseInt(params.get("current").toString()) : 1);
        Integer size = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                       (params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10);

        params.put("offset", (page - 1) * size);
        params.put("limit", size);

        List<TblMonitoring> list = monitoringMapper.selectMonitoringPage(params);
        int total = monitoringMapper.countMonitoringList(params);

        // 兼容前端数据格式: rows 和 total
        result.put("rows", list);
        result.put("total", total);

        return result;
    }

    @Override
    public TblMonitoring getMonitoringById(Long monitoringId) {
        return monitoringMapper.selectById(monitoringId);
    }

    @Override
    public int createMonitoring(TblMonitoring monitoring) {
        monitoring.setDeleteFlag(0);
        monitoring.setCreatedTime(new Date());
        return monitoringMapper.insert(monitoring);
    }

    @Override
    public int updateMonitoring(TblMonitoring monitoring) {
        monitoring.setUpdatedTime(new Date());
        return monitoringMapper.updateById(monitoring);
    }

    @Override
    public int deleteMonitoring(List<Long> monitoringIds) {
        if (monitoringIds == null || monitoringIds.isEmpty()) return 0;
        int count = 0;
        for (Long id : monitoringIds) {
            TblMonitoring m = getMonitoringById(id);
            if (m != null) {
                m.setDeleteFlag(1);
                m.setUpdatedTime(new Date());
                count += monitoringMapper.updateById(m);
            }
        }
        return count;
    }

    @Override
    public List<TblMonitoring> getAlerts(Long orgId) {
        return monitoringMapper.selectAlerts(orgId);
    }

    @Override
    public List<TblMonitoring> getCriticalAlerts(Long orgId) {
        return monitoringMapper.selectCriticalAlerts(orgId);
    }

    @Override
    public Map<String, Object> getSystemHealthStatus(Long orgId) {
        return monitoringMapper.selectHealthStatus(orgId);
    }

    @Override
    public Map<String, Object> getDashboardData(Long orgId) {
        return monitoringMapper.selectDashboardData(orgId);
    }

    @Override
    public List<Map<String, Object>> getMonitoringTrend(Map<String, Object> params) {
        return monitoringMapper.selectMonitoringTrend(params);
    }

    @Override
    public int sendAlert(Long monitoringId) {
        // 模拟发送告警
        return 1;
    }
}
