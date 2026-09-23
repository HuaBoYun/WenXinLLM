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
 * 借款单主表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_LOAN_APPLICATION")
@ApiModel(value = "TblLoanApplication", description = "借款单主表")
public class TblLoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LOAN_ID")
    @ApiModelProperty(value = "借款单ID")
    private String loanId;

    @TableField("LOAN_CODE")
    @ApiModelProperty(value = "借款单号")
    private String loanCode;

    @TableField("LOAN_TITLE")
    @ApiModelProperty(value = "借款单标题")
    private String loanTitle;

    @TableField("LOAN_TYPE")
    @ApiModelProperty(value = "借款类型(TRAVEL-差旅借款,BUSINESS-业务借款,EMERGENCY-紧急借款,OTHER-其他)")
    private String loanType;

    @TableField("LOAN_STATUS")
    @ApiModelProperty(value = "借款单状态(DRAFT-草稿,SUBMITTED-已提交,APPROVING-审批中,APPROVED-已审批,REJECTED-已驳回,DISBURSED-已放款,RETURNED-已归还,CANCELLED-已取消)")
    private String loanStatus;

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

    @TableField("LOAN_AMOUNT")
    @ApiModelProperty(value = "借款金额")
    private BigDecimal loanAmount;

    @TableField("LOAN_DATE")
    @ApiModelProperty(value = "借款日期")
    private LocalDate loanDate;

    @TableField("LOAN_REASON")
    @ApiModelProperty(value = "借款事由")
    private String loanReason;

    @TableField("EXPECT_RETURN_DATE")
    @ApiModelProperty(value = "预计归还日期")
    private LocalDate expectReturnDate;

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

    @TableField("DISBURSE_STATUS")
    @ApiModelProperty(value = "放款状态(UNDISBURSED-未放款,DISBURSED-已放款)")
    private String disburseStatus;

    @TableField("DISBURSE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "放款时间")
    private LocalDateTime disburseTime;

    @TableField("DISBURSE_VOUCHER_NO")
    @ApiModelProperty(value = "放款凭证号")
    private String disburseVoucherNo;

    @TableField("RETURN_STATUS")
    @ApiModelProperty(value = "归还状态(UNRETURNED-未归还,PARTIAL-部分归还,RETURNED-已归还)")
    private String returnStatus;

    @TableField("RETURNED_AMOUNT")
    @ApiModelProperty(value = "已归还金额")
    private BigDecimal returnedAmount;

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
