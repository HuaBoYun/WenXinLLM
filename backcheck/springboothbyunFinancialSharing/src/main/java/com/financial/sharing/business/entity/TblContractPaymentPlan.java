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
 * 合同收付款计划表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_CONTRACT_PAYMENT_PLAN")
@ApiModel(value = "TblContractPaymentPlan", description = "合同收付款计划表")
public class TblContractPaymentPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PLAN_ID")
    @ApiModelProperty(value = "计划ID")
    private String planId;

    @TableField("CONTRACT_ID")
    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @TableField("PLAN_TYPE")
    @ApiModelProperty(value = "计划类型(RECEIPT-收款,PAYMENT-付款)")
    private String planType;

    @TableField("PLAN_NAME")
    @ApiModelProperty(value = "计划名称")
    private String planName;

    @TableField("PLAN_AMOUNT")
    @ApiModelProperty(value = "计划金额")
    private BigDecimal planAmount;

    @TableField("PLAN_DATE")
    @ApiModelProperty(value = "计划日期")
    private LocalDate planDate;

    @TableField("PLAN_STATUS")
    @ApiModelProperty(value = "计划状态(PENDING-待处理,COMPLETED-已完成,CANCELLED-已取消)")
    private String planStatus;

    @TableField("ACTUAL_AMOUNT")
    @ApiModelProperty(value = "实际金额")
    private BigDecimal actualAmount;

    @TableField("ACTUAL_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际日期")
    private LocalDateTime actualDate;

    @TableField("PAYMENT_METHOD")
    @ApiModelProperty(value = "收付款方式")
    private String paymentMethod;

    @TableField("VOUCHER_NO")
    @ApiModelProperty(value = "凭证号")
    private String voucherNo;

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
