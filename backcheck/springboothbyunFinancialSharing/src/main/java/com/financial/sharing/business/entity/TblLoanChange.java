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
import java.time.LocalDateTime;

/**
 * 借款单变更记录表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_LOAN_CHANGE")
@ApiModel(value = "TblLoanChange", description = "借款单变更记录表")
public class TblLoanChange implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CHANGE_ID")
    @ApiModelProperty(value = "变更ID")
    private String changeId;

    @TableField("LOAN_ID")
    @ApiModelProperty(value = "借款单ID")
    private String loanId;

    @TableField("CHANGE_TYPE")
    @ApiModelProperty(value = "变更类型(INCREASE-增量,DECREASE-减量,EXTEND-延期,CANCEL-取消)")
    private String changeType;

    @TableField("BEFORE_AMOUNT")
    @ApiModelProperty(value = "变更前金额")
    private BigDecimal beforeAmount;

    @TableField("AFTER_AMOUNT")
    @ApiModelProperty(value = "变更后金额")
    private BigDecimal afterAmount;

    @TableField("CHANGE_REASON")
    @ApiModelProperty(value = "变更原因")
    private String changeReason;

    @TableField("APPROVER_ID")
    @ApiModelProperty(value = "审批人ID")
    private String approverId;

    @TableField("APPROVER_NAME")
    @ApiModelProperty(value = "审批人姓名")
    private String approverName;

    @TableField("APPROVE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审批时间")
    private LocalDateTime approveTime;

    @TableField("APPROVE_OPINION")
    @ApiModelProperty(value = "审批意见")
    private String approveOpinion;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
