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
 * 预算数据库集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_DATABASE_INTEGRATION")
public class BudgetDatabaseIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库集成ID (主键)
     */
    @TableId(value = "DB_ID", type = IdType.ASSIGN_UUID)
    private String dbId;

    /**
     * 数据库集成编码
     */
    @TableField("DB_CODE")
    private String dbCode;

    /**
     * 数据库集成名称
     */
    @TableField("DB_NAME")
    private String dbName;

    /**
     * 数据库类型 (ORACLE/MYSQL/SQLSERVER/POSTGRESQL/DAMENG/DB2)
     */
    @TableField("DB_TYPE")
    private String dbType;

    /**
     * 数据库主机地址
     */
    @TableField("DB_HOST")
    private String dbHost;

    /**
     * 数据库端口
     */
    @TableField("DB_PORT")
    private Integer dbPort;

    /**
     * 数据库名称/实例名
     */
    @TableField("DB_INSTANCE")
    private String dbInstance;

    /**
     * 数据库用户名
     */
    @TableField("DB_USERNAME")
    private String dbUsername;

    /**
     * 数据库密码(加密)
     */
    @TableField("DB_PASSWORD")
    private String dbPassword;

    /**
     * 连接字符串
     */
    @TableField("CONNECTION_STRING")
    private String connectionString;

    /**
     * 连接池配置 (JSON格式)
     */
    @TableField("POOL_CONFIG")
    private String poolConfig;

    /**
     * 数据同步方式 (FULL/INCREMENTAL)
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
     * 源表配置 (JSON格式)
     */
    @TableField("SOURCE_TABLES")
    private String sourceTables;

    /**
     * 目标表配置 (JSON格式)
     */
    @TableField("TARGET_TABLES")
    private String targetTables;

    /**
     * 字段映射配置 (JSON格式)
     */
    @TableField("FIELD_MAPPING")
    private String fieldMapping;

    /**
     * 过滤条件 (SQL WHERE子句)
     */
    @TableField("FILTER_CONDITION")
    private String filterCondition;

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

