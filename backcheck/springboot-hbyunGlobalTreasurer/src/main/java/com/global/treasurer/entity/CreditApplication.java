package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2026-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CREDIT_APPLICATION")
public class CreditApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long applicationId;

    /** 申请编号 */
    private String applicationNo;

    /** 授信类型 */
    private String creditType;

    /** 申请金额 */
    private BigDecimal appliedAmount;

    /** 币种 */
    private String currencyCode;

    /** 授信用途 */
    private String creditPurpose;

    /** 授信期限 */
    private Integer creditPeriod;

    /** 期限单位 */
    private String periodUnit;

    /** 利率 */
    private BigDecimal interestRate;

    /** 金融机构ID */
    private Long financialInstitutionId;

    /** 金融机构名称 */
    private String financialInstitutionName;

    /** 申请状态 */
    private String applicationStatus;

    /** 申请日期 */
    private Date applicationDate;

    /** 审批日期 */
    private Date approvalDate;

    /** 审批金额 */
    private BigDecimal approvalAmount;

    /** 审批意见 */
    private String approvalOpinion;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }
    public String getCreditType() { return creditType; }
    public void setCreditType(String creditType) { this.creditType = creditType; }
    public BigDecimal getAppliedAmount() { return appliedAmount; }
    public void setAppliedAmount(BigDecimal appliedAmount) { this.appliedAmount = appliedAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getCreditPurpose() { return creditPurpose; }
    public void setCreditPurpose(String creditPurpose) { this.creditPurpose = creditPurpose; }
    public Integer getCreditPeriod() { return creditPeriod; }
    public void setCreditPeriod(Integer creditPeriod) { this.creditPeriod = creditPeriod; }
    public String getPeriodUnit() { return periodUnit; }
    public void setPeriodUnit(String periodUnit) { this.periodUnit = periodUnit; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public Long getFinancialInstitutionId() { return financialInstitutionId; }
    public void setFinancialInstitutionId(Long financialInstitutionId) { this.financialInstitutionId = financialInstitutionId; }
    public String getFinancialInstitutionName() { return financialInstitutionName; }
    public void setFinancialInstitutionName(String financialInstitutionName) { this.financialInstitutionName = financialInstitutionName; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
    public BigDecimal getApprovalAmount() { return approvalAmount; }
    public void setApprovalAmount(BigDecimal approvalAmount) { this.approvalAmount = approvalAmount; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
