package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算集成监控实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_INTEGRATION_MONITOR")
public class BudgetIntegrationMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 监控记录ID (主键)
     */
    @TableId(value = "MONITOR_ID", type = IdType.ASSIGN_UUID)
    private String monitorId;

    /**
     * ID (兼容字段)
     */
    @TableField("ID")
    private String id;

    /**
     * 执行耗时 (毫秒)
     */
    @TableField("EXECUTION_DURATION")
    private Integer executionDuration;

    /**
     * 记录数量
     */
    @TableField("RECORD_COUNT")
    private Integer recordCount;

    /**
     * 集成类型 (API/ERP/DATABASE/FILE/WEBSERVICE/MQ/STREAM/BI/CLOUD)
     */
    @TableField("INTEGRATION_TYPE")
    private String integrationType;

    /**
     * 集成ID
     */
    @TableField("INTEGRATION_ID")
    private String integrationId;

    /**
     * 集成名称
     */
    @TableField("INTEGRATION_NAME")
    private String integrationName;

    /**
     * 执行类型 (SYNC/ASYNC/SCHEDULED/MANUAL)
     */
    @TableField("EXECUTION_TYPE")
    private String executionType;

    /**
     * 执行开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 执行结束时间
     */
    @TableField("END_TIME")
    private Date endTime;

    /**
     * 执行耗时(毫秒)
     */
    @TableField("DURATION_MS")
    private Long durationMs;

    /**
     * 执行状态 (SUCCESS/FAILURE/PARTIAL/TIMEOUT)
     */
    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    /**
     * 处理记录总数
     */
    @TableField("TOTAL_RECORDS")
    private Integer totalRecords;

    /**
     * 成功记录数
     */
    @TableField("SUCCESS_RECORDS")
    private Integer successRecords;

    /**
     * 失败记录数
     */
    @TableField("FAILURE_RECORDS")
    private Integer failureRecords;

    /**
     * 跳过记录数
     */
    @TableField("SKIPPED_RECORDS")
    private Integer skippedRecords;

    /**
     * 数据传输量(KB)
     */
    @TableField("DATA_SIZE_KB")
    private Long dataSizeKb;

    /**
     * 请求参数 (JSON格式)
     */
    @TableField("REQUEST_PARAMS")
    private String requestParams;

    /**
     * 响应数据摘要
     */
    @TableField("RESPONSE_SUMMARY")
    private String responseSummary;

    /**
     * 错误代码
     */
    @TableField("ERROR_CODE")
    private String errorCode;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 错误堆栈
     */
    @TableField("ERROR_STACK")
    private String errorStack;

    /**
     * 重试次数
     */
    @TableField("RETRY_COUNT")
    private Integer retryCount;

    /**
     * CPU使用率(%)
     */
    @TableField("CPU_USAGE")
    private Double cpuUsage;

    /**
     * 内存使用量(MB)
     */
    @TableField("MEMORY_USAGE_MB")
    private Long memoryUsageMb;

    /**
     * 网络流量(KB)
     */
    @TableField("NETWORK_TRAFFIC_KB")
    private Long networkTrafficKb;

    /**
     * 响应时间(毫秒)
     */
    @TableField("RESPONSE_TIME_MS")
    private Integer responseTimeMs;

    /**
     * 吞吐量(记录/秒)
     */
    @TableField("THROUGHPUT")
    private Double throughput;

    /**
     * 告警级别 (INFO/WARNING/ERROR/CRITICAL)
     */
    @TableField("ALERT_LEVEL")
    private String alertLevel;

    /**
     * 是否已告警
     */
    @TableField("IS_ALERTED")
    private Boolean isAlerted;

    /**
     * 告警时间
     */
    @TableField("ALERT_TIME")
    private Date alertTime;

    /**
     * 执行人
     */
    @TableField("EXECUTED_BY")
    private String executedBy;

    /**
     * 执行IP
     */
    @TableField("EXECUTED_IP")
    private String executedIp;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;
}

