package com.global.treasurer.financialProductDefinition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 本金计算请求DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-28
 */
@ApiModel("本金计算请求")
public class PrincipalCalculateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则ID", required = true, example = "7001")
    private Long ruleId;

    @ApiModelProperty(value = "投资金额", required = true, example = "100000")
    private BigDecimal investmentAmount;

    @ApiModelProperty(value = "投资期限", required = true, example = "12")
    private Integer investmentPeriod;

    @ApiModelProperty(value = "期限单位: day-天, month-月, year-年", required = true, example = "month")
    private String periodUnit;

    @ApiModelProperty(value = "年化收益率(%)", example = "4.5")
    private BigDecimal annualRate;

    // Getter and Setter methods
    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
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

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
    }
}
