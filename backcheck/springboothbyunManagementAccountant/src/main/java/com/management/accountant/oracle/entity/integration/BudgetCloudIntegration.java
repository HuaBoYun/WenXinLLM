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
 * 预算云平台集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_CLOUD_INTEGRATION")
public class BudgetCloudIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 云平台集成ID (主键)
     */
    @TableId(value = "CLOUD_ID", type = IdType.ASSIGN_UUID)
    private String cloudId;

    /**
     * 云平台编码
     */
    @TableField("CLOUD_CODE")
    private String cloudCode;

    /**
     * 云平台名称
     */
    @TableField("CLOUD_NAME")
    private String cloudName;

    /**
     * 云平台类型 (ALIYUN/TENCENT/HUAWEI/AWS/AZURE)
     */
    @TableField("CLOUD_TYPE")
    private String cloudType;

    /**
     * 服务类型 (OSS/RDS/MQ/FUNCTION/API_GATEWAY)
     */
    @TableField("SERVICE_TYPE")
    private String serviceType;

    /**
     * 区域
     */
    @TableField("REGION")
    private String region;

    /**
     * 访问密钥ID
     */
    @TableField("ACCESS_KEY_ID")
    private String accessKeyId;

    /**
     * 访问密钥Secret(加密)
     */
    @TableField("ACCESS_KEY_SECRET")
    private String accessKeySecret;

    /**
     * 端点地址
     */
    @TableField("ENDPOINT")
    private String endpoint;

    /**
     * 存储桶名称(OSS)
     */
    @TableField("BUCKET_NAME")
    private String bucketName;

    /**
     * 数据库实例ID(RDS)
     */
    @TableField("DB_INSTANCE_ID")
    private String dbInstanceId;

    /**
     * 队列名称(MQ)
     */
    @TableField("QUEUE_NAME")
    private String queueName;

    /**
     * 函数名称(Function)
     */
    @TableField("FUNCTION_NAME")
    private String functionName;

    /**
     * API网关ID
     */
    @TableField("API_GATEWAY_ID")
    private String apiGatewayId;

    /**
     * 连接配置 (JSON格式)
     */
    @TableField("CONNECTION_CONFIG")
    private String connectionConfig;

    /**
     * 数据同步方式 (UPLOAD/DOWNLOAD/BIDIRECTIONAL)
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
     * 是否启用加密
     */
    @TableField("ENABLE_ENCRYPTION")
    private Boolean enableEncryption;

    /**
     * 加密算法
     */
    @TableField("ENCRYPTION_ALGORITHM")
    private String encryptionAlgorithm;

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
     * 最后同步记录数
     */
    @TableField("LAST_SYNC_RECORDS")
    private Integer lastSyncRecords;

    /**
     * 总数据传输量
     */
    @TableField("TOTAL_DATA_TRANSFER")
    private Long totalDataTransfer;

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
     * 数据传输量(MB)
     */
    @TableField("DATA_TRANSFER_MB")
    private Long dataTransferMb;

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

