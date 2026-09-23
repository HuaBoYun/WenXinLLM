package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除,使用手动编写的getter/setter

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 质押融资实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PLEDGE_FINANCING")
public class TblPledgeFinancing implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "FINANCING_ID", type = IdType.INPUT)
    private Long financingId;

    @TableField("POOL_ID")
    private Long poolId;

    @TableField("FINANCING_NUMBER")
    private String financingNumber;

    @TableField("FINANCING_TYPE")
    private String financingType;

    @TableField("PLEDGE_AMOUNT")
    private BigDecimal pledgeAmount;

    @TableField("PLEDGE_RATE")
    private BigDecimal pledgeRate;

    @TableField("FINANCING_AMOUNT")
    private BigDecimal financingAmount;

    @TableField("INTEREST_RATE")
    private BigDecimal interestRate;

    @TableField("FINANCING_PERIOD")
    private Integer financingPeriod;

    @TableField("INTEREST_AMOUNT")
    private BigDecimal interestAmount;

    @TableField("REPAY_AMOUNT")
    private BigDecimal repayAmount;

    @TableField("REPAY_DATE")
    private Date repayDate;

    @TableField("ACTUAL_REPAY_DATE")
    private Date actualRepayDate;

    @TableField("REPAY_STATUS")
    private String repayStatus;

    @TableField("FINANCING_STATUS")
    private String financingStatus;

    @TableField("BILL_COUNT")
    private Integer billCount;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

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

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public String getFinancingStatus() {
        return financingStatus;
    }

    public void setFinancingStatus(String financingStatus) {
        this.financingStatus = financingStatus;
    }

    public Integer getBillCount() {
        return billCount;
    }

    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}

