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
 * 产品本金规则实体类
 * 对应数据库表：TBL_PRODUCT_PRINCIPAL_RULE
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PRODUCT_PRINCIPAL_RULE")
public class TblProductPrincipalRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "RULE_ID", type = IdType.INPUT)
    private Long ruleId;

    @TableField("RULE_CODE")
    private String ruleCode;

    @TableField("RULE_NAME")
    private String ruleName;

    @TableField("REPAYMENT_METHOD")
    private String repaymentMethod;

    @TableField("REPAYMENT_FREQUENCY")
    private String repaymentFrequency;

    @TableField("GRACE_PERIOD")
    private Integer gracePeriod;

    @TableField("PREPAYMENT_ALLOWED")
    private Integer prepaymentAllowed;

    @TableField("PREPAYMENT_PENALTY_RATE")
    private BigDecimal prepaymentPenaltyRate;

    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @TableField("DESCRIPTION")
    private String description;

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

    @TableField("PRODUCT_TYPE")
    private String productType;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("MIN_INVESTMENT_AMOUNT")
    private BigDecimal minInvestmentAmount;

    @TableField("MAX_INVESTMENT_AMOUNT")
    private BigDecimal maxInvestmentAmount;

    @TableField("PRINCIPAL_GUARANTEE_RATE")
    private BigDecimal principalGuaranteeRate;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRepaymentMethod() { return repaymentMethod; }
    public void setRepaymentMethod(String repaymentMethod) { this.repaymentMethod = repaymentMethod; }
    public String getRepaymentFrequency() { return repaymentFrequency; }
    public void setRepaymentFrequency(String repaymentFrequency) { this.repaymentFrequency = repaymentFrequency; }
    public Integer getGracePeriod() { return gracePeriod; }
    public void setGracePeriod(Integer gracePeriod) { this.gracePeriod = gracePeriod; }
    public Integer getPrepaymentAllowed() { return prepaymentAllowed; }
    public void setPrepaymentAllowed(Integer prepaymentAllowed) { this.prepaymentAllowed = prepaymentAllowed; }
    public BigDecimal getPrepaymentPenaltyRate() { return prepaymentPenaltyRate; }
    public void setPrepaymentPenaltyRate(BigDecimal prepaymentPenaltyRate) { this.prepaymentPenaltyRate = prepaymentPenaltyRate; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
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

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public BigDecimal getMinInvestmentAmount() { return minInvestmentAmount; }
    public void setMinInvestmentAmount(BigDecimal minInvestmentAmount) { this.minInvestmentAmount = minInvestmentAmount; }

    public BigDecimal getMaxInvestmentAmount() { return maxInvestmentAmount; }
    public void setMaxInvestmentAmount(BigDecimal maxInvestmentAmount) { this.maxInvestmentAmount = maxInvestmentAmount; }

    public BigDecimal getPrincipalGuaranteeRate() { return principalGuaranteeRate; }
    public void setPrincipalGuaranteeRate(BigDecimal principalGuaranteeRate) { this.principalGuaranteeRate = principalGuaranteeRate; }

}
