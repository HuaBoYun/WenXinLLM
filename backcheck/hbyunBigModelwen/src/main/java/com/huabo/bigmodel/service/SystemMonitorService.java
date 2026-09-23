package com.huabo.bigmodel.service;

import com.huabo.bigmodel.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 系统监控服务接口
 */
public interface SystemMonitorService {

    /**
     * 获取所有注册的微服务实例列表
     */
    List<ServiceInstanceVO> getAllServiceInstances();

    /**
     * 获取服务健康汇总
     */
    HealthSummaryVO getHealthSummary();

    /**
     * 获取服务器基础信息（静态）
     */
    ServerInfoVO getServerInfo();

    /**
     * 获取服务器实时资源指标
     */
    ServerMetricsVO getServerMetrics();

    /**
     * 获取磁盘分区信息
     */
    List<DiskInfoVO> getDiskInfos();

    /**
     * 获取JVM详情
     */
    Map<String, Object> getJvmDetails();

    /**
     * 获取所有网络接口信息（含实时网速）
     */
    List<NetworkInfoVO> getNetworkInfos();

    /**
     * 获取数据库连接池状态
     */
    DatabasePoolVO getDatabasePoolStatus();

    /**
     * 获取系统告警列表（CPU/内存/磁盘/JVM超阈值告警）
     */
    List<SystemAlertVO> getSystemAlerts();
}
