package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_SYSTEM_MONITOR")
public class BudgetSystemMonitorEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "MONITOR_ID", type = IdType.ASSIGN_UUID)
    private String monitorId;
    @TableField("MONITOR_NAME")
    private String monitorName;
    @TableField("MONITOR_TYPE")
    private String monitorType;
    @TableField("SERVICE_NAME")
    private String serviceName;
    @TableField("SERVICE_STATUS")
    private String serviceStatus;
    @TableField("CPU_USAGE")
    private BigDecimal cpuUsage;
    @TableField("MEMORY_USAGE")
    private BigDecimal memoryUsage;
    @TableField("DISK_USAGE")
    private BigDecimal diskUsage;
    @TableField("RESPONSE_TIME")
    private Integer responseTime;
    @TableField("REQUEST_COUNT")
    private Integer requestCount;
    @TableField("ERROR_COUNT")
    private Integer errorCount;
    @TableField("ACTIVE_CONNECTIONS")
    private Integer activeConnections;
    @TableField("ALERT_LEVEL")
    private String alertLevel;
    @TableField("MONITOR_TIME")
    private Date monitorTime;
    @TableField("REMARK")
    private String remark;
    @TableField("CREATOR_ID")
    private String creatorId;
    @TableField("CREATE_TIME")
    private Date createTime;
    @TableField("IS_DELETED")
    private Integer isDeleted;
    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("ALERT_MESSAGE")
    private String alertMessage;
    @TableField("ALERT_TYPE")
    private String alertType;
    @TableField("STATUS")
    private String status;
    @TableField("DESCRIPTION")
    private String description;
    @TableField("ICON")
    private String icon;
    @TableField("SERVICE_UPTIME")
    private String serviceUptime;
    @TableField("INBOUND_TRAFFIC")
    private BigDecimal inboundTraffic;
    @TableField("OUTBOUND_TRAFFIC")
    private BigDecimal outboundTraffic;
    @TableField("HEALTH_SCORE")
    private BigDecimal healthScore;
    @TableField("CONNECTIONS")
    private Integer connections;
    @TableField("QUERIES_PER_SECOND")
    private BigDecimal queriesPerSecond;
    @TableField("SLOW_QUERIES")
    private Integer slowQueries;
    @TableField("CACHE_HIT_RATE")
    private BigDecimal cacheHitRate;
    @TableField("LOCK_WAITS")
    private Integer lockWaits;
    @TableField("DATABASE_SIZE")
    private String databaseSize;
    @TableField("TABLESPACE_USAGE")
    private BigDecimal tablespaceUsage;
}

