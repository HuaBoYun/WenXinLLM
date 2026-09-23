package com.global.treasurer.financialProductDefinition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 返还计划
 *
 * @author 华博云开发团队
 * @since 2026-02-28
 */
@ApiModel("返还计划")
public class RepaymentPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("期数")
    private Integer period;

    @ApiModelProperty("应还日期")
    private Date repaymentDate;

    @ApiModelProperty("应还本金")
    private BigDecimal principal;

    @ApiModelProperty("应还收益")
    private BigDecimal income;

    @ApiModelProperty("应还总额")
    private BigDecimal total;

    @ApiModelProperty("备注")
    private String remark;

    // Getter and Setter methods
    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Date repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
