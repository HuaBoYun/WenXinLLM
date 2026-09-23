package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 坏账核销实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_BAD_DEBT_WRITE_OFF")
@ApiModel(value = "ArBadDebtWriteOffEntity对象", description = "坏账核销表")
public class ArBadDebtWriteOffEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "核销ID")
    @TableId(value = "WRITE_OFF_ID", type = IdType.ASSIGN_UUID)
    private String writeOffId;

    @ApiModelProperty(value = "核销单号")
    @TableField("WRITE_OFF_NO")
    private String writeOffNo;

    @ApiModelProperty(value = "应收单ID")
    @TableField("RECEIVABLE_ID")
    private String receivableId;

    @ApiModelProperty(value = "客户ID")
    @TableField("CUSTOMER_ID")
    private String customerId;

    @ApiModelProperty(value = "核销金额")
    @TableField("WRITE_OFF_AMOUNT")
    private BigDecimal writeOffAmount;

    @ApiModelProperty(value = "核销原因")
    @TableField("WRITE_OFF_REASON")
    private String writeOffReason;

    @ApiModelProperty(value = "核销日期")
    @TableField("WRITE_OFF_DATE")
    private LocalDate writeOffDate;

    @ApiModelProperty(value = "核销状态(0待审核 1已审核 2已核销 3已拒绝)")
    @TableField("WRITE_OFF_STATUS")
    private Integer writeOffStatus;

    @ApiModelProperty(value = "审核时间")
    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "审核人ID")
    @TableField("AUDITOR_ID")
    private String auditorId;

    @ApiModelProperty(value = "审核意见")
    @TableField("AUDIT_COMMENTS")
    private String auditComments;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "删除标识(0否 1是)")
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    // ========== 扩展字段（不映射到数据库）==========

    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty(value = "应收单号")
    @TableField(exist = false)
    private String receivableNo;

    @ApiModelProperty(value = "核销状态名称")
    @TableField(exist = false)
    private String writeOffStatusName;
}

