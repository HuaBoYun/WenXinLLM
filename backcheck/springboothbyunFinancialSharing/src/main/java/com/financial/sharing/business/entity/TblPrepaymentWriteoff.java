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
 * 预付款核销记录表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PREPAYMENT_WRITEOFF")
@ApiModel(value = "TblPrepaymentWriteoff", description = "预付款核销记录表")
public class TblPrepaymentWriteoff implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "WRITEOFF_ID")
    @ApiModelProperty(value = "核销记录ID")
    private String writeoffId;

    @TableField("PREPAYMENT_ID")
    @ApiModelProperty(value = "预付款单ID")
    private String prepaymentId;

    @TableField("WRITEOFF_TYPE")
    @ApiModelProperty(value = "核销类型(INVOICE-发票核销,RETURN-退款,OTHER-其他)")
    private String writeoffType;

    @TableField("WRITEOFF_AMOUNT")
    @ApiModelProperty(value = "核销金额")
    private BigDecimal writeoffAmount;

    @TableField("WRITEOFF_DATE")
    @ApiModelProperty(value = "核销日期")
    private LocalDate writeoffDate;

    @TableField("BUSINESS_TYPE")
    @ApiModelProperty(value = "关联业务类型")
    private String businessType;

    @TableField("BUSINESS_ID")
    @ApiModelProperty(value = "关联业务ID")
    private String businessId;

    @TableField("WRITEOFF_VOUCHER_NO")
    @ApiModelProperty(value = "核销凭证号")
    private String writeoffVoucherNo;

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
