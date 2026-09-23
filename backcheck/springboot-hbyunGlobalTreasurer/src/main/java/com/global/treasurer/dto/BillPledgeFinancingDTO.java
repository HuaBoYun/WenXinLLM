package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除,使用手动编写的getter/setter

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 质押融资DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel("质押融资DTO")
public class BillPledgeFinancingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("融资ID")
    private Long financingId;

    @ApiModelProperty(value = "票据池ID", required = true)
    @NotNull(message = "票据池ID不能为空")
    private Long poolId;

    @ApiModelProperty("融资编号")
    private String financingNumber;

    @ApiModelProperty("融资类型")
    private String financingType;

    @ApiModelProperty(value = "质押金额", required = true)
    @NotNull(message = "质押金额不能为空")
    private BigDecimal pledgeAmount;

    @ApiModelProperty(value = "质押率", required = true)
    @NotNull(message = "质押率不能为空")
    private BigDecimal pledgeRate;

    @ApiModelProperty("融资金额")
    private BigDecimal financingAmount;

    @ApiModelProperty("利率")
    private BigDecimal interestRate;

    @ApiModelProperty(value = "融资期限(天)", required = true)
    @NotNull(message = "融资期限不能为空")
    private Integer financingPeriod;

    @ApiModelProperty("利息金额")
    private BigDecimal interestAmount;

    @ApiModelProperty("还款金额")
    private BigDecimal repayAmount;

    @ApiModelProperty("计划还款日期")
    private Date repayDate;

    @ApiModelProperty("描述")
    private String description;

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getFinancingId() {
        return financingId;
    }

    public void setFinancingId(Long financingId) {
        this.financingId = financingId;
    }

    public Long getPoolId() {
        return poolId;
    }

    public void setPoolId(Long poolId) {
        this.poolId = poolId;
    }

    public String getFinancingNumber() {
        return financingNumber;
    }

    public void setFinancingNumber(String financingNumber) {
        this.financingNumber = financingNumber;
    }

    public String getFinancingType() {
        return financingType;
    }

    public void setFinancingType(String financingType) {
        this.financingType = financingType;
    }

    public BigDecimal getPledgeAmount() {
        return pledgeAmount;
    }

    public void setPledgeAmount(BigDecimal pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    public BigDecimal getPledgeRate() {
        return pledgeRate;
    }

    public void setPledgeRate(BigDecimal pledgeRate) {
        this.pledgeRate = pledgeRate;
    }

    public BigDecimal getFinancingAmount() {
        return financingAmount;
    }

    public void setFinancingAmount(BigDecimal financingAmount) {
        this.financingAmount = financingAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getFinancingPeriod() {
        return financingPeriod;
    }

    public void setFinancingPeriod(Integer financingPeriod) {
        this.financingPeriod = financingPeriod;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

