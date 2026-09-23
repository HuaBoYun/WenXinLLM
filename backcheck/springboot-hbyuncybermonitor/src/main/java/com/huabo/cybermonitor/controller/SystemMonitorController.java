package com.huabo.cybermonitor.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.SystemMonitor;
import com.huabo.cybermonitor.service.ISystemMonitorService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.SystemMonitorQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 系统监控记录管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="系统监控记录管理",description="系统监控记录管理")
@RestController
@RequestMapping("/v1/system/monitor")
public class SystemMonitorController {

	private static final Logger log = LoggerFactory.getLogger(SystemMonitorController.class);

    @Autowired
    private ISystemMonitorService monitorService;

    @Operation(summary = "分页查询系统监控记录列表")
    @PostMapping("/list")
    public R<PageResult<SystemMonitor>> getMonitorList(@RequestBody SystemMonitorQueryVO queryVO) {
        try {
            IPage<SystemMonitor> page = monitorService.getMonitorList(queryVO);
            PageResult<SystemMonitor> pageResult = new PageResult<>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());
            
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询系统监控记录列表失败", e);
            return R.fail("查询系统监控记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取系统监控记录详情")
    @PostMapping("/detail")
    public R<SystemMonitor> getMonitorDetail(@RequestBody Map<String, String> params) {
        try {
            String monitorId = params.get("monitorId");
            if (monitorId == null || monitorId.trim().isEmpty()) {
                return R.fail("监控记录ID不能为空");
            }
            
            SystemMonitor monitor = monitorService.getMonitorDetail(monitorId);
            return R.success(monitor);
        } catch (Exception e) {
            log.error("获取系统监控记录详情失败", e);
            return R.fail("获取系统监控记录详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增系统监控记录")
    @PostMapping("/add")
    public R<String> addMonitor(@RequestBody SystemMonitor monitor) {
        try {
            boolean success = monitorService.addMonitor(monitor);
            if (success) {
                return R.success("新增系统监控记录成功");
            } else {
                return R.fail("新增系统监控记录失败");
            }
        } catch (Exception e) {
            log.error("新增系统监控记录失败", e);
            return R.fail("新增系统监控记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新系统监控记录")
    @PostMapping("/update")
    public R<String> updateMonitor(@RequestBody SystemMonitor monitor) {
        try {
            if (monitor.getMonitorId() == null || monitor.getMonitorId().trim().isEmpty()) {
                return R.fail("监控记录ID不能为空");
            }
            
            boolean success = monitorService.updateMonitor(monitor);
            if (success) {
                return R.success("更新系统监控记录成功");
            } else {
                return R.fail("更新系统监控记录失败");
            }
        } catch (Exception e) {
            log.error("更新系统监控记录失败", e);
            return R.fail("更新系统监控记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除系统监控记录")
    @PostMapping("/delete")
    public R<String> deleteMonitor(@RequestBody Map<String, String> params) {
        try {
            String monitorId = params.get("monitorId");
            if (monitorId == null || monitorId.trim().isEmpty()) {
                return R.fail("监控记录ID不能为空");
            }
            
            boolean success = monitorService.deleteMonitor(monitorId);
            if (success) {
                return R.success("删除系统监控记录成功");
            } else {
                return R.fail("删除系统监控记录失败");
            }
        } catch (Exception e) {
            log.error("删除系统监控记录失败", e);
            return R.fail("删除系统监控记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除系统监控记录")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteMonitor(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> monitorIds = params.get("monitorIds");
            if (monitorIds == null || monitorIds.isEmpty()) {
                return R.fail("监控记录ID列表不能为空");
            }
            
            boolean success = monitorService.batchDeleteMonitor(monitorIds);
            if (success) {
                return R.success("批量删除系统监控记录成功");
            } else {
                return R.fail("批量删除系统监控记录失败");
            }
        } catch (Exception e) {
            log.error("批量删除系统监控记录失败", e);
            return R.fail("批量删除系统监控记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据监控类型查询监控记录列表")
    @PostMapping("/list-by-type")
    public R<List<SystemMonitor>> getMonitorsByType(@RequestBody Map<String, String> params) {
        try {
            String monitorType = params.get("monitorType");
            if (monitorType == null || monitorType.trim().isEmpty()) {
                return R.fail("监控类型不能为空");
            }
            
            List<SystemMonitor> monitors = monitorService.getMonitorsByType(monitorType);
            return R.success(monitors);
        } catch (Exception e) {
            log.error("根据监控类型查询监控记录列表失败", e);
            return R.fail("根据监控类型查询监控记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据监控状态查询监控记录列表")
    @PostMapping("/list-by-status")
    public R<List<SystemMonitor>> getMonitorsByStatus(@RequestBody Map<String, String> params) {
        try {
            String monitorStatus = params.get("monitorStatus");
            if (monitorStatus == null || monitorStatus.trim().isEmpty()) {
                return R.fail("监控状态不能为空");
            }
            
            List<SystemMonitor> monitors = monitorService.getMonitorsByStatus(monitorStatus);
            return R.success(monitors);
        } catch (Exception e) {
            log.error("根据监控状态查询监控记录列表失败", e);
            return R.fail("根据监控状态查询监控记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据告警级别查询监控记录列表")
    @PostMapping("/list-by-alert-level")
    public R<List<SystemMonitor>> getMonitorsByAlertLevel(@RequestBody Map<String, String> params) {
        try {
            String alertLevel = params.get("alertLevel");
            if (alertLevel == null || alertLevel.trim().isEmpty()) {
                return R.fail("告警级别不能为空");
            }
            
            List<SystemMonitor> monitors = monitorService.getMonitorsByAlertLevel(alertLevel);
            return R.success(monitors);
        } catch (Exception e) {
            log.error("根据告警级别查询监控记录列表失败", e);
            return R.fail("根据告警级别查询监控记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据服务器查询监控记录列表")
    @PostMapping("/list-by-server")
    public R<List<SystemMonitor>> getMonitorsByServer(@RequestBody Map<String, String> params) {
        try {
            String serverIp = params.get("serverIp");
            if (serverIp == null || serverIp.trim().isEmpty()) {
                return R.fail("服务器IP不能为空");
            }
            
            List<SystemMonitor> monitors = monitorService.getMonitorsByServer(serverIp);
            return R.success(monitors);
        } catch (Exception e) {
            log.error("根据服务器查询监控记录列表失败", e);
            return R.fail("根据服务器查询监控记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据应用查询监控记录列表")
    @PostMapping("/list-by-application")
    public R<List<SystemMonitor>> getMonitorsByApplication(@RequestBody Map<String, String> params) {
        try {
            String applicationName = params.get("applicationName");
            if (applicationName == null || applicationName.trim().isEmpty()) {
                return R.fail("应用名称不能为空");
            }
            
            List<SystemMonitor> monitors = monitorService.getMonitorsByApplication(applicationName);
            return R.success(monitors);
        } catch (Exception e) {
            log.error("根据应用查询监控记录列表失败", e);
            return R.fail("根据应用查询监控记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询未处理的监控记录列表")
    @PostMapping("/unprocessed")
    public R<List<SystemMonitor>> getUnprocessedMonitors() {
        try {
            List<SystemMonitor> monitors = monitorService.getUnprocessedMonitors();
            return R.success(monitors);
        } catch (Exception e) {
            log.error("查询未处理的监控记录列表失败", e);
            return R.fail("查询未处理的监控记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询高优先级告警记录列表")
    @PostMapping("/high-priority-alerts")
    public R<List<SystemMonitor>> getHighPriorityAlerts() {
        try {
            List<SystemMonitor> monitors = monitorService.getHighPriorityAlerts();
            return R.success(monitors);
        } catch (Exception e) {
            log.error("查询高优先级告警记录列表失败", e);
            return R.fail("查询高优先级告警记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "处理监控记录")
    @PostMapping("/process")
    public R<String> processMonitor(@RequestBody Map<String, String> params) {
        try {
            String monitorId = params.get("monitorId");
            String processStatus = params.get("processStatus");
            String processBy = params.get("processBy");
            String processRemark = params.get("processRemark");
            
            if (monitorId == null || monitorId.trim().isEmpty()) {
                return R.fail("监控记录ID不能为空");
            }
            if (processStatus == null || processStatus.trim().isEmpty()) {
                return R.fail("处理状态不能为空");
            }
            
            boolean success = monitorService.processMonitor(monitorId, processStatus, processBy, processRemark);
            if (success) {
                return R.success("处理监控记录成功");
            } else {
                return R.fail("处理监控记录失败");
            }
        } catch (Exception e) {
            log.error("处理监控记录失败", e);
            return R.fail("处理监控记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量处理监控记录")
    @PostMapping("/batch-process")
    public R<String> batchProcessMonitor(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> monitorIds = (List<String>) params.get("monitorIds");
            String processStatus = (String) params.get("processStatus");
            String processBy = (String) params.get("processBy");
            String processRemark = (String) params.get("processRemark");
            
            if (monitorIds == null || monitorIds.isEmpty()) {
                return R.fail("监控记录ID列表不能为空");
            }
            if (processStatus == null || processStatus.trim().isEmpty()) {
                return R.fail("处理状态不能为空");
            }
            
            boolean success = monitorService.batchProcessMonitor(monitorIds, processStatus, processBy, processRemark);
            if (success) {
                return R.success("批量处理监控记录成功");
            } else {
                return R.fail("批量处理监控记录失败");
            }
        } catch (Exception e) {
            log.error("批量处理监控记录失败", e);
            return R.fail("批量处理监控记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "记录监控数据")
    @PostMapping("/record-data")
    public R<String> recordMonitorData(@RequestBody Map<String, String> params) {
        try {
            String monitorType = params.get("monitorType");
            String monitorItem = params.get("monitorItem");
            String monitorValue = params.get("monitorValue");
            String monitorUnit = params.get("monitorUnit");
            String thresholdConfig = params.get("thresholdConfig");
            String serverIp = params.get("serverIp");
            String serverName = params.get("serverName");
            String applicationName = params.get("applicationName");
            
            if (monitorType == null || monitorType.trim().isEmpty()) {
                return R.fail("监控类型不能为空");
            }
            if (monitorItem == null || monitorItem.trim().isEmpty()) {
                return R.fail("监控项目不能为空");
            }
            if (monitorValue == null || monitorValue.trim().isEmpty()) {
                return R.fail("监控值不能为空");
            }
            
            boolean success = monitorService.recordMonitorData(monitorType, monitorItem, monitorValue,
                    monitorUnit, thresholdConfig, serverIp, serverName, applicationName);
            if (success) {
                return R.success("记录监控数据成功");
            } else {
                return R.fail("记录监控数据失败");
            }
        } catch (Exception e) {
            log.error("记录监控数据失败", e);
            return R.fail("记录监控数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取监控统计信息")
    @PostMapping("/statistics/overview")
    public R<Map<String, Object>> getMonitorStatistics() {
        try {
            Map<String, Object> statistics = monitorService.getMonitorStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取监控统计信息失败", e);
            return R.fail("获取监控统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取监控类型分布统计")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getMonitorTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = monitorService.getMonitorTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取监控类型分布统计失败", e);
            return R.fail("获取监控类型分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取监控状态分布统计")
    @PostMapping("/statistics/status-distribution")
    public R<List<Map<String, Object>>> getMonitorStatusDistribution() {
        try {
            List<Map<String, Object>> distribution = monitorService.getMonitorStatusDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取监控状态分布统计失败", e);
            return R.fail("获取监控状态分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取告警级别分布统计")
    @PostMapping("/statistics/alert-level-distribution")
    public R<List<Map<String, Object>>> getAlertLevelDistribution() {
        try {
            List<Map<String, Object>> distribution = monitorService.getAlertLevelDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取告警级别分布统计失败", e);
            return R.fail("获取告警级别分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取服务器监控分布统计")
    @PostMapping("/statistics/server-distribution")
    public R<List<Map<String, Object>>> getServerDistribution() {
        try {
            List<Map<String, Object>> distribution = monitorService.getServerDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取服务器监控分布统计失败", e);
            return R.fail("获取服务器监控分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取应用监控分布统计")
    @PostMapping("/statistics/application-distribution")
    public R<List<Map<String, Object>>> getApplicationDistribution() {
        try {
            List<Map<String, Object>> distribution = monitorService.getApplicationDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取应用监控分布统计失败", e);
            return R.fail("获取应用监控分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取监控趋势统计")
    @PostMapping("/statistics/monitor-trend")
    public R<List<Map<String, Object>>> getMonitorTrend(@RequestBody Map<String, String> params) {
        try {
            String startDate = params.get("startDate");
            String endDate = params.get("endDate");
            
            List<Map<String, Object>> trend = monitorService.getMonitorTrend(startDate, endDate);
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取监控趋势统计失败", e);
            return R.fail("获取监控趋势统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取告警趋势统计")
    @PostMapping("/statistics/alert-trend")
    public R<List<Map<String, Object>>> getAlertTrend(@RequestBody Map<String, String> params) {
        try {
            String startDate = params.get("startDate");
            String endDate = params.get("endDate");
            
            List<Map<String, Object>> trend = monitorService.getAlertTrend(startDate, endDate);
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取告警趋势统计失败", e);
            return R.fail("获取告警趋势统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取性能监控统计")
    @PostMapping("/statistics/performance")
    public R<Map<String, Object>> getPerformanceStatistics() {
        try {
            Map<String, Object> statistics = monitorService.getPerformanceStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取性能监控统计失败", e);
            return R.fail("获取性能监控统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取系统健康状况")
    @PostMapping("/health-status")
    public R<Map<String, Object>> getSystemHealthStatus() {
        try {
            Map<String, Object> healthStatus = monitorService.getSystemHealthStatus();
            return R.success(healthStatus);
        } catch (Exception e) {
            log.error("获取系统健康状况失败", e);
            return R.fail("获取系统健康状况失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取实时监控数据")
    @PostMapping("/real-time-data")
    public R<Map<String, Object>> getRealTimeMonitorData(@RequestBody Map<String, String> params) {
        try {
            String monitorType = params.get("monitorType");
            String serverIp = params.get("serverIp");
            
            Map<String, Object> data = monitorService.getRealTimeMonitorData(monitorType, serverIp);
            return R.success(data);
        } catch (Exception e) {
            log.error("获取实时监控数据失败", e);
            return R.fail("获取实时监控数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取监控仪表板数据")
    @PostMapping("/dashboard")
    public R<Map<String, Object>> getMonitorDashboardData() {
        try {
            Map<String, Object> dashboard = monitorService.getMonitorDashboardData();
            return R.success(dashboard);
        } catch (Exception e) {
            log.error("获取监控仪表板数据失败", e);
            return R.fail("获取监控仪表板数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "清理过期的监控记录")
    @PostMapping("/clean-expired")
    public R<String> cleanExpiredMonitors(@RequestBody Map<String, Integer> params) {
        try {
            Integer days = params.get("days");
            if (days == null || days <= 0) {
                days = 30; // 默认保留30天
            }
            
            Integer cleanCount = monitorService.cleanExpiredMonitors(days);
            return R.success("清理过期监控记录成功，共清理" + cleanCount + "条记录");
        } catch (Exception e) {
            log.error("清理过期监控记录失败", e);
            return R.fail("清理过期监控记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取监控项目列表")
    @PostMapping("/monitor-items")
    public R<List<String>> getMonitorItems(@RequestBody Map<String, String> params) {
        try {
            String monitorType = params.get("monitorType");
            if (monitorType == null || monitorType.trim().isEmpty()) {
                return R.fail("监控类型不能为空");
            }
            
            List<String> items = monitorService.getMonitorItems(monitorType);
            return R.success(items);
        } catch (Exception e) {
            log.error("获取监控项目列表失败", e);
            return R.fail("获取监控项目列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取服务器列表")
    @PostMapping("/server-list")
    public R<List<Map<String, Object>>> getServerList() {
        try {
            List<Map<String, Object>> servers = monitorService.getServerList();
            return R.success(servers);
        } catch (Exception e) {
            log.error("获取服务器列表失败", e);
            return R.fail("获取服务器列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取应用列表")
    @PostMapping("/application-list")
    public R<List<Map<String, Object>>> getApplicationList() {
        try {
            List<Map<String, Object>> applications = monitorService.getApplicationList();
            return R.success(applications);
        } catch (Exception e) {
            log.error("获取应用列表失败", e);
            return R.fail("获取应用列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出监控记录列表")
    @PostMapping("/export")
    public void exportMonitorList(@RequestBody SystemMonitorQueryVO queryVO, HttpServletResponse response) {
        try {
            monitorService.exportMonitorList(queryVO, response);
        } catch (Exception e) {
            log.error("导出监控记录列表失败", e);
            throw new RuntimeException("导出监控记录列表失败：" + e.getMessage());
        }
    }
}
