package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资成本分析DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingCostDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 分析ID */
    private Long analysisId;

    /** 融资ID */
    @NotNull(message = "融资ID不能为空")
    private Long financingId;

    /** 融资类型 */
    @NotBlank(message = "融资类型不能为空")
    private String financingType;

    /** 本金 */
    @NotNull(message = "本金不能为空")
    private BigDecimal principalAmount;

    /** 利息支出 */
    private BigDecimal interestExpense;

    /** 手续费用 */
    private BigDecimal feeCost;

    /** 担保费用 */
    private BigDecimal guaranteeCost;

    /** 其他费用 */
    private BigDecimal otherCost;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 分析日期 */
    private Date analysisDate;

    /** 周期类型(MONTH-月,QUARTER-季,YEAR-年) */
    private String periodType;

    /** 周期值 */
    private String periodValue;

    /** 公司ID */
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAnalysisId() { return analysisId; }
    public void setAnalysisId(Long analysisId) { this.analysisId = analysisId; }
    public Long getFinancingId() { return financingId; }
    public void setFinancingId(Long financingId) { this.financingId = financingId; }
    public String getFinancingType() { return financingType; }
    public void setFinancingType(String financingType) { this.financingType = financingType; }
    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }
    public BigDecimal getInterestExpense() { return interestExpense; }
    public void setInterestExpense(BigDecimal interestExpense) { this.interestExpense = interestExpense; }
    public BigDecimal getFeeCost() { return feeCost; }
    public void setFeeCost(BigDecimal feeCost) { this.feeCost = feeCost; }
    public BigDecimal getGuaranteeCost() { return guaranteeCost; }
    public void setGuaranteeCost(BigDecimal guaranteeCost) { this.guaranteeCost = guaranteeCost; }
    public BigDecimal getOtherCost() { return otherCost; }
    public void setOtherCost(BigDecimal otherCost) { this.otherCost = otherCost; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Date getAnalysisDate() { return analysisDate; }
    public void setAnalysisDate(Date analysisDate) { this.analysisDate = analysisDate; }
    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }
    public String getPeriodValue() { return periodValue; }
    public void setPeriodValue(String periodValue) { this.periodValue = periodValue; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
