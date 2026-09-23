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
 * 合同主表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_CONTRACT")
@ApiModel(value = "TblContract", description = "合同主表")
public class TblContract implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CONTRACT_ID")
    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @TableField("CONTRACT_CODE")
    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @TableField("CONTRACT_NAME")
    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @TableField("CONTRACT_TYPE")
    @ApiModelProperty(value = "合同类型(PURCHASE-采购合同,SALES-销售合同,SERVICE-服务合同,OTHER-其他)")
    private String contractType;

    @TableField("CONTRACT_STATUS")
    @ApiModelProperty(value = "合同状态(DRAFT-草稿,SUBMITTED-已提交,APPROVING-审批中,APPROVED-已审批,REJECTED-已驳回,EXECUTING-执行中,COMPLETED-已完成,CANCELLED-已取消)")
    private String contractStatus;

    @TableField("PARTY_A")
    @ApiModelProperty(value = "甲方")
    private String partyA;

    @TableField("PARTY_B")
    @ApiModelProperty(value = "乙方")
    private String partyB;

    @TableField("SUPPLIER_ID")
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @TableField("SUPPLIER_NAME")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @TableField("CONTRACT_AMOUNT")
    @ApiModelProperty(value = "合同金额")
    private BigDecimal contractAmount;

    @TableField("SIGNED_DATE")
    @ApiModelProperty(value = "签订日期")
    private LocalDate signedDate;

    @TableField("START_DATE")
    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @TableField("END_DATE")
    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

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

    @TableField("CONTRACT_CONTENT")
    @ApiModelProperty(value = "合同内容")
    private String contractContent;

    @TableField("ATTACHMENT_URL")
    @ApiModelProperty(value = "附件URL")
    private String attachmentUrl;

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
