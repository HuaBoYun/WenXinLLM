package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资产品实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_INVESTMENT_PRODUCT")
public class TblInvestmentProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 产品ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long productId;

    /** 产品代码 */
    private String productCode;

    /** 产品名称 */
    private String productName;

    /** 产品类型(BANK_WEALTH-银行理财,BOND-债券,EQUITY-股权,FUND-基金,DERIVATIVE-衍生品) */
    private String productType;

    /** 发行机构 */
    private String issuer;

    /** 风险等级(LOW-低,MEDIUM-中,HIGH-高) */
    private String riskLevel;

    /** 预期收益率 */
    private BigDecimal expectedReturnRate;

    /** 最小投资金额 */
    private BigDecimal minInvestmentAmount;

    /** 最大投资金额 */
    private BigDecimal maxInvestmentAmount;

    /** 投资期限(天) */
    private Integer investmentTerm;

    /** 净值 */
    private BigDecimal netValue;

    /** 发行日期 */
    private Date launchDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 申购开始日期 */
    private Date subscriptionStartDate;

    /** 申购结束日期 */
    private Date subscriptionEndDate;

    /** 允许赎回(1-是,0-否) */
    private Integer redemptionAllowed;

    /** 产品状态(ACTIVE-活跃,SUSPENDED-暂停,MATURED-已到期,TERMINATED-已终止) */
    private String productStatus;

    /** 产品描述 */
    private String productDescription;

    /** 暂停原因 */
    private String suspendReason;

    /** 币种 */
    private String currencyCode;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 删除标志(0-正常,1-删除) */
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


    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getExpectedReturnRate() { return expectedReturnRate; }
    public void setExpectedReturnRate(BigDecimal expectedReturnRate) { this.expectedReturnRate = expectedReturnRate; }
    public BigDecimal getMinInvestmentAmount() { return minInvestmentAmount; }
    public void setMinInvestmentAmount(BigDecimal minInvestmentAmount) { this.minInvestmentAmount = minInvestmentAmount; }
    public BigDecimal getMaxInvestmentAmount() { return maxInvestmentAmount; }
    public void setMaxInvestmentAmount(BigDecimal maxInvestmentAmount) { this.maxInvestmentAmount = maxInvestmentAmount; }
    public Integer getInvestmentTerm() { return investmentTerm; }
    public void setInvestmentTerm(Integer investmentTerm) { this.investmentTerm = investmentTerm; }
    public BigDecimal getNetValue() { return netValue; }
    public void setNetValue(BigDecimal netValue) { this.netValue = netValue; }
    public Date getLaunchDate() { return launchDate; }
    public void setLaunchDate(Date launchDate) { this.launchDate = launchDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public Date getSubscriptionStartDate() { return subscriptionStartDate; }
    public void setSubscriptionStartDate(Date subscriptionStartDate) { this.subscriptionStartDate = subscriptionStartDate; }
    public Date getSubscriptionEndDate() { return subscriptionEndDate; }
    public void setSubscriptionEndDate(Date subscriptionEndDate) { this.subscriptionEndDate = subscriptionEndDate; }
    public Integer getRedemptionAllowed() { return redemptionAllowed; }
    public void setRedemptionAllowed(Integer redemptionAllowed) { this.redemptionAllowed = redemptionAllowed; }
    public String getProductStatus() { return productStatus; }
    public void setProductStatus(String productStatus) { this.productStatus = productStatus; }
    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    public String getSuspendReason() { return suspendReason; }
    public void setSuspendReason(String suspendReason) { this.suspendReason = suspendReason; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
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
