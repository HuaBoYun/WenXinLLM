package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 融资租赁DTO
 *
 * 注意：前端表单字段名与数据库字段名存在差异，此DTO同时支持两种命名
 *
 * @author HuaBo Cloud
 * @since 2025-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancialLeaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 租赁ID（前端表单使用 id）
     */
    private Long leaseId;

    /**
     * ID（前端表单字段名，映射到 leaseId）
     */
    private Long id;

    /**
     * 租赁编号
     */
    private String leaseNo;

    /**
     * 租赁名称
     */
    private String leaseName;

    /**
     * 出租方公司（数据库字段名）
     */
    private String lessorCompany;

    /**
     * 租赁公司（前端表单字段名，映射到 lessorCompany）
     */
    private String leasingCompany;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 租赁类型（字符串类型：DIRECT/LEASEBACK/LEVERAGED/OPERATING）
     */
    private String leaseType;

    /**
     * 租赁类型（前端表单字段名，映射到 leaseType）
     */
    private String leasingType;

    /**
     * 租赁类型名称
     */
    private String leaseTypeName;

    // ========== 前端表单额外字段 ==========

    /**
     * 资产名称（前端表单字段）
     */
    private String assetName;

    /**
     * 租赁金额（前端表单字段，映射到数据库 LEASE_AMOUNT）
     */
    private BigDecimal leaseAmount;

    /**
     * 币种代码（前端表单字段）
     */
    private String currencyCode;

    /**
     * 租赁期限（前端表单字段）
     */
    private Integer leasePeriod;

    /**
     * 期限单位（前端表单字段：MONTH/YEAR）
     */
    private String periodUnit;

    /**
     * 公司ID（前端表单字段）
     */
    private String companyId;

    /**
     * 公司名称（前端表单字段）
     */
    private String companyName;

    /**
     * 申请状态（前端表单字段名，映射到 leaseStatus）
     */
    private String applicationStatus;

    /**
     * 资产原值
     */
    private BigDecimal assetValue;

    /**
     * 租赁开始日期
     */
    private LocalDate leaseStartDate;

    /**
     * 租赁结束日期
     */
    private LocalDate leaseEndDate;

    /**
     * 租赁期限(月)
     */
    private Integer leaseTerm;

    /**
     * 租赁利率(%)
     */
    private BigDecimal leaseRate;

    /**
     * 总租金
     */
    private BigDecimal totalLeaseAmount;

    /**
     * 支付方式（字符串类型：EQUAL_PRINCIPAL_INTEREST/EQUAL_PRINCIPAL/INTEREST_FIRST/LUMP_SUM）
     */
    private String paymentMethod;

    /**
     * 支付方式名称
     */
    private String paymentMethodName;

    /**
     * 支付周期(月)
     */
    private Integer paymentCycle;

    /**
     * 担保方式（字符串类型）
     */
    private String guaranteeType;

    /**
     * 担保方式名称
     */
    private String guaranteeTypeName;

    /**
     * 担保金额
     */
    private BigDecimal guaranteeAmount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 租赁状态（字符串类型：PENDING/SUBMITTED/APPROVED/ACTIVE/COMPLETED/REJECTED）
     */
    private String leaseStatus;

    /**
     * 租赁状态名称
     */
    private String leaseStatusName;

    /**
     * 剩余本金
     */
    private BigDecimal remainingPrincipal;

    /**
     * 已支付金额
     */
    private BigDecimal totalPaidAmount;

    /**
     * 经办人
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // 查询参数
    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer limit;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    // ==================== Getter/Setter 方法 ====================

    public Long getLeaseId() { return leaseId; }
    public void setLeaseId(Long leaseId) { this.leaseId = leaseId; }
    public String getLeaseNo() { return leaseNo; }
    public void setLeaseNo(String leaseNo) { this.leaseNo = leaseNo; }
    public String getLeaseName() { return leaseName; }
    public void setLeaseName(String leaseName) { this.leaseName = leaseName; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLessorCompany() { return lessorCompany; }
    public void setLessorCompany(String lessorCompany) { this.lessorCompany = lessorCompany; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getLeaseType() { return leaseType; }
    public void setLeaseType(String leaseType) { this.leaseType = leaseType; }
    public String getLeaseTypeName() { return leaseTypeName; }
    public void setLeaseTypeName(String leaseTypeName) { this.leaseTypeName = leaseTypeName; }
    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }
    public BigDecimal getAssetValue() { return assetValue; }
    public void setAssetValue(BigDecimal assetValue) { this.assetValue = assetValue; }
    public LocalDate getLeaseStartDate() { return leaseStartDate; }
    public void setLeaseStartDate(LocalDate leaseStartDate) { this.leaseStartDate = leaseStartDate; }
    public LocalDate getLeaseEndDate() { return leaseEndDate; }
    public void setLeaseEndDate(LocalDate leaseEndDate) { this.leaseEndDate = leaseEndDate; }
    public Integer getLeaseTerm() { return leaseTerm; }
    public void setLeaseTerm(Integer leaseTerm) { this.leaseTerm = leaseTerm; }
    public BigDecimal getLeaseRate() { return leaseRate; }
    public void setLeaseRate(BigDecimal leaseRate) { this.leaseRate = leaseRate; }
    public BigDecimal getTotalLeaseAmount() { return totalLeaseAmount; }
    public void setTotalLeaseAmount(BigDecimal totalLeaseAmount) { this.totalLeaseAmount = totalLeaseAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentMethodName() { return paymentMethodName; }
    public void setPaymentMethodName(String paymentMethodName) { this.paymentMethodName = paymentMethodName; }
    public Integer getPaymentCycle() { return paymentCycle; }
    public void setPaymentCycle(Integer paymentCycle) { this.paymentCycle = paymentCycle; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public String getGuaranteeTypeName() { return guaranteeTypeName; }
    public void setGuaranteeTypeName(String guaranteeTypeName) { this.guaranteeTypeName = guaranteeTypeName; }
    public BigDecimal getGuaranteeAmount() { return guaranteeAmount; }
    public void setGuaranteeAmount(BigDecimal guaranteeAmount) { this.guaranteeAmount = guaranteeAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getLeaseStatus() { return leaseStatus; }
    public void setLeaseStatus(String leaseStatus) { this.leaseStatus = leaseStatus; }
    public String getLeaseStatusName() { return leaseStatusName; }
    public void setLeaseStatusName(String leaseStatusName) { this.leaseStatusName = leaseStatusName; }
    public BigDecimal getRemainingPrincipal() { return remainingPrincipal; }
    public void setRemainingPrincipal(BigDecimal remainingPrincipal) { this.remainingPrincipal = remainingPrincipal; }
    public BigDecimal getTotalPaidAmount() { return totalPaidAmount; }
    public void setTotalPaidAmount(BigDecimal totalPaidAmount) { this.totalPaidAmount = totalPaidAmount; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    // ==================== 前端表单字段的 Getter/Setter ====================

    public String getLeasingCompany() { return leasingCompany; }
    public void setLeasingCompany(String leasingCompany) { this.leasingCompany = leasingCompany; }
    public String getLeasingType() { return leasingType; }
    public void setLeasingType(String leasingType) { this.leasingType = leasingType; }
    public BigDecimal getLeaseAmount() { return leaseAmount; }
    public void setLeaseAmount(BigDecimal leaseAmount) { this.leaseAmount = leaseAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getLeasePeriod() { return leasePeriod; }
    public void setLeasePeriod(Integer leasePeriod) { this.leasePeriod = leasePeriod; }
    public String getPeriodUnit() { return periodUnit; }
    public void setPeriodUnit(String periodUnit) { this.periodUnit = periodUnit; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
}
