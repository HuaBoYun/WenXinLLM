package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资成本分析实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCING_COST_ANALYSIS")
public class TblFinancingCostAnalysis implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 分析ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long analysisId;

    /** 融资ID */
    private Long financingId;

    /** 融资类型 */
    private String financingType;

    /** 本金 */
    private BigDecimal principalAmount;

    /** 利息支出 */
    private BigDecimal interestExpense;

    /** 手续费用 */
    private BigDecimal feeCost;

    /** 担保费用 */
    private BigDecimal guaranteeCost;

    /** 其他费用 */
    private BigDecimal otherCost;

    /** 总成本 */
    private BigDecimal totalCost;

    /** 成本率 */
    private BigDecimal costRate;

    /** 币种 */
    private String currencyCode;

    /** 分析日期 */
    private Date analysisDate;

    /** 周期类型(MONTH-月,QUARTER-季,YEAR-年) */
    private String periodType;

    /** 周期值 */
    private String periodValue;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

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
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public BigDecimal getCostRate() { return costRate; }
    public void setCostRate(BigDecimal costRate) { this.costRate = costRate; }
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
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
