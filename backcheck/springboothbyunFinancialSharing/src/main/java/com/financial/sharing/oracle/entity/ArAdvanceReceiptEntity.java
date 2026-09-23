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
 * 预收款实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_ADVANCE_RECEIPT")
@ApiModel(value = "ArAdvanceReceiptEntity对象", description = "预收款表")
public class ArAdvanceReceiptEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预收款ID")
    @TableId(value = "ADVANCE_ID", type = IdType.ASSIGN_UUID)
    private String advanceId;

    @ApiModelProperty(value = "预收款单号")
    @TableField("ADVANCE_NO")
    private String advanceNo;

    @ApiModelProperty(value = "客户ID")
    @TableField("CUSTOMER_ID")
    private String customerId;

    @ApiModelProperty(value = "预收金额")
    @TableField("ADVANCE_AMOUNT")
    private BigDecimal advanceAmount;

    @ApiModelProperty(value = "已冲销金额")
    @TableField("OFFSET_AMOUNT")
    private BigDecimal offsetAmount;

    @ApiModelProperty(value = "剩余金额")
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    @ApiModelProperty(value = "预收状态(0未冲销 1部分冲销 2全部冲销)")
    @TableField("ADVANCE_STATUS")
    private Integer advanceStatus;

    @ApiModelProperty(value = "预收日期")
    @TableField("ADVANCE_DATE")
    private LocalDate advanceDate;

    @ApiModelProperty(value = "收款方式(1现金 2银行转账 3支票 4承兑汇票)")
    @TableField("PAYMENT_METHOD")
    private Integer paymentMethod;

    @ApiModelProperty(value = "开户银行")
    @TableField("BANK_NAME")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    @TableField("BANK_ACCOUNT")
    private String bankAccount;

    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;

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

    @ApiModelProperty(value = "预收状态名称")
    @TableField(exist = false)
    private String advanceStatusName;

    @ApiModelProperty(value = "收款方式名称")
    @TableField(exist = false)
    private String paymentMethodName;

    @ApiModelProperty(value = "创建人姓名")
    @TableField(exist = false)
    private String creatorName;
}

