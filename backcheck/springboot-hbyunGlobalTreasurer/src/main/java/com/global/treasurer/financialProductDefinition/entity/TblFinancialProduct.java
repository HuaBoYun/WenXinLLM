package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 金融产品实体类
 * 对应数据库表：TBL_FINANCIAL_PRODUCT
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCIAL_PRODUCT")
public class TblFinancialProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PRODUCT_ID", type = IdType.INPUT)
    private Long productId;

    @TableField("PRODUCT_CODE")
    private String productCode;

    @TableField("PRODUCT_NAME")
    private String productName;

    @TableField("PRODUCT_TYPE")
    private String productType;

    @TableField("CATEGORY_ID")
    private Long categoryId;

    @TableField("ISSUER")
    private String issuer;

    @TableField("CURRENCY_CODE")
    private String currencyCode;

    @TableField("MIN_INVESTMENT_AMOUNT")
    private BigDecimal minInvestmentAmount;

    @TableField("MAX_INVESTMENT_AMOUNT")
    private BigDecimal maxInvestmentAmount;

    @TableField("EXPECTED_RETURN_RATE")
    private BigDecimal expectedReturnRate;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("INVESTMENT_TERM")
    private Integer investmentTerm;

    @TableField("TERM_UNIT")
    private String termUnit;

    @TableField("SUBSCRIPTION_START_DATE")
    private Date subscriptionStartDate;

    @TableField("SUBSCRIPTION_END_DATE")
    private Date subscriptionEndDate;

    @TableField("EARLY_REDEMPTION")
    private Integer earlyRedemption;

    @TableField("PRODUCT_STATUS")
    private String productStatus;

    @TableField("SHELF_STATUS")
    private String shelfStatus;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("ORG_ID")
    private Long orgId;

    /** 分类名称（非数据库字段） */
    @TableField(exist = false)
    private String categoryName;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getMinInvestmentAmount() { return minInvestmentAmount; }
    public void setMinInvestmentAmount(BigDecimal minInvestmentAmount) { this.minInvestmentAmount = minInvestmentAmount; }
    public BigDecimal getMaxInvestmentAmount() { return maxInvestmentAmount; }
    public void setMaxInvestmentAmount(BigDecimal maxInvestmentAmount) { this.maxInvestmentAmount = maxInvestmentAmount; }
    public BigDecimal getExpectedReturnRate() { return expectedReturnRate; }
    public void setExpectedReturnRate(BigDecimal expectedReturnRate) { this.expectedReturnRate = expectedReturnRate; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public Integer getInvestmentTerm() { return investmentTerm; }
    public void setInvestmentTerm(Integer investmentTerm) { this.investmentTerm = investmentTerm; }
    public String getTermUnit() { return termUnit; }
    public void setTermUnit(String termUnit) { this.termUnit = termUnit; }
    public Date getSubscriptionStartDate() { return subscriptionStartDate; }
    public void setSubscriptionStartDate(Date subscriptionStartDate) { this.subscriptionStartDate = subscriptionStartDate; }
    public Date getSubscriptionEndDate() { return subscriptionEndDate; }
    public void setSubscriptionEndDate(Date subscriptionEndDate) { this.subscriptionEndDate = subscriptionEndDate; }
    public Integer getEarlyRedemption() { return earlyRedemption; }
    public void setEarlyRedemption(Integer earlyRedemption) { this.earlyRedemption = earlyRedemption; }
    public String getProductStatus() { return productStatus; }
    public void setProductStatus(String productStatus) { this.productStatus = productStatus; }
    public String getShelfStatus() { return shelfStatus; }
    public void setShelfStatus(String shelfStatus) { this.shelfStatus = shelfStatus; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

}
