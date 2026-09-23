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
 * 费用预提冲销记录表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_PROVISION_REVERSE")
@ApiModel(value = "TblExpenseProvisionReverse", description = "费用预提冲销记录表")
public class TblExpenseProvisionReverse implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "REVERSE_ID")
    @ApiModelProperty(value = "冲销记录ID")
    private String reverseId;

    @TableField("PROVISION_ID")
    @ApiModelProperty(value = "预提ID")
    private String provisionId;

    @TableField("REVERSE_AMOUNT")
    @ApiModelProperty(value = "冲销金额")
    private BigDecimal reverseAmount;

    @TableField("REVERSE_DATE")
    @ApiModelProperty(value = "冲销日期")
    private LocalDate reverseDate;

    @TableField("REVERSE_REASON")
    @ApiModelProperty(value = "冲销原因")
    private String reverseReason;

    @TableField("REVERSE_VOUCHER_NO")
    @ApiModelProperty(value = "冲销凭证号")
    private String reverseVoucherNo;

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
