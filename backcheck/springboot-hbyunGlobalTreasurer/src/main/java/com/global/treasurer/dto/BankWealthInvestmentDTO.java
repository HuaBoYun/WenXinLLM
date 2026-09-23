package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * 银行理财投资DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@ApiModel(value = "BankWealthInvestmentDTO", description = "银行理财投资传输对象")
@Data
public class BankWealthInvestmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("银行理财投资ID")
    private Long bankWealthInvestmentId;

    @ApiModelProperty("投资计划ID")
    private Long planId;

    @ApiModelProperty("产品ID")
    private Long productId;

    @ApiModelProperty("资金账户ID")
    private Long accountId;

    @ApiModelProperty("投资编号")
    private String investmentNo;

    @ApiModelProperty("产品代码")
    private String productCode;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("银行代码")
    private String bankCode;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("风险等级")
    private String riskLevel;

    @ApiModelProperty("投资金额")
    private BigDecimal investmentAmount;

    @ApiModelProperty("预期收益率")
    private BigDecimal expectedReturnRate;

    @ApiModelProperty("实际收益率")
    private BigDecimal actualReturnRate;

    @ApiModelProperty("当前价值")
    private BigDecimal currentValue;

    @ApiModelProperty("预期收益")
    private BigDecimal expectedReturn;

    @ApiModelProperty("实际收益")
    private BigDecimal actualReturn;

    @ApiModelProperty("投资日期")
    private Date investmentDate;

    @ApiModelProperty("到期日期")
    private Date maturityDate;

    @ApiModelProperty("赎回日期")
    private Date redeemDate;

    @ApiModelProperty("赎回金额")
    private BigDecimal redeemAmount;

    @ApiModelProperty("投资状态")
    private String investmentStatus;

    // Getter和Setter
    public Long getBankWealthInvestmentId() { return bankWealthInvestmentId; }
    public void setBankWealthInvestmentId(Long bankWealthInvestmentId) { this.bankWealthInvestmentId = bankWealthInvestmentId; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getInvestmentNo() { return investmentNo; }
    public void setInvestmentNo(String investmentNo) { this.investmentNo = investmentNo; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getInvestmentAmount() { return investmentAmount; }
    public void setInvestmentAmount(BigDecimal investmentAmount) { this.investmentAmount = investmentAmount; }
    public BigDecimal getExpectedReturnRate() { return expectedReturnRate; }
    public void setExpectedReturnRate(BigDecimal expectedReturnRate) { this.expectedReturnRate = expectedReturnRate; }
    public BigDecimal getActualReturnRate() { return actualReturnRate; }
    public void setActualReturnRate(BigDecimal actualReturnRate) { this.actualReturnRate = actualReturnRate; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public BigDecimal getExpectedReturn() { return expectedReturn; }
    public void setExpectedReturn(BigDecimal expectedReturn) { this.expectedReturn = expectedReturn; }
    public BigDecimal getActualReturn() { return actualReturn; }
    public void setActualReturn(BigDecimal actualReturn) { this.actualReturn = actualReturn; }
    public Date getInvestmentDate() { return investmentDate; }
    public void setInvestmentDate(Date investmentDate) { this.investmentDate = investmentDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public Date getRedeemDate() { return redeemDate; }
    public void setRedeemDate(Date redeemDate) { this.redeemDate = redeemDate; }
    public BigDecimal getRedeemAmount() { return redeemAmount; }
    public void setRedeemAmount(BigDecimal redeemAmount) { this.redeemAmount = redeemAmount; }
    public String getInvestmentStatus() { return investmentStatus; }
    public void setInvestmentStatus(String investmentStatus) { this.investmentStatus = investmentStatus; }
}
