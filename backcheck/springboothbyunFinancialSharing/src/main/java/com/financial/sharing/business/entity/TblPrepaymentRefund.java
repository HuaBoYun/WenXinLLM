package com.financial.sharing.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预付款退款记录表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PREPAYMENT_REFUND")
@ApiModel(value = "TblPrepaymentRefund", description = "预付款退款记录表")
public class TblPrepaymentRefund implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "REFUND_ID")
    @ApiModelProperty(value = "退款记录ID")
    private String refundId;

    @TableField("PREPAYMENT_ID")
    @ApiModelProperty(value = "预付款单ID")
    private String prepaymentId;

    @TableField("REFUND_AMOUNT")
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @TableField("REFUND_DATE")
    @ApiModelProperty(value = "退款日期")
    private LocalDate refundDate;

    @TableField("REFUND_REASON")
    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    @TableField("REFUND_VOUCHER_NO")
    @ApiModelProperty(value = "退款凭证号")
    private String refundVoucherNo;

    @TableField("REFUND_ACCOUNT")
    @ApiModelProperty(value = "退款账户")
    private String refundAccount;

    @TableField("BANK_NAME")
    @ApiModelProperty(value = "开户银行")
    private String bankName;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;
}
