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
 * 报销单主表实体类
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_REPORT")
@ApiModel(value = "TblExpenseReport", description = "报销单主表")
public class TblExpenseReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "REPORT_ID")
    @ApiModelProperty(value = "报销单ID")
    private String reportId;

    @TableField("REPORT_CODE")
    @ApiModelProperty(value = "报销单号")
    private String reportCode;

    @TableField("REPORT_TITLE")
    @ApiModelProperty(value = "报销单标题")
    private String reportTitle;

    @TableField("REPORT_TYPE")
    @ApiModelProperty(value = "报销类型(TRAVEL-差旅费,BUSINESS-业务费,ENTERTAINMENT-招待费,TRAINING-培训费,WELFARE-福利费,OTHER-其他)")
    private String reportType;

    @TableField("REPORT_STATUS")
    @ApiModelProperty(value = "报销单状态(DRAFT-草稿,SUBMITTED-已提交,APPROVING-审批中,APPROVED-已审批,REJECTED-已驳回,PAID-已支付,CANCELLED-已取消)")
    private String reportStatus;

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

    @TableField("PROJECT_ID")
    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @TableField("PROJECT_NAME")
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @TableField("TOTAL_AMOUNT")
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;

    @TableField("PAYMENT_AMOUNT")
    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentAmount;

    @TableField("REPORT_DATE")
    @ApiModelProperty(value = "报销日期")
    private LocalDate reportDate;

    @TableField("REPORT_REASON")
    @ApiModelProperty(value = "报销事由")
    private String reportReason;

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

    @TableField("PAYMENT_STATUS")
    @ApiModelProperty(value = "支付状态(UNPAID-未支付,PARTIAL-部分支付,PAID-已支付)")
    private String paymentStatus;

    @TableField("PAYMENT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
    private LocalDateTime paymentTime;

    @TableField("PAYMENT_VOUCHER_NO")
    @ApiModelProperty(value = "支付凭证号")
    private String paymentVoucherNo;

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