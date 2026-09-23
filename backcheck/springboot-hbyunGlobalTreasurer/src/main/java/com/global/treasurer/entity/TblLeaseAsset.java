package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 租赁资产实体类
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@TableName("TBL_LEASE_ASSET")
public class TblLeaseAsset implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 资产ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long assetId;

    /** 租赁ID */
    private Long leaseId;

    /** 资产编号 */
    private String assetCode;

    /** 资产名称 */
    private String assetName;

    /** 资产类型(EQUIPMENT-设备,VEHICLE-车辆,PROPERTY-房产,OTHER-其他) */
    private String assetType;

    /** 资产价值 */
    private BigDecimal assetValue;

    /** 购置日期 */
    private Date purchaseDate;

    /** 存放位置 */
    private String location;

    /** 资产描述 */
    private String description;

    /** 资产状态(NORMAL-正常,ABNORMAL-异常,DISPOSED-已处置) */
    private String status;

    /** 创建人ID */
    private Long createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人ID */
    private Long updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    // Getters and Setters
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public Long getLeaseId() { return leaseId; }
    public void setLeaseId(Long leaseId) { this.leaseId = leaseId; }

    public String getAssetCode() { return assetCode; }
    public void setAssetCode(String assetCode) { this.assetCode = assetCode; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }

    public BigDecimal getAssetValue() { return assetValue; }
    public void setAssetValue(BigDecimal assetValue) { this.assetValue = assetValue; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }

    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }

    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
}

