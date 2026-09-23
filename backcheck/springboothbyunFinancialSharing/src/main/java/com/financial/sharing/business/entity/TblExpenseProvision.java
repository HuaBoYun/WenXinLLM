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
 * 费用预提主表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_PROVISION")
@ApiModel(value = "TblExpenseProvision", description = "费用预提主表")
public class TblExpenseProvision implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PROVISION_ID")
    @ApiModelProperty(value = "预提ID")
    private String provisionId;

    @TableField("PROVISION_CODE")
    @ApiModelProperty(value = "预提单号")
    private String provisionCode;

    @TableField("PROVISION_TITLE")
    @ApiModelProperty(value = "预提标题")
    private String provisionTitle;

    @TableField("PROVISION_TYPE")
    @ApiModelProperty(value = "预提类型(SALARY-工资预提,BONUS-奖金预提,RENT-租金预提,INTEREST-利息预提,OTHER-其他)")
    private String provisionType;

    @TableField("PROVISION_STATUS")
    @ApiModelProperty(value = "预提状态(DRAFT-草稿,SUBMITTED-已提交,APPROVING-审批中,APPROVED-已审批,REJECTED-已驳回,REVERSED-已冲销,CANCELLED-已取消)")
    private String provisionStatus;

    @TableField("APPLICANT_ID")
    @ApiModelProperty(value = "申请人ID")
    private String applicantId;

    @TableField("APPLICANT_NAME")
    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @TableField("APPLICANT_DEPT_ID")
    @ApiModelProperty(value = "申请人部门ID")
    private String applicantDeptId;

    @TableField("APPLICANT_DEPT_NAME")
    @ApiModelProperty(value = "申请人部门名称")
    private String applicantDeptName;

    @TableField("PROVISION_AMOUNT")
    @ApiModelProperty(value = "预提金额")
    private BigDecimal provisionAmount;

    @TableField("PROVISION_DATE")
    @ApiModelProperty(value = "预提日期")
    private LocalDate provisionDate;

    @TableField("PROVISION_REASON")
    @ApiModelProperty(value = "预提事由")
    private String provisionReason;

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

    @TableField("REVERSE_STATUS")
    @ApiModelProperty(value = "冲销状态(UNREVERSED-未冲销,REVERSED-已冲销)")
    private String reverseStatus;

    @TableField("REVERSE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "冲销时间")
    private LocalDateTime reverseTime;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
