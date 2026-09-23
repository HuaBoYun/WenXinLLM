package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 付款计划实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PAYMENT_PLAN")
public class TblPaymentPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 计划ID */
    @TableId(value = "PLAN_ID", type = IdType.ASSIGN_UUID)
    private String planId;

    /** 计划编号 */
    @TableField("PLAN_NO")
    private String planNo;

    /** 计划名称 */
    @TableField("PLAN_NAME")
    private String planName;

    /** 计划日期 */
    @TableField("PLAN_DATE")
    private LocalDate planDate;

    /** 计划总金额 */
    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    /** 已执行金额 */
    @TableField("EXECUTED_AMOUNT")
    private BigDecimal executedAmount;

    /** 状态(pending/executing/executed/cancelled) */
    @TableField("STATUS")
    private String status;

    /** 备注 */
    @TableField("REMARKS")
    private String remarks;

    /** 创建时间 */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 创建人 */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /** 更新人 */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 租户ID */
    @TableField("TENANT_ID")
    private String tenantId;

    /** 逻辑删除标记 */
    @TableLogic
    @TableField("IS_DELETED")
    private Integer isDeleted;
}

