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
import java.util.List;

/**
 * 报销单表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_REPORT")
@ApiModel(value = "TblExpenseReport", description = "报销单表")
public class TblExpenseReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "REPORT_ID")
    @ApiModelProperty(value = "报销单 ID")
    private String reportId;

    @TableField("REPORT_CODE")
    @ApiModelProperty(value = "报销单号")
    private String reportCode;

    @TableField("REPORT_TITLE")
    @ApiModelProperty(value = "报销单标题")
    private String reportTitle;

    @TableField("REPORT_TYPE")
    @ApiModelProperty(value = "报销单类型")
    private String reportType;

    @TableField("REPORT_STATUS")
    @ApiModelProperty(value = "报销单状态 (DRAFT-草稿，PENDING-待审批，APPROVED-已审批，REJECTED-已拒绝，PAID-已付款)")
    private String reportStatus;

    @TableField("APPLICANT_ID")
    @ApiModelProperty(value = "申请人 ID")
    private String applicantId;

    @TableField("APPLICANT_NAME")
    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @TableField("APPLICANT_DEPT_ID")
    @ApiModelProperty(value = "申请人部门 ID")
    private String applicantDeptId;

    @TableField("APPLICANT_DEPT_NAME")
    @ApiModelProperty(value = "申请人部门名称")
    private String applicantDeptName;

    @TableField("PROJECT_ID")
    @ApiModelProperty(value = "项目 ID")
    private String projectId;

    @TableField("PROJECT_NAME")
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @TableField("TOTAL_AMOUNT")
    @ApiModelProperty(value = "报销总金额")
    private BigDecimal totalAmount;

    @TableField("PAYMENT_AMOUNT")
    @ApiModelProperty(value = "已支付金额")
    private BigDecimal paymentAmount;

    @TableField("REPORT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "报销日期")
    private LocalDateTime reportDate;

    @TableField("REPORT_REASON")
    @ApiModelProperty(value = "报销事由")
    private String reportReason;

    @TableField("APPROVER_ID")
    @ApiModelProperty(value = "审批人 ID")
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
    @ApiModelProperty(value = "付款状态 (UNPAID-未付款，PAID-已付款)")
    private String paymentStatus;

    @TableField("PAYMENT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "付款时间")
    private LocalDateTime paymentTime;

    @TableField("PAYMENT_VOUCHER_NO")
    @ApiModelProperty(value = "付款凭证号")
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

    @TableField(exist = false)
    @ApiModelProperty(value = "费用明细列表")
    private List<TblExpenseReportDetail> expenseItems;
}
