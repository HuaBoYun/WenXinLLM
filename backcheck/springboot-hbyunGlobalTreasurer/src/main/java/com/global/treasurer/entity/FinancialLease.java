package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资租赁实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCIAL_LEASE")
public class FinancialLease implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 租赁ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long leaseId;

    /** 租赁编号 */
    private String leaseNo;

    /** 融资计划ID */
    private Long planId;

    /** 租赁类型 */
    private String leaseType;

    /** 资产名称 */
    private String assetName;

    /** 资产价值 */
    private BigDecimal assetValue;

    /** 币种 */
    private String currencyCode;

    /** 租赁金额 */
    private BigDecimal leaseAmount;

    /** 租赁期限 */
    private Integer leasePeriod;

    /** 期限单位 */
    private String periodUnit;

    /** 租赁费率 */
    private BigDecimal leaseRate;

    /** 开始日期 */
    private Date startDate;

    /** 结束日期 */
    private Date endDate;

    /** 支付方式 */
    private String paymentMethod;

    /** 租赁公司ID */
    private Long leasingCompanyId;

    /** 租赁公司名称 */
    private String leasingCompanyName;

    /** 租赁状态 */
    private String leaseStatus;

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


    public Long getLeaseId() { return leaseId; }
    public void setLeaseId(Long leaseId) { this.leaseId = leaseId; }
    public String getLeaseNo() { return leaseNo; }
    public void setLeaseNo(String leaseNo) { this.leaseNo = leaseNo; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getLeaseType() { return leaseType; }
    public void setLeaseType(String leaseType) { this.leaseType = leaseType; }
    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }
    public BigDecimal getAssetValue() { return assetValue; }
    public void setAssetValue(BigDecimal assetValue) { this.assetValue = assetValue; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getLeaseAmount() { return leaseAmount; }
    public void setLeaseAmount(BigDecimal leaseAmount) { this.leaseAmount = leaseAmount; }
    public Integer getLeasePeriod() { return leasePeriod; }
    public void setLeasePeriod(Integer leasePeriod) { this.leasePeriod = leasePeriod; }
    public String getPeriodUnit() { return periodUnit; }
    public void setPeriodUnit(String periodUnit) { this.periodUnit = periodUnit; }
    public BigDecimal getLeaseRate() { return leaseRate; }
    public void setLeaseRate(BigDecimal leaseRate) { this.leaseRate = leaseRate; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Long getLeasingCompanyId() { return leasingCompanyId; }
    public void setLeasingCompanyId(Long leasingCompanyId) { this.leasingCompanyId = leasingCompanyId; }
    public String getLeasingCompanyName() { return leasingCompanyName; }
    public void setLeasingCompanyName(String leasingCompanyName) { this.leasingCompanyName = leasingCompanyName; }
    public String getLeaseStatus() { return leaseStatus; }
    public void setLeaseStatus(String leaseStatus) { this.leaseStatus = leaseStatus; }
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
