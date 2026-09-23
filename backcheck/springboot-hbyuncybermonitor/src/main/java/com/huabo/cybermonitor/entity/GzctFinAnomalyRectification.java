package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 财务异常派单整改记录实体
 * 对应表: GZCT_FIN_ANOMALY_RECTIFICATION
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_ANOMALY_RECTIFICATION")
public class GzctFinAnomalyRectification {

    @TableId(value = "RECTIFICATION_ID", type = IdType.ASSIGN_UUID)
    private String rectificationId;

    /** 关联异常ID */
    @TableField("ANOMALY_ID")
    private String anomalyId;

    /** 企业ID */
    @TableField("COMPANY_ID")
    private String companyId;

    /** 企业名称 */
    @TableField("COMPANY_NAME")
    private String companyName;

    /** 异常类型 */
    @TableField("ANOMALY_TYPE")
    private String anomalyType;

    /** 异常指标 */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /** 整改责任人 */
    @TableField("ASSIGNEE")
    private String assignee;

    /** 整改期限 */
    @TableField("DEADLINE")
    private LocalDate deadline;

    /** 整改要求 */
    @TableField("REQUIREMENT")
    private String requirement;

    /** 整改状态: PENDING-待整改, PROCESSING-整改中, COMPLETED-已完成, OVERDUE-已逾期 */
    @TableField("STATUS")
    private String status;

    /** 下发人 */
    @TableField("ISSUED_BY")
    private String issuedBy;

    /** 下发时间 */
    @TableField("ISSUED_TIME")
    private LocalDateTime issuedTime;

    /** 完成时间 */
    @TableField("COMPLETE_TIME")
    private LocalDateTime completeTime;

    /** 整改结果说明 */
    @TableField("RESULT_DESC")
    private String resultDesc;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
