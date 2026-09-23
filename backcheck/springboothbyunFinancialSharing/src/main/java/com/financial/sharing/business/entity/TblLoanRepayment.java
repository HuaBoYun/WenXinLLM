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
 * 借款单还款记录表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_LOAN_REPAYMENT")
@ApiModel(value = "TblLoanRepayment", description = "借款单还款记录表")
public class TblLoanRepayment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "REPAYMENT_ID")
    @ApiModelProperty(value = "还款记录ID")
    private String repaymentId;

    @TableField("LOAN_ID")
    @ApiModelProperty(value = "借款单ID")
    private String loanId;

    @TableField("REPAYMENT_TYPE")
    @ApiModelProperty(value = "还款类型(CASH-现金,TRANSFER-转账,OFFSET-抵冲报销)")
    private String repaymentType;

    @TableField("REPAYMENT_AMOUNT")
    @ApiModelProperty(value = "还款金额")
    private BigDecimal repaymentAmount;

    @TableField("REPAYMENT_DATE")
    @ApiModelProperty(value = "还款日期")
    private LocalDate repaymentDate;

    @TableField("REPAYMENT_VOUCHER_NO")
    @ApiModelProperty(value = "还款凭证号")
    private String repaymentVoucherNo;

    @TableField("REPAYMENT_METHOD")
    @ApiModelProperty(value = "还款方式")
    private String repaymentMethod;

    @TableField("REPAYMENT_ACCOUNT")
    @ApiModelProperty(value = "还款账户")
    private String repaymentAccount;

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
