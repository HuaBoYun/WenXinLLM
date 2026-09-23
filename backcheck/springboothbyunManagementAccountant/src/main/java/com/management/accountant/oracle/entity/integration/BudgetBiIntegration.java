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
 * 预算BI系统集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_BI_INTEGRATION")
public class BudgetBiIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * BI集成ID (主键)
     */
    @TableId(value = "BI_ID", type = IdType.ASSIGN_UUID)
    private String biId;

    /**
     * BI集成编码
     */
    @TableField("BI_CODE")
    private String biCode;

    /**
     * BI集成名称
     */
    @TableField("BI_NAME")
    private String biName;

    /**
     * BI系统类型 (POWERBI/TABLEAU/QLIK/FINEBI/SUPERSET)
     */
    @TableField("BI_TYPE")
    private String biType;

    /**
     * BI系统版本
     */
    @TableField("BI_VERSION")
    private String biVersion;

    /**
     * 连接方式 (API/ODBC/JDBC/DIRECT)
     */
    @TableField("CONNECTION_TYPE")
    private String connectionType;

    /**
     * 服务器地址
     */
    @TableField("SERVER_URL")
    private String serverUrl;

    /**
     * 认证方式 (BASIC/OAUTH2/API_KEY)
     */
    @TableField("AUTH_TYPE")
    private String authType;

    /**
     * 认证配置 (JSON格式)
     */
    @TableField("AUTH_CONFIG")
    private String authConfig;

    /**
     * 工作区ID
     */
    @TableField("WORKSPACE_ID")
    private String workspaceId;

    /**
     * 数据集ID
     */
    @TableField("DATASET_ID")
    private String datasetId;

    /**
     * 报表ID列表 (JSON格式)
     */
    @TableField("REPORT_IDS")
    private String reportIds;

    /**
     * 数据同步方式 (PUSH/PULL/REALTIME)
     */
    @TableField("SYNC_MODE")
    private String syncMode;

    /**
     * 同步频率 (REALTIME/HOURLY/DAILY/WEEKLY/MONTHLY/MANUAL)
     */
    @TableField("SYNC_FREQUENCY")
    private String syncFrequency;

    /**
     * 同步时间配置 (CRON表达式)
     */
    @TableField("SYNC_SCHEDULE")
    private String syncSchedule;

    /**
     * 数据映射配置 (JSON格式)
     */
    @TableField("DATA_MAPPING")
    private String dataMapping;

    /**
     * 维度配置 (JSON格式)
     */
    @TableField("DIMENSION_CONFIG")
    private String dimensionConfig;

    /**
     * 度量配置 (JSON格式)
     */
    @TableField("MEASURE_CONFIG")
    private String measureConfig;

    /**
     * 是否启用缓存
     */
    @TableField("ENABLE_CACHE")
    private Boolean enableCache;

    /**
     * 缓存过期时间(分钟)
     */
    @TableField("CACHE_EXPIRE_MINUTES")
    private Integer cacheExpireMinutes;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 最后同步时间
     */
    @TableField("LAST_SYNC_TIME")
    private Date lastSyncTime;

    /**
     * 下次同步时间
     */
    @TableField("NEXT_SYNC_TIME")
    private Date nextSyncTime;

    /**
     * 同步成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 同步失败次数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 最后同步记录数
     */
    @TableField("LAST_SYNC_RECORDS")
    private Integer lastSyncRecords;

    /**
     * 集成状态 (ACTIVE/INACTIVE/ERROR/TESTING)
     */
    @TableField("INTEGRATION_STATUS")
    private String integrationStatus;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;
}

