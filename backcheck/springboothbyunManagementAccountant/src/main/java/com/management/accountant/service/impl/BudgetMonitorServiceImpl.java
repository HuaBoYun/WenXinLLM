package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetMonitor;
import com.management.accountant.oracle.mapper.budget.BudgetMonitorMapper;
import com.management.accountant.service.BudgetMonitorService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 预算监控Service实现类
 * 
 * @description 预算监控业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetMonitorServiceImpl implements BudgetMonitorService {

    @Resource
    private BudgetMonitorMapper monitorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetMonitor create(BudgetMonitor monitor) {
        // 1. 参数校验
        if (monitor == null) {
            throw new ServiceException("监控信息不能为空");
        }
        if (!StringUtils.hasText(monitor.getMonitorName())) {
            throw new ServiceException("监控名称不能为空");
        }
        if (!StringUtils.hasText(monitor.getMonitorType())) {
            throw new ServiceException("监控类型不能为空");
        }

        // 2. 生成监控编码
        if (!StringUtils.hasText(monitor.getMonitorCode())) {
            monitor.setMonitorCode(generateMonitorCode());
        }

        // 3. 设置默认值
        if (monitor.getDelFlag() == null) {
            monitor.setDelFlag(0);
        }
        if (!StringUtils.hasText(monitor.getMonitorStatus())) {
            monitor.setMonitorStatus("ACTIVE");
        }
        if (monitor.getCheckInterval() == null) {
            monitor.setCheckInterval(60);
        }
        monitor.setCreateTime(new Date());
        monitor.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = monitorMapper.insert(monitor);
        if (result <= 0) {
            throw new ServiceException("创建监控失败");
        }

        log.info("创建监控成功，ID: {}", monitor.getMonitorId());
        return monitor;
    }

    @Override
    public BudgetMonitor getById(String monitorId) {
        if (!StringUtils.hasText(monitorId)) {
            throw new ServiceException("监控ID不能为空");
        }
        
        QueryWrapper<BudgetMonitor> wrapper = new QueryWrapper<>();
        wrapper.eq("MONITOR_ID", monitorId)
               .eq("DEL_FLAG", 0);
        
        return monitorMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetMonitor monitor) {
        if (monitor == null || !StringUtils.hasText(monitor.getMonitorId())) {
            throw new ServiceException("监控ID不能为空");
        }

        BudgetMonitor existing = getById(monitor.getMonitorId());
        if (existing == null) {
            throw new ServiceException("监控记录不存在");
        }

        monitor.setUpdateTime(new Date());
        int result = monitorMapper.updateById(monitor);
        if (result <= 0) {
            throw new ServiceException("更新监控失败");
        }

        log.info("更新监控成功，ID: {}", monitor.getMonitorId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String monitorId) {
        if (!StringUtils.hasText(monitorId)) {
            throw new ServiceException("监控ID不能为空");
        }

        BudgetMonitor monitor = getById(monitorId);
        if (monitor == null) {
            throw new ServiceException("监控记录不存在");
        }

        BudgetMonitor update = new BudgetMonitor();
        update.setMonitorId(monitorId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = monitorMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除监控失败");
        }

        log.info("删除监控成功，ID: {}", monitorId);
    }

    @Override
    public PageResult<BudgetMonitor> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetMonitor> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 监控编码
        if (params.get("monitorCode") != null && StringUtils.hasText(params.get("monitorCode").toString())) {
            wrapper.like("MONITOR_CODE", params.get("monitorCode"));
        }

        // 监控名称
        if (params.get("monitorName") != null && StringUtils.hasText(params.get("monitorName").toString())) {
            wrapper.like("MONITOR_NAME", params.get("monitorName"));
        }

        // 监控类型
        if (params.get("monitorType") != null && StringUtils.hasText(params.get("monitorType").toString())) {
            wrapper.eq("MONITOR_TYPE", params.get("monitorType"));
        }

        // 监控状态
        if (params.get("monitorStatus") != null && StringUtils.hasText(params.get("monitorStatus").toString())) {
            wrapper.eq("MONITOR_STATUS", params.get("monitorStatus"));
        }

        // 预算ID
        if (params.get("budgetId") != null && StringUtils.hasText(params.get("budgetId").toString())) {
            wrapper.eq("BUDGET_ID", params.get("budgetId"));
        }

        // 组织ID
        if (params.get("organizationId") != null && StringUtils.hasText(params.get("organizationId").toString())) {
            wrapper.eq("ORGANIZATION_ID", params.get("organizationId"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetMonitor> page = new Page<>(pageNum, pageSize);
        IPage<BudgetMonitor> pageResult = monitorMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetMonitor> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void start(String monitorId) {
        if (!StringUtils.hasText(monitorId)) {
            throw new ServiceException("监控ID不能为空");
        }

        BudgetMonitor monitor = getById(monitorId);
        if (monitor == null) {
            throw new ServiceException("监控记录不存在");
        }

        if ("ACTIVE".equals(monitor.getMonitorStatus())) {
            throw new ServiceException("监控已经启动");
        }

        BudgetMonitor update = new BudgetMonitor();
        update.setMonitorId(monitorId);
        update.setMonitorStatus("ACTIVE");
        update.setUpdateTime(new Date());

        int result = monitorMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("启动监控失败");
        }

        log.info("启动监控成功，ID: {}", monitorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stop(String monitorId) {
        if (!StringUtils.hasText(monitorId)) {
            throw new ServiceException("监控ID不能为空");
        }

        BudgetMonitor monitor = getById(monitorId);
        if (monitor == null) {
            throw new ServiceException("监控记录不存在");
        }

        if ("STOPPED".equals(monitor.getMonitorStatus())) {
            throw new ServiceException("监控已经处于停止状态");
        }

        BudgetMonitor update = new BudgetMonitor();
        update.setMonitorId(monitorId);
        update.setMonitorStatus("STOPPED");
        update.setUpdateTime(new Date());

        int result = monitorMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("停止监控失败");
        }

        log.info("停止监控成功，ID: {}", monitorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetMonitor refresh(String monitorId) {
        if (!StringUtils.hasText(monitorId)) {
            throw new ServiceException("监控ID不能为空");
        }

        BudgetMonitor monitor = getById(monitorId);
        if (monitor == null) {
            throw new ServiceException("监控记录不存在");
        }

        // 刷新：更新最后检查时间，根据当前值与阈值比较更新状态
        BigDecimal currentValue = monitor.getCurrentValue();
        BigDecimal thresholdValue = monitor.getThresholdValue();

        if (currentValue != null && thresholdValue != null && thresholdValue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = currentValue.divide(thresholdValue, 4, RoundingMode.HALF_UP)
                                           .multiply(new BigDecimal("100"));
            if (ratio.compareTo(new BigDecimal("90")) >= 0) {
                monitor.setMonitorStatus("CRITICAL");
            } else if (ratio.compareTo(new BigDecimal("80")) >= 0) {
                monitor.setMonitorStatus("ALERT");
            } else if (ratio.compareTo(new BigDecimal("70")) >= 0) {
                monitor.setMonitorStatus("WARNING");
            } else {
                monitor.setMonitorStatus("ACTIVE");
            }
        }

        monitor.setLastCheckTime(new Date());
        monitor.setUpdateTime(new Date());

        int result = monitorMapper.updateById(monitor);
        if (result <= 0) {
            throw new ServiceException("刷新监控数据失败");
        }

        log.info("刷新监控数据成功，ID: {}", monitorId);
        return monitor;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        QueryWrapper<BudgetMonitor> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 总数
        Integer totalCount = monitorMapper.selectCount(wrapper).intValue();
        statistics.put("totalCount", totalCount);

        // 激活数量
        QueryWrapper<BudgetMonitor> activeWrapper = new QueryWrapper<>();
        activeWrapper.eq("DEL_FLAG", 0).eq("MONITOR_STATUS", "ACTIVE");
        Integer activeCount = monitorMapper.selectCount(activeWrapper).intValue();
        statistics.put("activeCount", activeCount);

        // 预警数量
        wrapper = new QueryWrapper<BudgetMonitor>();
        wrapper.eq("DEL_FLAG", 0);
        wrapper.eq("MONITOR_STATUS", "WARNING");
        Integer warningCount = monitorMapper.selectCount(wrapper).intValue();
        statistics.put("warningCount", warningCount);

        // 告警数量
        wrapper = new QueryWrapper<BudgetMonitor>();
        wrapper.eq("DEL_FLAG", 0);
        wrapper.eq("MONITOR_STATUS", "ALERT");
        Integer alertCount = monitorMapper.selectCount(wrapper).intValue();
        statistics.put("alertCount", alertCount);

        // 严重数量
        wrapper = new QueryWrapper<BudgetMonitor>();
        wrapper.eq("DEL_FLAG", 0);
        wrapper.eq("MONITOR_STATUS", "CRITICAL");
        Integer criticalCount = monitorMapper.selectCount(wrapper).intValue();
        statistics.put("criticalCount", criticalCount);

        return statistics;
    }

    @Override
    public List<BudgetMonitor> getAlerts() {
        QueryWrapper<BudgetMonitor> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0)
               .in("MONITOR_STATUS", Arrays.asList("WARNING", "ALERT", "CRITICAL"))
               .orderByDesc("LAST_CHECK_TIME");

        return monitorMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchStart(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> monitorIds = (List<String>) params.get("monitorIds");

        if (monitorIds == null || monitorIds.isEmpty()) {
            throw new ServiceException("监控ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String monitorId : monitorIds) {
            try {
                start(monitorId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(monitorId);
                log.error("批量启动监控失败，ID: {}", monitorId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", monitorIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量启动监控完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchStop(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> monitorIds = (List<String>) params.get("monitorIds");

        if (monitorIds == null || monitorIds.isEmpty()) {
            throw new ServiceException("监控ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String monitorId : monitorIds) {
            try {
                stop(monitorId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(monitorId);
                log.error("批量停止监控失败，ID: {}", monitorId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", monitorIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量停止监控完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public List<BudgetMonitor> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetMonitor> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 应用查询条件
        if (params.get("monitorStatus") != null && StringUtils.hasText(params.get("monitorStatus").toString())) {
            wrapper.eq("MONITOR_STATUS", params.get("monitorStatus"));
        }
        if (params.get("organizationId") != null && StringUtils.hasText(params.get("organizationId").toString())) {
            wrapper.eq("ORGANIZATION_ID", params.get("organizationId"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        return monitorMapper.selectList(wrapper);
    }

    /**
     * 生成监控编码
     */
    private String generateMonitorCode() {
        return "MON" + System.currentTimeMillis();
    }
}

