package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统监控记录实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_SYSTEM_MONITOR")
public class SystemMonitor {

    /**
     * 监控记录ID
     */
    @TableId(value = "MONITOR_ID", type = IdType.ASSIGN_UUID)
    private String monitorId;

    /**
     * 监控类型
     */
    private String monitorType;

    /**
     * 监控项目
     */
    private String monitorItem;

    /**
     * 监控值
     */
    private String monitorValue;

    /**
     * 监控单位
     */
    private String monitorUnit;

    /**
     * 监控状态
     */
    private String monitorStatus;

    /**
     * 阈值配置
     */
    private String thresholdConfig;

    /**
     * 告警级别
     */
    private String alertLevel;

    /**
     * 告警消息
     */
    private String alertMessage;

    /**
     * 服务器IP
     */
    private String serverIp;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 监控时间
     */
    private LocalDateTime monitorTime;

    /**
     * 处理状态
     */
    private String processStatus;

    /**
     * 处理人
     */
    private String processBy;

    /**
     * 处理时间
     */
    private LocalDateTime processTime;

    /**
     * 处理备注
     */
    private String processRemark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    // 监控类型常量
    public static final String TYPE_PERFORMANCE = "PERFORMANCE";
    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_APPLICATION = "APPLICATION";
    public static final String TYPE_DATABASE = "DATABASE";
    public static final String TYPE_NETWORK = "NETWORK";
    public static final String TYPE_SECURITY = "SECURITY";
    public static final String TYPE_BUSINESS = "BUSINESS";
    public static final String TYPE_ERROR = "ERROR";

    // 监控状态常量
    public static final String STATUS_NORMAL = "NORMAL";
    public static final String STATUS_WARNING = "WARNING";
    public static final String STATUS_CRITICAL = "CRITICAL";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_UNKNOWN = "UNKNOWN";

    // 告警级别常量
    public static final String ALERT_LEVEL_INFO = "INFO";
    public static final String ALERT_LEVEL_LOW = "LOW";
    public static final String ALERT_LEVEL_MEDIUM = "MEDIUM";
    public static final String ALERT_LEVEL_HIGH = "HIGH";
    public static final String ALERT_LEVEL_CRITICAL = "CRITICAL";

    // 处理状态常量
    public static final String PROCESS_STATUS_PENDING = "PENDING";
    public static final String PROCESS_STATUS_PROCESSING = "PROCESSING";
    public static final String PROCESS_STATUS_RESOLVED = "RESOLVED";
    public static final String PROCESS_STATUS_IGNORED = "IGNORED";
    public static final String PROCESS_STATUS_ESCALATED = "ESCALATED";
}
