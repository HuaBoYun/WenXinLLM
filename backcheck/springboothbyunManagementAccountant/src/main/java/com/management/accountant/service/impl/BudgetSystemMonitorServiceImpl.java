package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.budget.BudgetSystemMonitorEntity;
import com.management.accountant.oracle.mapper.budget.BudgetSystemMonitorMapper;
import com.management.accountant.service.BudgetSystemMonitorService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.math.BigDecimal;

@Service
@Slf4j
public class BudgetSystemMonitorServiceImpl implements BudgetSystemMonitorService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetSystemMonitorMapper monitorMapper;

    @Override
    public PageResult<BudgetSystemMonitorEntity> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetSystemMonitorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        if (params.get("monitorType") != null && StringUtils.hasText(params.get("monitorType").toString())) {
            wrapper.eq("MONITOR_TYPE", params.get("monitorType"));
        }
        if (params.get("serviceStatus") != null && StringUtils.hasText(params.get("serviceStatus").toString())) {
            wrapper.eq("SERVICE_STATUS", params.get("serviceStatus"));
        }
        if (params.get("alertLevel") != null && StringUtils.hasText(params.get("alertLevel").toString())) {
            wrapper.eq("ALERT_LEVEL", params.get("alertLevel"));
        }
        wrapper.orderByDesc("MONITOR_TIME");

        Page<BudgetSystemMonitorEntity> page = new Page<>(pageNum, pageSize);
        IPage<BudgetSystemMonitorEntity> pageResult = monitorMapper.selectPage(page, wrapper);

        PageResult<BudgetSystemMonitorEntity> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public BudgetSystemMonitorEntity getById(String monitorId) {
        if (!StringUtils.hasText(monitorId)) {
            return null;
        }
        QueryWrapper<BudgetSystemMonitorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("MONITOR_ID", monitorId).eq("IS_DELETED", 0);
        return monitorMapper.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        QueryWrapper<BudgetSystemMonitorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        stats.put("totalMonitors", monitorMapper.selectCount(wrapper));

        QueryWrapper<BudgetSystemMonitorEntity> runningWrapper = new QueryWrapper<>();
        runningWrapper.eq("IS_DELETED", 0).eq("SERVICE_STATUS", "RUNNING");
        stats.put("runningServices", monitorMapper.selectCount(runningWrapper));

        QueryWrapper<BudgetSystemMonitorEntity> warningWrapper = new QueryWrapper<>();
        warningWrapper.eq("IS_DELETED", 0).eq("ALERT_LEVEL", "WARNING");
        stats.put("warningCount", monitorMapper.selectCount(warningWrapper));

        QueryWrapper<BudgetSystemMonitorEntity> errorWrapper = new QueryWrapper<>();
        errorWrapper.eq("IS_DELETED", 0).eq("ALERT_LEVEL", "ERROR");
        stats.put("errorCount", monitorMapper.selectCount(errorWrapper));

        return stats;
    }

    @Override
    public List<BudgetSystemMonitorEntity> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetSystemMonitorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.orderByDesc("MONITOR_TIME");
        return monitorMapper.selectList(wrapper);
    }

    @Override
    public int updateMonitor(BudgetSystemMonitorEntity entity) {
        if (entity == null || !StringUtils.hasText(entity.getMonitorId())) {
            return 0;
        }
        return monitorMapper.updateById(entity);
    }

    @Override
    public BudgetSystemMonitorEntity saveMonitor(BudgetSystemMonitorEntity entity) {
        if (entity == null) {
            return null;
        }
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(0);
        }
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(new Date());
        }
        if (entity.getMonitorTime() == null) {
            entity.setMonitorTime(new Date());
        }
        monitorMapper.insert(entity);
        return entity;
    }

    @Override
    public PageResult<BudgetSystemMonitorEntity> getAlerts(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetSystemMonitorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.eq("MONITOR_TYPE", "ALERT");
        wrapper.isNotNull("ALERT_LEVEL");
        wrapper.ne("ALERT_LEVEL", "");

        if (params.get("alertLevel") != null && StringUtils.hasText(params.get("alertLevel").toString())) {
            wrapper.eq("ALERT_LEVEL", params.get("alertLevel"));
        }
        if (params.get("status") != null && StringUtils.hasText(params.get("status").toString())) {
            wrapper.eq("STATUS", params.get("status"));
        }
        wrapper.orderByDesc("MONITOR_TIME");

        Page<BudgetSystemMonitorEntity> page = new Page<>(pageNum, pageSize);
        IPage<BudgetSystemMonitorEntity> pageResult = monitorMapper.selectPage(page, wrapper);

        PageResult<BudgetSystemMonitorEntity> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public PageResult<BudgetSystemMonitorEntity> getServiceMonitors(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetSystemMonitorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.eq("MONITOR_TYPE", "SERVICE");
        wrapper.orderByDesc("MONITOR_TIME");

        Page<BudgetSystemMonitorEntity> page = new Page<>(pageNum, pageSize);
        IPage<BudgetSystemMonitorEntity> pageResult = monitorMapper.selectPage(page, wrapper);

        PageResult<BudgetSystemMonitorEntity> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public List<BudgetSystemMonitorEntity> getNetworkTrafficData(int hours) {
        QueryWrapper<BudgetSystemMonitorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.eq("MONITOR_TYPE", "NETWORK");
        wrapper.orderByAsc("MONITOR_TIME");
        wrapper.last("FETCH FIRST " + hours + " ROWS ONLY");
        return monitorMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getMonitorSettings() {
        Map<String, Object> settings = new HashMap<>();
        // 从数据库读取SETTINGS类型的监控记录作为配置
        QueryWrapper<BudgetSystemMonitorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.eq("MONITOR_TYPE", "SETTINGS");
        wrapper.orderByDesc("CREATE_TIME");
        wrapper.last("FETCH FIRST 1 ROWS ONLY");
        BudgetSystemMonitorEntity settingsEntity = monitorMapper.selectOne(wrapper);
        if (settingsEntity != null) {
            settings.put("refreshInterval", settingsEntity.getResponseTime() != null ? settingsEntity.getResponseTime() : 30);
            settings.put("cpuThreshold", settingsEntity.getCpuUsage() != null ? settingsEntity.getCpuUsage() : new BigDecimal("80"));
            settings.put("memoryThreshold", settingsEntity.getMemoryUsage() != null ? settingsEntity.getMemoryUsage() : new BigDecimal("80"));
            settings.put("diskThreshold", settingsEntity.getDiskUsage() != null ? settingsEntity.getDiskUsage() : new BigDecimal("90"));
            settings.put("alertEnabled", settingsEntity.getStatus() != null ? "ENABLED".equals(settingsEntity.getStatus()) : true);
            settings.put("remark", settingsEntity.getRemark() != null ? settingsEntity.getRemark() : "");
        } else {
            settings.put("refreshInterval", 30);
            settings.put("cpuThreshold", new BigDecimal("80"));
            settings.put("memoryThreshold", new BigDecimal("80"));
            settings.put("diskThreshold", new BigDecimal("90"));
            settings.put("alertEnabled", true);
            settings.put("remark", "");
        }
        return settings;
    }

    @Override
    public void saveMonitorSettings(Map<String, Object> settings) {
        // 先删除旧的SETTINGS记录
        UpdateWrapper<BudgetSystemMonitorEntity> deleteWrapper = new UpdateWrapper<>();
        deleteWrapper.eq("MONITOR_TYPE", "SETTINGS").set("IS_DELETED", 1);
        monitorMapper.update(null, deleteWrapper);

        // 插入新的SETTINGS记录
        BudgetSystemMonitorEntity entity = new BudgetSystemMonitorEntity();
        entity.setMonitorName("监控设置");
        entity.setMonitorType("SETTINGS");
        entity.setResponseTime(settings.get("refreshInterval") != null ? Integer.parseInt(settings.get("refreshInterval").toString()) : 30);
        entity.setCpuUsage(settings.get("cpuThreshold") != null ? new BigDecimal(settings.get("cpuThreshold").toString()) : new BigDecimal("80"));
        entity.setMemoryUsage(settings.get("memoryThreshold") != null ? new BigDecimal(settings.get("memoryThreshold").toString()) : new BigDecimal("80"));
        entity.setDiskUsage(settings.get("diskThreshold") != null ? new BigDecimal(settings.get("diskThreshold").toString()) : new BigDecimal("90"));
        entity.setStatus(settings.get("alertEnabled") != null && Boolean.parseBoolean(settings.get("alertEnabled").toString()) ? "ENABLED" : "DISABLED");
        entity.setRemark(settings.get("remark") != null ? settings.get("remark").toString() : "");
        entity.setIsDeleted(0);
        entity.setCreateTime(new Date());
        entity.setMonitorTime(new Date());
        monitorMapper.insert(entity);
    }
}

