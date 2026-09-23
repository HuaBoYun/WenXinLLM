package com.global.treasurer.financialProductDefinition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 本金计算响应DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-28
 */
@ApiModel("本金计算响应")
public class PrincipalCalculateResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规则编码")
    private String ruleCode;

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("投资金额")
    private BigDecimal investmentAmount;

    @ApiModelProperty("投资期限")
    private Integer investmentPeriod;

    @ApiModelProperty("期限单位")
    private String periodUnit;

    @ApiModelProperty("本金处理方式")
    private String repaymentMethod;

    @ApiModelProperty("本金处理方式名称")
    private String repaymentMethodName;

    @ApiModelProperty("年化收益率(%)")
    private BigDecimal annualRate;

    @ApiModelProperty("预期收益")
    private BigDecimal expectedIncome;

    @ApiModelProperty("本金+收益总计")
    private BigDecimal totalAmount;

    @ApiModelProperty("本金保障率(%)")
    private BigDecimal principalGuaranteeRate;

    @ApiModelProperty("实际保障金额")
    private BigDecimal guaranteedAmount;

    @ApiModelProperty("返还计划列表")
    private List<RepaymentPlan> repaymentPlans;

    // Getter and Setter methods
    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public Integer getInvestmentPeriod() {
        return investmentPeriod;
    }

    public void setInvestmentPeriod(Integer investmentPeriod) {
        this.investmentPeriod = investmentPeriod;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public String getRepaymentMethodName() {
        return repaymentMethodName;
    }

    public void setRepaymentMethodName(String repaymentMethodName) {
        this.repaymentMethodName = repaymentMethodName;
    }

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
    }

    public BigDecimal getExpectedIncome() {
        return expectedIncome;
    }

    public void setExpectedIncome(BigDecimal expectedIncome) {
        this.expectedIncome = expectedIncome;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPrincipalGuaranteeRate() {
        return principalGuaranteeRate;
    }

    public void setPrincipalGuaranteeRate(BigDecimal principalGuaranteeRate) {
        this.principalGuaranteeRate = principalGuaranteeRate;
    }

    public BigDecimal getGuaranteedAmount() {
        return guaranteedAmount;
    }

    public void setGuaranteedAmount(BigDecimal guaranteedAmount) {
        this.guaranteedAmount = guaranteedAmount;
    }

    public List<RepaymentPlan> getRepaymentPlans() {
        return repaymentPlans;
    }

    public void setRepaymentPlans(List<RepaymentPlan> repaymentPlans) {
        this.repaymentPlans = repaymentPlans;
    }
}
