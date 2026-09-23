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
 * 预付款单主表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PREPAYMENT")
@ApiModel(value = "TblPrepayment", description = "预付款单主表")
public class TblPrepayment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PREPAYMENT_ID")
    @ApiModelProperty(value = "预付款单ID")
    private String prepaymentId;

    @TableField("PREPAYMENT_CODE")
    @ApiModelProperty(value = "预付款单号")
    private String prepaymentCode;

    @TableField("PREPAYMENT_TITLE")
    @ApiModelProperty(value = "预付款单标题")
    private String prepaymentTitle;

    @TableField("PREPAYMENT_TYPE")
    @ApiModelProperty(value = "预付款类型(PURCHASE-采购预付款,SERVICE-服务预付款,RENT-租金预付款,OTHER-其他)")
    private String prepaymentType;

    @TableField("PREPAYMENT_STATUS")
    @ApiModelProperty(value = "预付款单状态(DRAFT-草稿,SUBMITTED-已提交,APPROVING-审批中,APPROVED-已审批,REJECTED-已驳回,PAID-已支付,WRITEOFF-已核销,CANCELLED-已取消)")
    private String prepaymentStatus;

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

    @TableField("SUPPLIER_ID")
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @TableField("SUPPLIER_NAME")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @TableField("CONTRACT_ID")
    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @TableField("CONTRACT_CODE")
    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @TableField("PREPAYMENT_AMOUNT")
    @ApiModelProperty(value = "预付金额")
    private BigDecimal prepaymentAmount;

    @TableField("PREPAYMENT_DATE")
    @ApiModelProperty(value = "预付款日期")
    private LocalDate prepaymentDate;

    @TableField("PREPAYMENT_REASON")
    @ApiModelProperty(value = "预付款事由")
    private String prepaymentReason;

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
    @ApiModelProperty(value = "支付状态(UNPAID-未支付,PAID-已支付)")
    private String paymentStatus;

    @TableField("PAYMENT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
    private LocalDateTime paymentTime;

    @TableField("PAYMENT_VOUCHER_NO")
    @ApiModelProperty(value = "支付凭证号")
    private String paymentVoucherNo;

    @TableField("WRITEOFF_STATUS")
    @ApiModelProperty(value = "核销状态(UNWRITEOFF-未核销,PARTIAL-部分核销,WRITEOFF-已核销)")
    private String writeoffStatus;

    @TableField("WRITEOFF_AMOUNT")
    @ApiModelProperty(value = "已核销金额")
    private BigDecimal writeoffAmount;

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
