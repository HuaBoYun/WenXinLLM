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
 * 票据池实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_POOL")
public class TblBillPool implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "POOL_ID", type = IdType.INPUT)
    private Long poolId;

    @TableField("POOL_NAME")
    private String poolName;

    @TableField("POOL_CODE")
    private String poolCode;

    @TableField("POOL_TYPE")
    private String poolType;

    @TableField("OWNER_ID")
    private Long ownerId;

    @TableField("OWNER_NAME")
    private String ownerName;

    @TableField("POOL_PURPOSE")
    private String poolPurpose;

    @TableField("MAX_POOL_VALUE")
    private BigDecimal maxPoolValue;

    @TableField("MIN_BILL_VALUE")
    private BigDecimal minBillValue;

    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @TableField("BILL_COUNT")
    private Integer billCount;

    @TableField("PLEDGE_RATE")
    private BigDecimal pledgeRate;

    @TableField("FINANCING_AMOUNT")
    private BigDecimal financingAmount;

    @TableField("POOL_STATUS")
    private String poolStatus;

    @TableField("ALLOWED_BILL_TYPES")
    private String allowedBillTypes;

    @TableField("POOL_RULES")
    private String poolRules;

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

    public Long getPoolId() {
        return poolId;
    }

    public void setPoolId(Long poolId) {
        this.poolId = poolId;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolCode() {
        return poolCode;
    }

    public void setPoolCode(String poolCode) {
        this.poolCode = poolCode;
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPoolPurpose() {
        return poolPurpose;
    }

    public void setPoolPurpose(String poolPurpose) {
        this.poolPurpose = poolPurpose;
    }

    public BigDecimal getMaxPoolValue() {
        return maxPoolValue;
    }

    public void setMaxPoolValue(BigDecimal maxPoolValue) {
        this.maxPoolValue = maxPoolValue;
    }

    public BigDecimal getMinBillValue() {
        return minBillValue;
    }

    public void setMinBillValue(BigDecimal minBillValue) {
        this.minBillValue = minBillValue;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getBillCount() {
        return billCount;
    }

    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
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

    public String getPoolStatus() {
        return poolStatus;
    }

    public void setPoolStatus(String poolStatus) {
        this.poolStatus = poolStatus;
    }

    public String getAllowedBillTypes() {
        return allowedBillTypes;
    }

    public void setAllowedBillTypes(String allowedBillTypes) {
        this.allowedBillTypes = allowedBillTypes;
    }

    public String getPoolRules() {
        return poolRules;
    }

    public void setPoolRules(String poolRules) {
        this.poolRules = poolRules;
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

