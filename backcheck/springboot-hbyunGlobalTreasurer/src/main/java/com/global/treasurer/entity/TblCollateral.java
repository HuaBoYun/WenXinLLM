package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 担保物实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@TableName("TBL_COLLATERAL")
public class TblCollateral implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 担保物ID - 使用数据库自增 */
    @TableId(value = "COLLATERAL_ID", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long collateralId;

    /** 担保物编号 */
    @TableField("COLLATERAL_NO")
    private String collateralNo;

    /** 担保物类型(PROPERTY-房产,LAND-土地,EQUIPMENT-设备,INVENTORY-存货) */
    @TableField("COLLATERAL_TYPE")
    private String collateralType;

    /** 担保物名称 */
    @TableField("COLLATERAL_NAME")
    private String collateralName;

    /** 担保物价值 */
    @TableField("COLLATERAL_VALUE")
    private BigDecimal collateralValue;

    /** 币种 */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /** 评估价值 */
    @TableField("EVALUATION_VALUE")
    private BigDecimal evaluationValue;

    /** 评估日期 */
    @TableField("EVALUATION_DATE")
    private Date evaluationDate;

    /** 评估机构 */
    @TableField("EVALUATION_AGENCY")
    private String evaluationAgency;

    /** 担保物状态 */
    @TableField("COLLATERAL_STATUS")
    private String collateralStatus;

    /** 所有者ID */
    @TableField("OWNER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ownerId;

    /** 所有者姓名 */
    @TableField("OWNER_NAME")
    private String ownerName;

    /** 存放地点 */
    @TableField("LOCATION")
    private String location;

    /** 关联合同ID */
    @TableField("CONTRACT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long contractId;

    /** 公司ID */
    @TableField("COMPANY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long companyId;

    /** 公司名称 */
    @TableField("COMPANY_NAME")
    private String companyName;

    /** 删除标志 */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 创建人ID */
    @TableField("CREATED_BY")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createdBy;

    /** 创建人姓名 */
    @TableField("CREATED_BY_NAME")
    private String createdByName;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新人ID */
    @TableField("UPDATED_BY")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updatedBy;

    /** 更新人姓名 */
    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /** 备注 */
    @TableField("REMARK")
    private String remark;

    // ============ Getter/Setter 方法 ============
    public Long getCollateralId() { return collateralId; }
    public void setCollateralId(Long collateralId) { this.collateralId = collateralId; }
    public String getCollateralNo() { return collateralNo; }
    public void setCollateralNo(String collateralNo) { this.collateralNo = collateralNo; }
    public String getCollateralType() { return collateralType; }
    public void setCollateralType(String collateralType) { this.collateralType = collateralType; }
    public String getCollateralName() { return collateralName; }
    public void setCollateralName(String collateralName) { this.collateralName = collateralName; }
    public BigDecimal getCollateralValue() { return collateralValue; }
    public void setCollateralValue(BigDecimal collateralValue) { this.collateralValue = collateralValue; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getEvaluationValue() { return evaluationValue; }
    public void setEvaluationValue(BigDecimal evaluationValue) { this.evaluationValue = evaluationValue; }
    public Date getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(Date evaluationDate) { this.evaluationDate = evaluationDate; }
    public String getEvaluationAgency() { return evaluationAgency; }
    public void setEvaluationAgency(String evaluationAgency) { this.evaluationAgency = evaluationAgency; }
    public String getCollateralStatus() { return collateralStatus; }
    public void setCollateralStatus(String collateralStatus) { this.collateralStatus = collateralStatus; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
