package com.financial.sharing.mysql.entity;

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
 * 收款单实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_RECEIPT")
@ApiModel(value = "ArReceiptEntity对象", description = "收款单表")
public class ArReceiptEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收款单ID")
    @TableId(value = "RECEIPT_ID", type = IdType.ASSIGN_UUID)
    private String receiptId;

    @ApiModelProperty(value = "收款单号")
    @TableField("RECEIPT_NO")
    private String receiptNo;

    @ApiModelProperty(value = "客户ID")
    @TableField("CUSTOMER_ID")
    private String customerId;

    @ApiModelProperty(value = "收款金额")
    @TableField("RECEIPT_AMOUNT")
    private BigDecimal receiptAmount;

    @ApiModelProperty(value = "已核销金额")
    @TableField("WRITE_OFF_AMOUNT")
    private BigDecimal writeOffAmount;

    @ApiModelProperty(value = "剩余金额")
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    @ApiModelProperty(value = "收款状态(0待收款 1已收款 2已核销 3已取消)")
    @TableField("RECEIPT_STATUS")
    private Integer receiptStatus;

    @ApiModelProperty(value = "收款日期")
    @TableField("RECEIPT_DATE")
    private LocalDate receiptDate;

    @ApiModelProperty(value = "收款方式(1现金 2银行转账 3支票 4承兑汇票)")
    @TableField("PAYMENT_METHOD")
    private Integer paymentMethod;

    @ApiModelProperty(value = "开户银行")
    @TableField("BANK_NAME")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    @TableField("BANK_ACCOUNT")
    private String bankAccount;

    @ApiModelProperty(value = "确认收款金额")
    @TableField("CONFIRM_AMOUNT")
    private BigDecimal confirmAmount;

    @ApiModelProperty(value = "确认收款日期")
    @TableField("CONFIRM_DATE")
    private LocalDate confirmDate;

    @ApiModelProperty(value = "确认人")
    @TableField("CONFIRM_BY")
    private String confirmBy;

    @ApiModelProperty(value = "取消原因")
    @TableField("CANCEL_REASON")
    private String cancelReason;

    @ApiModelProperty(value = "取消时间")
    @TableField("CANCEL_TIME")
    private LocalDateTime cancelTime;

    @ApiModelProperty(value = "取消人")
    @TableField("CANCEL_BY")
    private String cancelBy;

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

    @ApiModelProperty(value = "收款状态名称")
    @TableField(exist = false)
    private String receiptStatusName;

    @ApiModelProperty(value = "收款方式名称")
    @TableField(exist = false)
    private String paymentMethodName;

    @ApiModelProperty(value = "创建人姓名")
    @TableField(exist = false)
    private String creatorName;

    @ApiModelProperty(value = "确认人姓名")
    @TableField(exist = false)
    private String confirmerName;
}

