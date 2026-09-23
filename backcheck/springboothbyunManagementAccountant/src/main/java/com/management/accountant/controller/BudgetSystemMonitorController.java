 package com.management.accountant.controller;
 
 import com.management.accountant.oracle.entity.budget.BudgetSystemMonitorEntity;
 import com.management.accountant.service.BudgetSystemMonitorService;
 import com.management.accountant.util.MyJsonBean;
 import com.management.accountant.util.PageResult;
 import io.swagger.v3.oas.annotations.Operation;
 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiOperation;
 import io.swagger.annotations.ApiParam;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.web.bind.annotation.*;
 
 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletResponse;
 import java.lang.management.ManagementFactory;
 import java.lang.management.MemoryMXBean;
 import java.lang.management.RuntimeMXBean;
 import java.lang.management.OperatingSystemMXBean;
 import java.io.File;
 import java.math.BigDecimal;
 import java.text.SimpleDateFormat;
 import java.util.*;
 import javax.sql.DataSource;
 import org.springframework.beans.factory.annotation.Autowired;
 
 @RestController
 @Api(tags = {"NCV65全面预算-系统监控"})
 @RequestMapping(value = "/accountant/budget/system-monitor")
 @Slf4j
 public class BudgetSystemMonitorController {
     private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
 
     @Resource
     private BudgetSystemMonitorService monitorService;

     @Autowired(required = false)
     private DataSource dataSource;
 
     @Operation(summary = "获取系统统计数据")
     @ApiOperation("获取系统统计数据")
     @GetMapping("/stats")
     public MyJsonBean<Map<String, Object>> getSystemStats() {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             Map<String, Object> stats = monitorService.getStats();
             MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
             RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
             OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

             // CPU使用率 - 通过系统负载估算
             double systemLoadAvg = osBean.getSystemLoadAverage();
             int availableProcessors = osBean.getAvailableProcessors();
             double cpuUsage = systemLoadAvg >= 0 ? Math.min(100, (systemLoadAvg / availableProcessors) * 100) : 35;
             stats.put("cpuUsage", Math.round(cpuUsage));

             // 内存使用率
             long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
             long heapMax = memoryBean.getHeapMemoryUsage().getMax();
             double memoryUsage = (double) heapUsed / heapMax * 100;
             stats.put("memoryUsage", Math.round(memoryUsage));

             // 磁盘使用率
             File root = new File("/");
             long totalSpace = root.getTotalSpace();
             long freeSpace = root.getFreeSpace();
             double diskUsage = totalSpace > 0 ? (double)(totalSpace - freeSpace) / totalSpace * 100 : 0;
             stats.put("diskUsage", Math.round(diskUsage));

             // 健康度评分 - 基于CPU、内存、磁盘、错误数综合计算
             int errorCount = stats.get("errorCount") != null ? Integer.parseInt(stats.get("errorCount").toString()) : 0;
             int warningCount = stats.get("warningCount") != null ? Integer.parseInt(stats.get("warningCount").toString()) : 0;
             double healthScore = 100;
             healthScore -= Math.max(0, cpuUsage - 50) * 0.3;
             healthScore -= Math.max(0, memoryUsage - 60) * 0.3;
             healthScore -= Math.max(0, diskUsage - 70) * 0.2;
             healthScore -= errorCount * 5;
             healthScore -= warningCount * 2;
             healthScore = Math.max(0, Math.min(100, healthScore));
             stats.put("healthScore", Math.round(healthScore));

             stats.put("uptime", formatUptime(runtimeBean.getUptime()));
             stats.put("threadCount", ManagementFactory.getThreadMXBean().getThreadCount());
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(stats);
         } catch (Exception e) {
             log.error("获取系统统计数据异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取服务状态列表")
     @ApiOperation("获取服务状态列表")
     @GetMapping("/service-status")
     public MyJsonBean<PageResult<BudgetSystemMonitorEntity>> getServiceStatus(
             @RequestParam(required = false, defaultValue = "1") Integer pageNum,
             @RequestParam(required = false, defaultValue = "10") Integer pageSize,
             @RequestParam(required = false) String monitorType,
             @RequestParam(required = false) String serviceStatus) {
         MyJsonBean<PageResult<BudgetSystemMonitorEntity>> result = new MyJsonBean<>();
         try {
             Map<String, Object> params = new HashMap<>();
             params.put("pageNum", pageNum);
             params.put("pageSize", pageSize);
             if (monitorType != null && monitorType.length() > 0) {
                 params.put("monitorType", monitorType);
             }
             if (serviceStatus != null && serviceStatus.length() > 0) {
                 params.put("serviceStatus", serviceStatus);
             }
             PageResult<BudgetSystemMonitorEntity> pageResult = monitorService.getPage(params);
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(pageResult);
         } catch (Exception e) {
             log.error("获取服务状态异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取监控详情")
     @ApiOperation("获取监控详情")
     @GetMapping("/detail/{id}")
     public MyJsonBean<BudgetSystemMonitorEntity> getMonitorDetail(
             @ApiParam(value = "监控ID", required = true) @PathVariable String id) {
         MyJsonBean<BudgetSystemMonitorEntity> result = new MyJsonBean<>();
         try {
             BudgetSystemMonitorEntity monitor = monitorService.getById(id);
             if (monitor != null) {
                 result.setCode(1);
                 result.setMsg("查询成功");
                 result.setData(monitor);
             } else {
                 result.setCode(0);
                 result.setMsg("监控记录不存在");
             }
         } catch (Exception e) {
             log.error("获取监控详情异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取数据库状态")
     @ApiOperation("获取数据库状态")
     @GetMapping("/database-status")
     public MyJsonBean<Map<String, Object>> getDatabaseStatus() {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             Map<String, Object> dbStatus = new HashMap<>();

             // 从数据库获取最新的DATABASE类型监控记录
             Map<String, Object> params = new HashMap<>();
             params.put("monitorType", "DATABASE");
             params.put("pageNum", 1);
             params.put("pageSize", 1);
             PageResult<BudgetSystemMonitorEntity> dbMonitors = monitorService.getPage(params);

             if (dbMonitors.getList() != null && !dbMonitors.getList().isEmpty()) {
                 BudgetSystemMonitorEntity latest = dbMonitors.getList().get(0);
                 dbStatus.put("connections", latest.getConnections() != null ? latest.getConnections() : 0);
                 dbStatus.put("activeConnections", latest.getActiveConnections() != null ? latest.getActiveConnections() : 0);
                 dbStatus.put("queriesPerSecond", latest.getQueriesPerSecond() != null ? latest.getQueriesPerSecond() : BigDecimal.ZERO);
                 dbStatus.put("slowQueries", latest.getSlowQueries() != null ? latest.getSlowQueries() : 0);
                 dbStatus.put("cacheHitRate", latest.getCacheHitRate() != null ? latest.getCacheHitRate() : BigDecimal.ZERO);
                 dbStatus.put("lockWaits", latest.getLockWaits() != null ? latest.getLockWaits() : 0);
                 dbStatus.put("databaseSize", latest.getDatabaseSize() != null ? latest.getDatabaseSize() : "0MB");
                 dbStatus.put("tablespaceUsage", latest.getTablespaceUsage() != null ? latest.getTablespaceUsage() : BigDecimal.ZERO);
             } else {
                 // 尝试从DataSource获取真实连接池信息
                 int activeConn = 0;
                 int totalConn = 0;
                 if (dataSource != null) {
                     try {
                         java.sql.Connection conn = dataSource.getConnection();
                         java.sql.DatabaseMetaData metaData = conn.getMetaData();
                         dbStatus.put("databaseSize", metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
                         conn.close();
                     } catch (Exception ex) {
                         log.warn("获取数据库元信息失败", ex);
                     }
                 }
                 dbStatus.put("connections", totalConn);
                 dbStatus.put("activeConnections", activeConn);
                 dbStatus.put("queriesPerSecond", 0);
                 dbStatus.put("slowQueries", 0);
                 dbStatus.put("cacheHitRate", 0);
                 dbStatus.put("lockWaits", 0);
                 if (!dbStatus.containsKey("databaseSize")) {
                     dbStatus.put("databaseSize", "未知");
                 }
                 dbStatus.put("tablespaceUsage", 0);
             }

             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(dbStatus);
         } catch (Exception e) {
             log.error("获取数据库状态异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取JVM状态")
     @ApiOperation("获取JVM状态")
     @GetMapping("/jvm-status")
     public MyJsonBean<Map<String, Object>> getJvmStatus() {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
             RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
             Map<String, Object> jvmStatus = new HashMap<>();
             jvmStatus.put("heapMemoryUsed", formatBytes(memoryBean.getHeapMemoryUsage().getUsed()));
             jvmStatus.put("heapMemoryTotal", formatBytes(memoryBean.getHeapMemoryUsage().getMax()));
             jvmStatus.put("nonHeapMemoryUsed", formatBytes(memoryBean.getNonHeapMemoryUsage().getUsed()));
             jvmStatus.put("gcCount", ManagementFactory.getGarbageCollectorMXBeans().stream()
                     .mapToLong(gc -> gc.getCollectionCount()).sum());
             jvmStatus.put("gcTime", ManagementFactory.getGarbageCollectorMXBeans().stream()
                     .mapToLong(gc -> gc.getCollectionTime()).sum());
             jvmStatus.put("threadCount", ManagementFactory.getThreadMXBean().getThreadCount());
             jvmStatus.put("classLoadedCount", ManagementFactory.getClassLoadingMXBean().getLoadedClassCount());
             jvmStatus.put("uptime", formatUptime(runtimeBean.getUptime()));
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(jvmStatus);
         } catch (Exception e) {
             log.error("获取JVM状态异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取告警列表")
     @ApiOperation("获取告警列表")
     @GetMapping("/alerts")
     public MyJsonBean<PageResult<BudgetSystemMonitorEntity>> getAlertList(
             @RequestParam(required = false, defaultValue = "1") Integer pageNum,
             @RequestParam(required = false, defaultValue = "10") Integer pageSize,
             @RequestParam(required = false) String alertLevel,
             @RequestParam(required = false) String status) {
         MyJsonBean<PageResult<BudgetSystemMonitorEntity>> result = new MyJsonBean<>();
         try {
             Map<String, Object> params = new HashMap<>();
             params.put("pageNum", pageNum);
             params.put("pageSize", pageSize);
             if (alertLevel != null && alertLevel.length() > 0) {
                 params.put("alertLevel", alertLevel);
             }
             if (status != null && status.length() > 0) {
                 params.put("status", status);
             }
             PageResult<BudgetSystemMonitorEntity> pageResult = monitorService.getAlerts(params);
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(pageResult);
         } catch (Exception e) {
             log.error("获取告警列表异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "解决告警")
     @ApiOperation("解决告警")
     @PutMapping("/alerts/{alertId}/resolve")
     public MyJsonBean<Void> resolveAlert(
             @ApiParam(value = "告警ID", required = true) @PathVariable String alertId) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             BudgetSystemMonitorEntity monitor = monitorService.getById(alertId);
             if (monitor == null) {
                 result.setCode(0);
                 result.setMsg("告警记录不存在");
                 return result;
             }
             monitor.setStatus("RESOLVED");
             monitorService.updateMonitor(monitor);
             log.info("告警已解决: {}, monitorName: {}", alertId, monitor.getMonitorName());
             result.setCode(1);
             result.setMsg("告警已标记为已解决");
         } catch (Exception e) {
             log.error("解决告警异常", e);
             result.setCode(0);
             result.setMsg("操作失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "导出监控报告")
     @ApiOperation("导出监控报告")
     @GetMapping("/export")
     public void exportMonitorReport(HttpServletResponse response) {
         try {
             Map<String, Object> params = new HashMap<>();
             params.put("pageNum", 1);
             params.put("pageSize", 1000);
             PageResult<BudgetSystemMonitorEntity> pageResult = monitorService.getPage(params);
             response.setContentType("application/vnd.ms-excel");
             response.setHeader("Content-Disposition", "attachment;filename=monitor_report.csv");
             StringBuilder sb = new StringBuilder();
             sb.append("监控ID,监控名称,监控类型,服务名称,服务状态,CPU使用率,内存使用率,磁盘使用率,响应时间,请求数,错误数,活跃连接数,告警级别,监控时间\n");
             if (pageResult.getList() != null) {
                 for (BudgetSystemMonitorEntity item : pageResult.getList()) {
                     sb.append(item.getMonitorId()).append(",");
                     sb.append(item.getMonitorName()).append(",");
                     sb.append(item.getMonitorType()).append(",");
                     sb.append(item.getServiceName()).append(",");
                     sb.append(item.getServiceStatus()).append(",");
                     sb.append(item.getCpuUsage()).append(",");
                     sb.append(item.getMemoryUsage()).append(",");
                     sb.append(item.getDiskUsage()).append(",");
                     sb.append(item.getResponseTime()).append(",");
                     sb.append(item.getRequestCount()).append(",");
                     sb.append(item.getErrorCount()).append(",");
                     sb.append(item.getActiveConnections()).append(",");
                     sb.append(item.getAlertLevel()).append(",");
                     sb.append(item.getMonitorTime()).append("\n");
                 }
             }
             response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
             response.getOutputStream().flush();
         } catch (Exception e) {
             log.error("导出监控报告异常", e);
         }
     }
 
     @Operation(summary = "获取性能数据")
     @ApiOperation("获取性能数据")
     @GetMapping("/performance")
     public MyJsonBean<Map<String, Object>> getPerformanceData(
             @RequestParam(required = false, defaultValue = "24") Integer hours) {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             Map<String, Object> params = new HashMap<>();
             params.put("pageNum", 1);
             params.put("pageSize", hours);
             params.put("monitorType", "PERFORMANCE");
             PageResult<BudgetSystemMonitorEntity> pageResult = monitorService.getPage(params);
             Map<String, Object> data = new HashMap<>();
             List<String> timeLabels = new ArrayList<>();
             List<Object> cpuData = new ArrayList<>();
             List<Object> memoryData = new ArrayList<>();
             SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
             if (pageResult.getList() != null) {
                 for (BudgetSystemMonitorEntity m : pageResult.getList()) {
                     timeLabels.add(m.getMonitorTime() != null ? sdf.format(m.getMonitorTime()) : "");
                     cpuData.add(m.getCpuUsage());
                     memoryData.add(m.getMemoryUsage());
                 }
             }
             data.put("timeLabels", timeLabels);
             data.put("cpuData", cpuData);
             data.put("memoryData", memoryData);
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(data);
         } catch (Exception e) {
             log.error("获取性能数据异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取网络流量数据")
     @ApiOperation("获取网络流量数据")
     @GetMapping("/network-traffic")
     public MyJsonBean<Map<String, Object>> getNetworkTraffic(
             @RequestParam(required = false, defaultValue = "24") Integer hours) {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             List<BudgetSystemMonitorEntity> networkData = monitorService.getNetworkTrafficData(hours);
             Map<String, Object> data = new HashMap<>();
             List<String> timeLabels = new ArrayList<>();
             List<Object> inboundData = new ArrayList<>();
             List<Object> outboundData = new ArrayList<>();
             SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
             if (networkData != null) {
                 for (BudgetSystemMonitorEntity m : networkData) {
                     timeLabels.add(m.getMonitorTime() != null ? sdf.format(m.getMonitorTime()) : "");
                     inboundData.add(m.getInboundTraffic() != null ? m.getInboundTraffic() : BigDecimal.ZERO);
                     outboundData.add(m.getOutboundTraffic() != null ? m.getOutboundTraffic() : BigDecimal.ZERO);
                 }
             }
             data.put("timeLabels", timeLabels);
             data.put("inboundData", inboundData);
             data.put("outboundData", outboundData);
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(data);
         } catch (Exception e) {
             log.error("获取网络流量数据异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "获取监控设置")
     @ApiOperation("获取监控设置")
     @GetMapping("/settings")
     public MyJsonBean<Map<String, Object>> getMonitorSettings() {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             Map<String, Object> settings = monitorService.getMonitorSettings();
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(settings);
         } catch (Exception e) {
             log.error("获取监控设置异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "保存监控设置")
     @ApiOperation("保存监控设置")
     @PostMapping("/settings")
     public MyJsonBean<Void> saveMonitorSettings(@RequestBody Map<String, Object> settings) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             monitorService.saveMonitorSettings(settings);
             result.setCode(1);
             result.setMsg("保存成功");
         } catch (Exception e) {
             log.error("保存监控设置异常", e);
             result.setCode(0);
             result.setMsg("保存失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "获取告警详情")
     @ApiOperation("获取告警详情")
     @GetMapping("/alerts/{alertId}/detail")
     public MyJsonBean<BudgetSystemMonitorEntity> getAlertDetail(
             @ApiParam(value = "告警ID", required = true) @PathVariable String alertId) {
         MyJsonBean<BudgetSystemMonitorEntity> result = new MyJsonBean<>();
         try {
             BudgetSystemMonitorEntity monitor = monitorService.getById(alertId);
             if (monitor != null) {
                 result.setCode(1);
                 result.setMsg("查询成功");
                 result.setData(monitor);
             } else {
                 result.setCode(0);
                 result.setMsg("告警记录不存在");
             }
         } catch (Exception e) {
             log.error("获取告警详情异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }

     private String formatUptime(long millis) {
         long seconds = millis / 1000;
         long minutes = seconds / 60;
         long hours = minutes / 60;
         long days = hours / 24;
         if (days > 0) {
             return days + "天" + (hours % 24) + "小时";
         } else if (hours > 0) {
             return hours + "小时" + (minutes % 60) + "分钟";
         } else {
             return minutes + "分钟";
         }
     }
 
     private String formatBytes(long bytes) {
         if (bytes >= 1024 * 1024 * 1024) {
             return String.format("%.1fGB", bytes / (1024.0 * 1024 * 1024));
         } else if (bytes >= 1024 * 1024) {
             return String.format("%.1fMB", bytes / (1024.0 * 1024));
         } else if (bytes >= 1024) {
             return String.format("%.1fKB", bytes / 1024.0);
         } else {
             return bytes + "B";
         }
     }
 }