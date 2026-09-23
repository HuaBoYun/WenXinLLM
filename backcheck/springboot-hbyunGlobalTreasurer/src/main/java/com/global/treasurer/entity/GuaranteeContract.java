package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 担保合同实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_GUARANTEE_CONTRACT")
public class GuaranteeContract implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 合同ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long contractId;

    /** 合同编号 */
    private String contractNo;

    /** 申请ID */
    private Long applicationId;

    /** 担保类型 */
    private String guaranteeType;

    /** 担保金额 */
    private BigDecimal guaranteeAmount;

    /** 币种 */
    private String currencyCode;

    /** 担保期限 */
    private Integer guaranteePeriod;

    /** 期限单位 */
    private String periodUnit;

    /** 担保开始日期 */
    private Date guaranteeStartDate;

    /** 担保结束日期 */
    private Date guaranteeEndDate;

    /** 担保费率 */
    private BigDecimal guaranteeRate;

    /** 担保费用 */
    private BigDecimal guaranteeFee;

    /** 支付方式 */
    private String paymentMethod;

    /** 担保人ID */
    private Long guarantorId;

    /** 担保人姓名 */
    private String guarantorName;

    /** 受益人ID */
    private Long beneficiaryId;

    /** 受益人名称 */
    private String beneficiaryName;

    /** 担保方式 */
    private String guaranteeMode;

    /** 合同状态 */
    private String contractStatus;

    /** 签署日期 */
    private Date signingDate;

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


    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public BigDecimal getGuaranteeAmount() { return guaranteeAmount; }
    public void setGuaranteeAmount(BigDecimal guaranteeAmount) { this.guaranteeAmount = guaranteeAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getGuaranteePeriod() { return guaranteePeriod; }
    public void setGuaranteePeriod(Integer guaranteePeriod) { this.guaranteePeriod = guaranteePeriod; }
    public String getPeriodUnit() { return periodUnit; }
    public void setPeriodUnit(String periodUnit) { this.periodUnit = periodUnit; }
    public Date getGuaranteeStartDate() { return guaranteeStartDate; }
    public void setGuaranteeStartDate(Date guaranteeStartDate) { this.guaranteeStartDate = guaranteeStartDate; }
    public Date getGuaranteeEndDate() { return guaranteeEndDate; }
    public void setGuaranteeEndDate(Date guaranteeEndDate) { this.guaranteeEndDate = guaranteeEndDate; }
    public BigDecimal getGuaranteeRate() { return guaranteeRate; }
    public void setGuaranteeRate(BigDecimal guaranteeRate) { this.guaranteeRate = guaranteeRate; }
    public BigDecimal getGuaranteeFee() { return guaranteeFee; }
    public void setGuaranteeFee(BigDecimal guaranteeFee) { this.guaranteeFee = guaranteeFee; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Long getGuarantorId() { return guarantorId; }
    public void setGuarantorId(Long guarantorId) { this.guarantorId = guarantorId; }
    public String getGuarantorName() { return guarantorName; }
    public void setGuarantorName(String guarantorName) { this.guarantorName = guarantorName; }
    public Long getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(Long beneficiaryId) { this.beneficiaryId = beneficiaryId; }
    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }
    public String getGuaranteeMode() { return guaranteeMode; }
    public void setGuaranteeMode(String guaranteeMode) { this.guaranteeMode = guaranteeMode; }
    public String getContractStatus() { return contractStatus; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    public Date getSigningDate() { return signingDate; }
    public void setSigningDate(Date signingDate) { this.signingDate = signingDate; }
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
