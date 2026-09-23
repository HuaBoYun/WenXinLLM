package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.SystemMonitor;
import com.huabo.cybermonitor.mapper.SystemMonitorMapper;
import com.huabo.cybermonitor.service.ISystemMonitorService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.SystemMonitorQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统监控记录服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SystemMonitorServiceImpl extends ServiceImpl<SystemMonitorMapper, SystemMonitor> implements ISystemMonitorService {

    @Autowired
    private SystemMonitorMapper monitorMapper;

    @Override
    public IPage<SystemMonitor> getMonitorList(SystemMonitorQueryVO queryVO) {
        Page<SystemMonitor> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        return monitorMapper.selectMonitorList(page, queryVO);
    }

    @Override
    public SystemMonitor getMonitorDetail(String monitorId) {
        if (StringUtils.isEmpty(monitorId)) {
            return null;
        }
        return monitorMapper.selectById(monitorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMonitor(SystemMonitor monitor) {
        try {
            monitor.setCreateTime(LocalDateTime.now());
            if (monitor.getMonitorTime() == null) {
                monitor.setMonitorTime(LocalDateTime.now());
            }
            if (monitor.getProcessStatus() == null) {
                monitor.setProcessStatus(SystemMonitor.PROCESS_STATUS_PENDING);
            }

            return save(monitor);
        } catch (Exception e) {
            log.error("新增系统监控记录失败", e);
            throw new RuntimeException("新增系统监控记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMonitor(SystemMonitor monitor) {
        try {
            return updateById(monitor);
        } catch (Exception e) {
            log.error("更新系统监控记录失败", e);
            throw new RuntimeException("更新系统监控记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMonitor(String monitorId) {
        try {
            return removeById(monitorId);
        } catch (Exception e) {
            log.error("删除系统监控记录失败", e);
            throw new RuntimeException("删除系统监控记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteMonitor(List<String> monitorIds) {
        try {
            return removeByIds(monitorIds);
        } catch (Exception e) {
            log.error("批量删除系统监控记录失败", e);
            throw new RuntimeException("批量删除系统监控记录失败：" + e.getMessage());
        }
    }

    @Override
    public List<SystemMonitor> getMonitorsByType(String monitorType) {
        return monitorMapper.selectMonitorsByType(monitorType);
    }

    @Override
    public List<SystemMonitor> getMonitorsByStatus(String monitorStatus) {
        return monitorMapper.selectMonitorsByStatus(monitorStatus);
    }

    @Override
    public List<SystemMonitor> getMonitorsByAlertLevel(String alertLevel) {
        return monitorMapper.selectMonitorsByAlertLevel(alertLevel);
    }

    @Override
    public List<SystemMonitor> getMonitorsByServer(String serverIp) {
        return monitorMapper.selectMonitorsByServer(serverIp);
    }

    @Override
    public List<SystemMonitor> getMonitorsByApplication(String applicationName) {
        return monitorMapper.selectMonitorsByApplication(applicationName);
    }

    @Override
    public List<SystemMonitor> getUnprocessedMonitors() {
        return monitorMapper.selectUnprocessedMonitors();
    }

    @Override
    public List<SystemMonitor> getHighPriorityAlerts() {
        return monitorMapper.selectHighPriorityAlerts();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processMonitor(String monitorId, String processStatus, String processBy, String processRemark) {
        try {
            return monitorMapper.updateMonitorProcessStatus(monitorId, processStatus, processBy, processRemark) > 0;
        } catch (Exception e) {
            log.error("处理监控记录失败", e);
            throw new RuntimeException("处理监控记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchProcessMonitor(List<String> monitorIds, String processStatus, String processBy, String processRemark) {
        try {
            return monitorMapper.batchUpdateMonitorProcessStatus(monitorIds, processStatus, processBy, processRemark) > 0;
        } catch (Exception e) {
            log.error("批量处理监控记录失败", e);
            throw new RuntimeException("批量处理监控记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordMonitorData(String monitorType, String monitorItem, String monitorValue,
                                     String monitorUnit, String thresholdConfig, String serverIp,
                                     String serverName, String applicationName) {
        try {
            SystemMonitor monitor = new SystemMonitor();
            monitor.setMonitorType(monitorType);
            monitor.setMonitorItem(monitorItem);
            monitor.setMonitorValue(monitorValue);
            monitor.setMonitorUnit(monitorUnit);
            monitor.setThresholdConfig(thresholdConfig);
            monitor.setServerIp(serverIp);
            monitor.setServerName(serverName);
            monitor.setApplicationName(applicationName);
            monitor.setMonitorTime(LocalDateTime.now());
            monitor.setCreateTime(LocalDateTime.now());

            // 检查阈值并设置状态
            checkThresholdAndAlert(monitor);

            return save(monitor);
        } catch (Exception e) {
            log.error("记录监控数据失败", e);
            throw new RuntimeException("记录监控数据失败：" + e.getMessage());
        }
    }

    @Override
    public boolean checkThresholdAndAlert(SystemMonitor monitor) {
        try {
            // 简化的阈值检查逻辑
            String thresholdConfig = monitor.getThresholdConfig();
            String monitorValue = monitor.getMonitorValue();

            if (StringUtils.isEmpty(thresholdConfig) || StringUtils.isEmpty(monitorValue)) {
                monitor.setMonitorStatus(SystemMonitor.STATUS_NORMAL);
                monitor.setAlertLevel(SystemMonitor.ALERT_LEVEL_INFO);
                return false;
            }

            try {
                double value = Double.parseDouble(monitorValue);
                
                // 假设阈值配置格式为 "warning:80,critical:90"
                if (thresholdConfig.contains("critical")) {
                    String[] parts = thresholdConfig.split(",");
                    for (String part : parts) {
                        if (part.contains("critical")) {
                            double criticalThreshold = Double.parseDouble(part.split(":")[1]);
                            if (value >= criticalThreshold) {
                                monitor.setMonitorStatus(SystemMonitor.STATUS_CRITICAL);
                                monitor.setAlertLevel(SystemMonitor.ALERT_LEVEL_CRITICAL);
                                monitor.setAlertMessage("监控值超过严重阈值：" + value + " >= " + criticalThreshold);
                                sendAlertNotification(monitor);
                                return true;
                            }
                        } else if (part.contains("warning")) {
                            double warningThreshold = Double.parseDouble(part.split(":")[1]);
                            if (value >= warningThreshold) {
                                monitor.setMonitorStatus(SystemMonitor.STATUS_WARNING);
                                monitor.setAlertLevel(SystemMonitor.ALERT_LEVEL_MEDIUM);
                                monitor.setAlertMessage("监控值超过警告阈值：" + value + " >= " + warningThreshold);
                                return true;
                            }
                        }
                    }
                }

                monitor.setMonitorStatus(SystemMonitor.STATUS_NORMAL);
                monitor.setAlertLevel(SystemMonitor.ALERT_LEVEL_INFO);
                return false;
            } catch (NumberFormatException e) {
                monitor.setMonitorStatus(SystemMonitor.STATUS_ERROR);
                monitor.setAlertLevel(SystemMonitor.ALERT_LEVEL_LOW);
                monitor.setAlertMessage("监控值格式错误：" + monitorValue);
                return false;
            }
        } catch (Exception e) {
            log.error("检查监控阈值失败", e);
            return false;
        }
    }

    @Override
    public boolean sendAlertNotification(SystemMonitor monitor) {
        try {
            // 简化的告警通知逻辑
            log.warn("发送告警通知: 服务器[{}] 监控项[{}] 状态[{}] 级别[{}] 消息[{}]",
                    monitor.getServerIp(), monitor.getMonitorItem(), 
                    monitor.getMonitorStatus(), monitor.getAlertLevel(), monitor.getAlertMessage());
            
            // 这里可以集成邮件、短信、钉钉等通知方式
            return true;
        } catch (Exception e) {
            log.error("发送告警通知失败", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getMonitorStatistics() {
        return monitorMapper.selectMonitorStatistics();
    }

    @Override
    public List<Map<String, Object>> getMonitorTypeDistribution() {
        return monitorMapper.selectMonitorTypeDistribution();
    }

    @Override
    public List<Map<String, Object>> getMonitorStatusDistribution() {
        return monitorMapper.selectMonitorStatusDistribution();
    }

    @Override
    public List<Map<String, Object>> getAlertLevelDistribution() {
        return monitorMapper.selectAlertLevelDistribution();
    }

    @Override
    public List<Map<String, Object>> getServerDistribution() {
        return monitorMapper.selectServerDistribution();
    }

    @Override
    public List<Map<String, Object>> getApplicationDistribution() {
        return monitorMapper.selectApplicationDistribution();
    }

    @Override
    public List<Map<String, Object>> getMonitorTrend(String startDate, String endDate) {
        return monitorMapper.selectMonitorTrend(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getAlertTrend(String startDate, String endDate) {
        return monitorMapper.selectAlertTrend(startDate, endDate);
    }

    @Override
    public Map<String, Object> getPerformanceStatistics() {
        return monitorMapper.selectPerformanceStatistics();
    }

    @Override
    public Map<String, Object> getSystemHealthStatus() {
        return monitorMapper.selectSystemHealthStatus();
    }

    @Override
    public Map<String, Object> getRealTimeMonitorData(String monitorType, String serverIp) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取最新监控数据
            List<SystemMonitor> monitors = monitorMapper.selectMonitorsByType(monitorType);
            if (!StringUtils.isEmpty(serverIp)) {
                monitors = monitors.stream()
                        .filter(m -> serverIp.equals(m.getServerIp()))
                        .collect(java.util.stream.Collectors.toList());
            }

            result.put("monitorData", monitors);
            result.put("updateTime", LocalDateTime.now());
            result.put("totalCount", monitors.size());
        } catch (Exception e) {
            log.error("获取实时监控数据失败", e);
            result.put("error", "获取实时监控数据失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getMonitorDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();
        try {
            // 基础统计
            Map<String, Object> statistics = getMonitorStatistics();
            dashboard.put("statistics", statistics);

            // 分布统计
            dashboard.put("typeDistribution", getMonitorTypeDistribution());
            dashboard.put("statusDistribution", getMonitorStatusDistribution());
            dashboard.put("alertLevelDistribution", getAlertLevelDistribution());

            // 服务器和应用分布
            dashboard.put("serverDistribution", getServerDistribution());
            dashboard.put("applicationDistribution", getApplicationDistribution());

            // 未处理告警
            List<SystemMonitor> unprocessedAlerts = getUnprocessedMonitors();
            dashboard.put("unprocessedAlerts", unprocessedAlerts);
            dashboard.put("unprocessedCount", unprocessedAlerts.size());

            // 高优先级告警
            List<SystemMonitor> highPriorityAlerts = getHighPriorityAlerts();
            dashboard.put("highPriorityAlerts", highPriorityAlerts);
            dashboard.put("highPriorityCount", highPriorityAlerts.size());

            // 系统健康状况
            dashboard.put("healthStatus", getSystemHealthStatus());

        } catch (Exception e) {
            log.error("获取监控仪表板数据失败", e);
            dashboard.put("error", "获取监控仪表板数据失败：" + e.getMessage());
        }
        return dashboard;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer cleanExpiredMonitors(Integer days) {
        try {
            return monitorMapper.deleteExpiredMonitors(days);
        } catch (Exception e) {
            log.error("清理过期监控记录失败", e);
            throw new RuntimeException("清理过期监控记录失败：" + e.getMessage());
        }
    }

    @Override
    public void exportMonitorList(SystemMonitorQueryVO queryVO, HttpServletResponse response) {
        try {
            List<SystemMonitor> monitorList = monitorMapper.selectMonitorListForExport(queryVO);

            // 设置导出的列标题
            String[] headers = {
                "监控类型", "监控项目", "监控值", "监控单位", "监控状态", "告警级别", 
                "告警消息", "服务器IP", "服务器名称", "应用名称", "监控时间", 
                "处理状态", "处理人", "处理时间", "处理备注"
            };

            ExcelUtil excelUtil = new ExcelUtil("系统监控记录列表", headers);

            // 添加数据行
            for (int i = 0; i < monitorList.size(); i++) {
                SystemMonitor monitor = monitorList.get(i);
                Object[] row = {
                    getMonitorTypeLabel(monitor.getMonitorType()),
                    monitor.getMonitorItem(),
                    monitor.getMonitorValue(),
                    monitor.getMonitorUnit(),
                    getMonitorStatusLabel(monitor.getMonitorStatus()),
                    getAlertLevelLabel(monitor.getAlertLevel()),
                    monitor.getAlertMessage(),
                    monitor.getServerIp(),
                    monitor.getServerName(),
                    monitor.getApplicationName(),
                    monitor.getMonitorTime(),
                    getProcessStatusLabel(monitor.getProcessStatus()),
                    monitor.getProcessBy(),
                    monitor.getProcessTime(),
                    monitor.getProcessRemark()
                };
                excelUtil.addRow(i + 1, row);
            }

            excelUtil.exportExcel(response, "系统监控记录列表.xls");
        } catch (Exception e) {
            log.error("导出系统监控记录列表失败", e);
            throw new RuntimeException("导出系统监控记录列表失败：" + e.getMessage());
        }
    }

    @Override
    public String getMonitorTypeLabel(String monitorType) {
        if (StringUtils.isEmpty(monitorType)) {
            return "";
        }
        switch (monitorType) {
            case SystemMonitor.TYPE_PERFORMANCE:
                return "性能监控";
            case SystemMonitor.TYPE_SYSTEM:
                return "系统监控";
            case SystemMonitor.TYPE_APPLICATION:
                return "应用监控";
            case SystemMonitor.TYPE_DATABASE:
                return "数据库监控";
            case SystemMonitor.TYPE_NETWORK:
                return "网络监控";
            case SystemMonitor.TYPE_SECURITY:
                return "安全监控";
            case SystemMonitor.TYPE_BUSINESS:
                return "业务监控";
            case SystemMonitor.TYPE_ERROR:
                return "错误监控";
            default:
                return monitorType;
        }
    }

    @Override
    public String getMonitorStatusLabel(String monitorStatus) {
        if (StringUtils.isEmpty(monitorStatus)) {
            return "";
        }
        switch (monitorStatus) {
            case SystemMonitor.STATUS_NORMAL:
                return "正常";
            case SystemMonitor.STATUS_WARNING:
                return "警告";
            case SystemMonitor.STATUS_CRITICAL:
                return "严重";
            case SystemMonitor.STATUS_ERROR:
                return "错误";
            case SystemMonitor.STATUS_UNKNOWN:
                return "未知";
            default:
                return monitorStatus;
        }
    }

    @Override
    public String getAlertLevelLabel(String alertLevel) {
        if (StringUtils.isEmpty(alertLevel)) {
            return "";
        }
        switch (alertLevel) {
            case SystemMonitor.ALERT_LEVEL_INFO:
                return "信息";
            case SystemMonitor.ALERT_LEVEL_LOW:
                return "低";
            case SystemMonitor.ALERT_LEVEL_MEDIUM:
                return "中";
            case SystemMonitor.ALERT_LEVEL_HIGH:
                return "高";
            case SystemMonitor.ALERT_LEVEL_CRITICAL:
                return "严重";
            default:
                return alertLevel;
        }
    }

    @Override
    public String getProcessStatusLabel(String processStatus) {
        if (StringUtils.isEmpty(processStatus)) {
            return "";
        }
        switch (processStatus) {
            case SystemMonitor.PROCESS_STATUS_PENDING:
                return "待处理";
            case SystemMonitor.PROCESS_STATUS_PROCESSING:
                return "处理中";
            case SystemMonitor.PROCESS_STATUS_RESOLVED:
                return "已解决";
            case SystemMonitor.PROCESS_STATUS_IGNORED:
                return "已忽略";
            case SystemMonitor.PROCESS_STATUS_ESCALATED:
                return "已升级";
            default:
                return processStatus;
        }
    }

    @Override
    public SystemMonitor getLatestMonitorData(String monitorType, String monitorItem, String serverIp) {
        return monitorMapper.selectLatestMonitorData(monitorType, monitorItem, serverIp);
    }

    @Override
    public List<String> getMonitorItems(String monitorType) {
        return monitorMapper.selectMonitorItems(monitorType);
    }

    @Override
    public List<Map<String, Object>> getServerList() {
        return monitorMapper.selectServerList();
    }

    @Override
    public List<Map<String, Object>> getApplicationList() {
        return monitorMapper.selectApplicationList();
    }
}
