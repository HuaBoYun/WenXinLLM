package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.dto.*;
import com.huabo.bigmodel.service.SystemMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 系统监控控制器
 * <p>提供微服务状态、服务器资源（CPU/内存/磁盘/JVM）的实时监控接口</p>
 */
@Slf4j
@RestController
@RequestMapping("/v1/monitor")
@Tag(name = "系统监控", description = "微服务状态与服务器资源监控接口")
public class SystemMonitorController {

    @Autowired
    private SystemMonitorService systemMonitorService;

    /**
     * 获取所有注册的微服务实例列表
     */
    @GetMapping("/services")
    @Operation(summary = "获取全部微服务实例列表", description = "从Eureka注册中心获取所有已注册服务的实例详情")
    public Result<List<ServiceInstanceVO>> getServices() {
        try {
            List<ServiceInstanceVO> list = systemMonitorService.getAllServiceInstances();
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取服务列表失败", e);
            return Result.error("获取服务列表失败: " + e.getMessage());
        }
    }

    /**
     * 服务健康汇总
     */
    @GetMapping("/healthSummary")
    @Operation(summary = "服务健康汇总", description = "统计在线/离线/总计服务数量及健康率")
    public Result<HealthSummaryVO> getHealthSummary() {
        try {
            HealthSummaryVO summary = systemMonitorService.getHealthSummary();
            return Result.success(summary);
        } catch (Exception e) {
            log.error("获取健康汇总失败", e);
            return Result.error("获取健康汇总失败: " + e.getMessage());
        }
    }

    /**
     * 服务器基础信息（静态数据，不常变化）
     */
    @GetMapping("/serverInfo")
    @Operation(summary = "服务器基础信息", description = "获取操作系统、CPU、内存、JVM等静态基础信息")
    public Result<ServerInfoVO> getServerInfo() {
        try {
            ServerInfoVO info = systemMonitorService.getServerInfo();
            return Result.success(info);
        } catch (Exception e) {
            log.error("获取服务器信息失败", e);
            return Result.error("获取服务器信息失败: " + e.getMessage());
        }
    }

    /**
     * 服务器实时资源指标（适合轮询刷新）
     */
    @GetMapping("/serverMetrics")
    @Operation(summary = "服务器实时指标", description = "获取CPU使用率、内存使用率、JVM堆使用等实时指标，适合前端轮询展示")
    public Result<ServerMetricsVO> getServerMetrics() {
        try {
            ServerMetricsVO metrics = systemMonitorService.getServerMetrics();
            return Result.success(metrics);
        } catch (Exception e) {
            log.error("获取服务器指标失败", e);
            return Result.error("获取服务器指标失败: " + e.getMessage());
        }
    }

    /**
     * 磁盘分区列表
     */
    @GetMapping("/disks")
    @Operation(summary = "磁盘分区信息", description = "获取所有磁盘分区的容量使用情况")
    public Result<List<DiskInfoVO>> getDisks() {
        try {
            List<DiskInfoVO> disks = systemMonitorService.getDiskInfos();
            return Result.success(disks);
        } catch (Exception e) {
            log.error("获取磁盘信息失败", e);
            return Result.error("获取磁盘信息失败: " + e.getMessage());
        }
    }

    /**
     * JVM详细信息
     */
    @GetMapping("/jvm")
    @Operation(summary = "JVM详情", description = "获取JVM堆内存、GC、线程等详细运行信息")
    public Result<Map<String, Object>> getJvmDetails() {
        try {
            Map<String, Object> details = systemMonitorService.getJvmDetails();
            return Result.success(details);
        } catch (Exception e) {
            log.error("获取JVM详情失败", e);
            return Result.error("获取JVM详情失败: " + e.getMessage());
        }
    }

    /**
     * 网络接口信息（含实时网速）
     */
    @GetMapping("/network")
    @Operation(summary = "网络接口信息", description = "获取所有物理网卡的IP/MAC/MTU、累计收发字节/包数及实时网速，自动过滤loopback回环接口")
    public Result<List<NetworkInfoVO>> getNetwork() {
        try {
            List<NetworkInfoVO> list = systemMonitorService.getNetworkInfos();
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取网络接口信息失败", e);
            return Result.error("获取网络接口信息失败: " + e.getMessage());
        }
    }

    /**
     * 数据库连接池状态
     */
    @GetMapping("/database")
    @Operation(summary = "数据库连接池状态", description = "获取达梦数据库Druid连接池的当前状态，包含活跃连接数、池化连接数、累计使用次数等")
    public Result<DatabasePoolVO> getDatabase() {
        try {
            DatabasePoolVO status = systemMonitorService.getDatabasePoolStatus();
            return Result.success(status);
        } catch (Exception e) {
            log.error("获取数据库连接池状态失败", e);
            return Result.error("获取数据库连接池状态失败: " + e.getMessage());
        }
    }

    /**
     * 系统告警列表
     */
    @GetMapping("/alerts")
    @Operation(summary = "系统告警列表", description = "检查CPU/内存/磁盘/JVM堆内存是否超过阈值，返回当前活跃告警。danger级别: CPU≥85%、内存≥85%、磁盘≥90%、JVM≥85%；warning级别: CPU≥70%、内存≥70%、磁盘≥70%、JVM≥70%")
    public Result<List<SystemAlertVO>> getAlerts() {
        try {
            List<SystemAlertVO> alerts = systemMonitorService.getSystemAlerts();
            return Result.success(alerts);
        } catch (Exception e) {
            log.error("获取系统告警失败", e);
            return Result.error("获取系统告警失败: " + e.getMessage());
        }
    }
}
