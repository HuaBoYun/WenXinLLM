package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 系统集成实体类
 * 
 * @description 系统集成管理实体，支持与外部系统的集成配置和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_INTEGRATION")
public class BudgetIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 集成编码
     */
    @TableField("INTEGRATION_CODE")
    private String integrationCode;

    /**
     * 集成名称
     */
    @TableField("INTEGRATION_NAME")
    private String integrationName;

    /**
     * 集成类型：ERP-ERP系统，CRM-CRM系统，HRM-人力资源，BI-商业智能
     */
    @TableField("INTEGRATION_TYPE")
    private String integrationType;

    /**
     * 系统名称
     */
    @TableField("SYSTEM_NAME")
    private String systemName;

    /**
     * 系统版本
     */
    @TableField("SYSTEM_VERSION")
    private String systemVersion;

    /**
     * 集成方式：API-接口集成，FILE-文件集成，DATABASE-数据库集成
     */
    @TableField("INTEGRATION_METHOD")
    private String integrationMethod;

    /**
     * 连接配置（JSON格式）
     */
    @TableField("CONNECTION_CONFIG")
    private String connectionConfig;

    /**
     * 数据映射配置（JSON格式）
     */
    @TableField("DATA_MAPPING_CONFIG")
    private String dataMappingConfig;

    /**
     * 同步频率：REAL_TIME-实时，HOURLY-每小时，DAILY-每日，WEEKLY-每周
     */
    @TableField("SYNC_FREQUENCY")
    private String syncFrequency;

    /**
     * 集成状态：ACTIVE-激活，INACTIVE-停用，ERROR-错误
     */
    @TableField("INTEGRATION_STATUS")
    private String integrationStatus;

    /**
     * 最后同步时间
     */
    @TableField("LAST_SYNC_TIME")
    private LocalDateTime lastSyncTime;

    /**
     * 下次同步时间
     */
    @TableField("NEXT_SYNC_TIME")
    private LocalDateTime nextSyncTime;

    /**
     * 同步记录数
     */
    @TableField("SYNC_RECORD_COUNT")
    private Integer syncRecordCount;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // ==================== 常量定义 ====================

    /**
     * 集成类型常量
     */
    public static final String INTEGRATION_TYPE_ERP = "ERP";
    public static final String INTEGRATION_TYPE_CRM = "CRM";
    public static final String INTEGRATION_TYPE_HRM = "HRM";
    public static final String INTEGRATION_TYPE_BI = "BI";

    /**
     * 集成方式常量
     */
    public static final String INTEGRATION_METHOD_API = "API";
    public static final String INTEGRATION_METHOD_FILE = "FILE";
    public static final String INTEGRATION_METHOD_DATABASE = "DATABASE";

    /**
     * 同步频率常量
     */
    public static final String SYNC_FREQUENCY_REAL_TIME = "REAL_TIME";
    public static final String SYNC_FREQUENCY_HOURLY = "HOURLY";
    public static final String SYNC_FREQUENCY_DAILY = "DAILY";
    public static final String SYNC_FREQUENCY_WEEKLY = "WEEKLY";

    /**
     * 集成状态常量
     */
    public static final String INTEGRATION_STATUS_ACTIVE = "ACTIVE";
    public static final String INTEGRATION_STATUS_INACTIVE = "INACTIVE";
    public static final String INTEGRATION_STATUS_ERROR = "ERROR";
}
