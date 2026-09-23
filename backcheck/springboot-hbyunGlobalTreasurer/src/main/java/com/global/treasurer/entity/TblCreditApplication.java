package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 授信申请实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CREDIT_APPLICATION")
public class TblCreditApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    @TableId(type = IdType.AUTO)
    private Long applicationId;

    /** 申请编号 */
    @TableField("APPLICATION_NO")
    private String applicationNo;

    /** 公司ID */
    @TableField("COMPANY_ID")
    private Long companyId;

    /** 公司名称 */
    @TableField("COMPANY_NAME")
    private String companyName;

    /** 银行ID */
    @TableField("BANK_ID")
    private Long bankId;

    /** 银行名称 */
    @TableField("BANK_NAME")
    private String bankName;

    /** 授信类型 */
    @TableField("CREDIT_TYPE")
    private String creditType;

    /** 申请金额 */
    @TableField("APPLY_AMOUNT")
    private BigDecimal applyAmount;

    /** 批准金额 */
    @TableField("APPROVED_AMOUNT")
    private BigDecimal approvedAmount;

    /** 币种 */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /** 申请日期 */
    @TableField("APPLY_DATE")
    private Date applyDate;

    /** 预计开始日期 */
    @TableField("EXPECTED_START_DATE")
    private Date expectedStartDate;

    /** 预计结束日期 */
    @TableField("EXPECTED_END_DATE")
    private Date expectedEndDate;

    /** 申请状态(DRAFT-草稿,PENDING-待审批,APPROVED-已审批,REJECTED-已拒绝) */
    @TableField("APPLICATION_STATUS")
    private String applicationStatus;

    /** 删除标志 */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 创建人 */
    @TableField("CREATED_BY")
    private String createdBy;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /** 审批日期 */
    @TableField("APPROVAL_DATE")
    private Date approvalDate;

    /** 审批意见 */
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;


    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getCreditType() { return creditType; }
    public void setCreditType(String creditType) { this.creditType = creditType; }

    public BigDecimal getApplyAmount() { return applyAmount; }
    public void setApplyAmount(BigDecimal applyAmount) { this.applyAmount = applyAmount; }

    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public Date getApplyDate() { return applyDate; }
    public void setApplyDate(Date applyDate) { this.applyDate = applyDate; }

    public Date getExpectedStartDate() { return expectedStartDate; }
    public void setExpectedStartDate(Date expectedStartDate) { this.expectedStartDate = expectedStartDate; }

    public Date getExpectedEndDate() { return expectedEndDate; }
    public void setExpectedEndDate(Date expectedEndDate) { this.expectedEndDate = expectedEndDate; }

    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }

    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }

    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }

    public String getApprovalComments() { return approvalComments; }
    public void setApprovalComments(String approvalComments) { this.approvalComments = approvalComments; }

    // 为了兼容前端，添加别名方法
    public BigDecimal getCreditAmount() { return applyAmount; }
    public void setCreditAmount(BigDecimal creditAmount) { this.applyAmount = creditAmount; }

    public Date getApplicationTime() { return applyDate; }
    public void setApplicationTime(Date applicationTime) { this.applyDate = applicationTime; }

    public Date getApplicationDate() { return applyDate; }
    public void setApplicationDate(Date applicationDate) { this.applyDate = applicationDate; }

    public String getOrgName() { return companyName; }
    public void setOrgName(String orgName) { this.companyName = orgName; }
}
