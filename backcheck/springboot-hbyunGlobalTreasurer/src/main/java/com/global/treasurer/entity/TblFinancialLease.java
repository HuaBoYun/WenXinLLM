package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资租赁实体类 - 匹配实际数据库表结构 create_tables.sql
 *
 * 数据库表字段：
 * LEASE_ID, LEASE_NO, LEASE_NAME, COMPANY_ID, COMPANY_NAME,
 * LESSOR_ID, LESSOR_NAME, LEASE_TYPE, LEASE_AMOUNT, OUTSTANDING_AMOUNT,
 * CURRENCY_CODE, INTEREST_RATE, START_DATE, END_DATE, LEASE_STATUS,
 * DELETE_FLAG, CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@TableName("TBL_FINANCIAL_LEASE")
public class TblFinancialLease implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 租赁ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long leaseId;

    /** 租赁编号 */
    private String leaseNo;

    /** 租赁名称 */
    private String leaseName;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 出租方ID */
    private Long lessorId;

    /** 出租方名称（租赁公司） */
    private String lessorName;

    /** 租赁类型 */
    private String leaseType;

    /** 租赁金额 */
    private BigDecimal leaseAmount;

    /** 未偿还金额 */
    private BigDecimal outstandingAmount;

    /** 币种 */
    private String currencyCode;

    /** 利率 */
    private BigDecimal interestRate;

    /** 起租日期 */
    private Date startDate;

    /** 到期日期 */
    private Date endDate;

    /** 租赁状态 */
    private String leaseStatus;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    /** 资产名称 */
    private String assetName;

    /** 资产价值 */
    private BigDecimal assetValue;

    /** 支付方式 */
    private String paymentMethod;

    /** 备注 */
    private String remark;

    /** 租赁期限 */
    private Integer leasePeriod;

    /** 期限单位 */
    private String periodUnit;

    // ==================== Getter/Setter 方法 ====================

    public Long getLeaseId() { return leaseId; }
    public void setLeaseId(Long leaseId) { this.leaseId = leaseId; }

    public String getLeaseNo() { return leaseNo; }
    public void setLeaseNo(String leaseNo) { this.leaseNo = leaseNo; }

    public String getLeaseName() { return leaseName; }
    public void setLeaseName(String leaseName) { this.leaseName = leaseName; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Long getLessorId() { return lessorId; }
    public void setLessorId(Long lessorId) { this.lessorId = lessorId; }

    public String getLessorName() { return lessorName; }
    public void setLessorName(String lessorName) { this.lessorName = lessorName; }

    public String getLeaseType() { return leaseType; }
    public void setLeaseType(String leaseType) { this.leaseType = leaseType; }

    public BigDecimal getLeaseAmount() { return leaseAmount; }
    public void setLeaseAmount(BigDecimal leaseAmount) { this.leaseAmount = leaseAmount; }

    public BigDecimal getOutstandingAmount() { return outstandingAmount; }
    public void setOutstandingAmount(BigDecimal outstandingAmount) { this.outstandingAmount = outstandingAmount; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getLeaseStatus() { return leaseStatus; }
    public void setLeaseStatus(String leaseStatus) { this.leaseStatus = leaseStatus; }

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

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public BigDecimal getAssetValue() { return assetValue; }
    public void setAssetValue(BigDecimal assetValue) { this.assetValue = assetValue; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Integer getLeasePeriod() { return leasePeriod; }
    public void setLeasePeriod(Integer leasePeriod) { this.leasePeriod = leasePeriod; }

    public String getPeriodUnit() { return periodUnit; }
    public void setPeriodUnit(String periodUnit) { this.periodUnit = periodUnit; }

    // ==================== 兼容旧字段的别名方法 ====================
    // 为了兼容前端和其他代码中使用的旧字段名

    /** 获取租赁公司（兼容旧字段名） */
    public String getLeasingCompany() { return lessorName; }
    public void setLeasingCompany(String leasingCompany) { this.lessorName = leasingCompany; }

    /** 获取租赁类型（兼容旧字段名） */
    public String getLeasingType() { return leaseType; }
    public void setLeasingType(String leasingType) { this.leaseType = leasingType; }

    /** 获取租赁金额（兼容旧字段名） */
    public BigDecimal getLeasingAmount() { return leaseAmount; }
    public void setLeasingAmount(BigDecimal leasingAmount) { this.leaseAmount = leasingAmount; }

    /** 获取申请状态（兼容旧字段名） */
    public String getApplicationStatus() { return leaseStatus; }
    public void setApplicationStatus(String applicationStatus) { this.leaseStatus = applicationStatus; }
}
