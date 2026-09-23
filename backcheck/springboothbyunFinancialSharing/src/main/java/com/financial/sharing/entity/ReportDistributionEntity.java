package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报告分发实体
 */
@Data
@TableName("TBL_REPORT_DISTRIBUTION")
public class ReportDistributionEntity {

    @TableId(type = IdType.AUTO)
    @TableField("DISTRIBUTION_ID")
    private Long distributionId;

    @TableField("REPORT_ID")
    private String reportId;

    @TableField("REPORT_NAME")
    private String reportName;

    @TableField("DISTRIBUTION_TYPE")
    private String distributionType; // EMAIL, FTP, INTERNAL, SYSTEM

    @TableField("DISTRIBUTION_CONFIG")
    private String distributionConfig;

    @TableField("RECIPIENTS")
    private String recipients;

    @TableField("CC_RECIPIENTS")
    private String ccRecipients;

    @TableField("BCC_RECIPIENTS")
    private String bccRecipients;

    @TableField("SUBJECT")
    private String subject;

    @TableField("BODY_TEMPLATE")
    private String bodyTemplate;

    @TableField("ATTACHMENTS")
    private String attachments;

    @TableField("DISTRIBUTION_TIME")
    private LocalDateTime distributionTime;

    @TableField("STATUS")
    private String status; // PENDING, SENDING, SENT, FAILED

    @TableField("RETRY_COUNT")
    private Integer retryCount;

    @TableField("MAX_RETRIES")
    private Integer maxRetries;

    @TableField("NEXT_RETRY_TIME")
    private LocalDateTime nextRetryTime;

    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @TableField("SENT_TIME")
    private LocalDateTime sentTime;

    @TableField("TRACKING_ID")
    private String trackingId;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("CREATE_USER")
    private Long createUser;

    @TableField("UPDATE_USER")
    private Long updateUser;
}