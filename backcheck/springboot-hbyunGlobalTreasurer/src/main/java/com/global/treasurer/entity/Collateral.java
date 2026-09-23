package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 担保物实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_COLLATERAL")
public class Collateral implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 担保物ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long collateralId;

    /** 担保物编号 */
    private String collateralNo;

    /** 担保物类型 */
    private String collateralType;

    /** 担保物名称 */
    private String collateralName;

    /** 担保物价值 */
    private BigDecimal collateralValue;

    /** 币种 */
    private String currencyCode;

    /** 评估价值 */
    private BigDecimal evaluationValue;

    /** 评估日期 */
    private Date evaluationDate;

    /** 评估机构 */
    private String evaluationAgency;

    /** 担保物状态 */
    private String collateralStatus;

    /** 所有者ID */
    private Long ownerId;

    /** 所有者姓名 */
    private String ownerName;

    /** 存放地点 */
    private String location;

    /** 关联合同ID */
    private Long contractId;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


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
