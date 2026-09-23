package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资产品DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class InvestmentProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 产品ID(更新时必填) */
    private Long productId;

    /** 产品代码 */
    @NotBlank(message = "产品代码不能为空")
    private String productCode;

    /** 产品名称 */
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    /** 产品类型 */
    @NotBlank(message = "产品类型不能为空")
    private String productType;

    /** 发行机构 */
    @NotBlank(message = "发行机构不能为空")
    private String issuer;

    /** 风险等级 */
    @NotBlank(message = "风险等级不能为空")
    private String riskLevel;

    /** 预期收益率 */
    @NotNull(message = "预期收益率不能为空")
    private BigDecimal expectedReturnRate;

    /** 最小投资金额 */
    @NotNull(message = "最小投资金额不能为空")
    private BigDecimal minInvestmentAmount;

    /** 最大投资金额 */
    private BigDecimal maxInvestmentAmount;

    /** 投资期限(天) */
    private Integer investmentTerm;

    /** 净值 */
    private BigDecimal netValue;

    /** 发行日期 */
    @NotNull(message = "发行日期不能为空")
    private Date launchDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 申购开始日期 */
    private Date subscriptionStartDate;

    /** 申购结束日期 */
    private Date subscriptionEndDate;

    /** 允许赎回 */
    private Integer redemptionAllowed;

    /** 产品状态 */
    private String productStatus;

    /** 产品描述 */
    private String productDescription;

    /** 币种 */
    private String currencyCode;

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
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    @Override
    public String toString() {
        return "InvestmentProductDTO{" +
                "productId=" + productId +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", productType='" + productType + '\'' +
                ", issuer='" + issuer + '\'' +
                ", riskLevel='" + riskLevel + '\'' +
                ", expectedReturnRate=" + expectedReturnRate +
                ", minInvestmentAmount=" + minInvestmentAmount +
                ", launchDate=" + launchDate +
                '}';
    }
}
