package com.financial.sharing.entity;

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
 * 项目结算记录表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROJECT_SETTLEMENT")
@ApiModel(value = "TblProjectSettlement", description = "项目结算记录表")
public class TblProjectSettlement implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "SETTLEMENT_ID")
    @ApiModelProperty(value = "结算ID")
    private String settlementId;

    @TableField("PROJECT_ID")
    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @TableField("SETTLEMENT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结算日期")
    private LocalDate settlementDate;

    @TableField("SETTLEMENT_AMOUNT")
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;

    @TableField("SETTLEMENT_STATUS")
    @ApiModelProperty(value = "结算状态")
    private String settlementStatus;

    @TableField("SETTLEMENT_DESCRIPTION")
    @ApiModelProperty(value = "结算说明")
    private String settlementDescription;

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

