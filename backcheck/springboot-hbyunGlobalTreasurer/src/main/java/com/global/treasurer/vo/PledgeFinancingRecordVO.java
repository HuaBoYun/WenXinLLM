package com.global.treasurer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 质押融资记录VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel("质押融资记录VO")
public class PledgeFinancingRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("融资ID")
    private Long financingId;

    @ApiModelProperty("票据池ID")
    private Long poolId;

    @ApiModelProperty("票据池名称")
    private String poolName;

    @ApiModelProperty("融资编号")
    private String financingNumber;

    @ApiModelProperty("融资类型")
    private String financingType;

    @ApiModelProperty("质押金额")
    private BigDecimal pledgeAmount;

    @ApiModelProperty("质押率")
    private BigDecimal pledgeRate;

    @ApiModelProperty("融资金额")
    private BigDecimal financingAmount;

    @ApiModelProperty("利率")
    private BigDecimal interestRate;

    @ApiModelProperty("融资期限(天)")
    private Integer financingPeriod;

    @ApiModelProperty("利息金额")
    private BigDecimal interestAmount;

    @ApiModelProperty("还款金额")
    private BigDecimal repayAmount;

    @ApiModelProperty("计划还款日期")
    private Date repayDate;

    @ApiModelProperty("实际还款日期")
    private Date actualRepayDate;

    @ApiModelProperty("还款状态")
    private String repayStatus;

    @ApiModelProperty("融资状态")
    private String financingStatus;

    @ApiModelProperty("票据数量")
    private Integer billCount;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getFinancingId() { return financingId; }
    public void setFinancingId(Long financingId) { this.financingId = financingId; }
    public Long getPoolId() { return poolId; }
    public void setPoolId(Long poolId) { this.poolId = poolId; }
    public String getPoolName() { return poolName; }
    public void setPoolName(String poolName) { this.poolName = poolName; }
    public String getFinancingNumber() { return financingNumber; }
    public void setFinancingNumber(String financingNumber) { this.financingNumber = financingNumber; }
    public String getFinancingType() { return financingType; }
    public void setFinancingType(String financingType) { this.financingType = financingType; }
    public BigDecimal getPledgeAmount() { return pledgeAmount; }
    public void setPledgeAmount(BigDecimal pledgeAmount) { this.pledgeAmount = pledgeAmount; }
    public BigDecimal getPledgeRate() { return pledgeRate; }
    public void setPledgeRate(BigDecimal pledgeRate) { this.pledgeRate = pledgeRate; }
    public BigDecimal getFinancingAmount() { return financingAmount; }
    public void setFinancingAmount(BigDecimal financingAmount) { this.financingAmount = financingAmount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public Integer getFinancingPeriod() { return financingPeriod; }
    public void setFinancingPeriod(Integer financingPeriod) { this.financingPeriod = financingPeriod; }
    public BigDecimal getInterestAmount() { return interestAmount; }
    public void setInterestAmount(BigDecimal interestAmount) { this.interestAmount = interestAmount; }
    public BigDecimal getRepayAmount() { return repayAmount; }
    public void setRepayAmount(BigDecimal repayAmount) { this.repayAmount = repayAmount; }
    public Date getRepayDate() { return repayDate; }
    public void setRepayDate(Date repayDate) { this.repayDate = repayDate; }
    public Date getActualRepayDate() { return actualRepayDate; }
    public void setActualRepayDate(Date actualRepayDate) { this.actualRepayDate = actualRepayDate; }
    public String getRepayStatus() { return repayStatus; }
    public void setRepayStatus(String repayStatus) { this.repayStatus = repayStatus; }
    public String getFinancingStatus() { return financingStatus; }
    public void setFinancingStatus(String financingStatus) { this.financingStatus = financingStatus; }
    public Integer getBillCount() { return billCount; }
    public void setBillCount(Integer billCount) { this.billCount = billCount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

}
