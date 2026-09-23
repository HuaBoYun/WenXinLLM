package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 授信额度实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CREDIT_LIMIT")
public class CreditLimit implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 额度ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long limitId;

    /** 合同ID */
    private Long contractId;

    /** 额度类型 */
    private String limitType;

    /** 总额度 */
    private BigDecimal totalLimit;

    /** 已用额度 */
    private BigDecimal usedLimit;

    /** 可用额度 */
    private BigDecimal availableLimit;

    /** 币种 */
    private String currencyCode;

    /** 额度状态 */
    private String limitStatus;

    /** 生效日期 */
    private Date effectiveDate;

    /** 到期日期 */
    private Date expiryDate;

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


    public Long getLimitId() { return limitId; }
    public void setLimitId(Long limitId) { this.limitId = limitId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getLimitType() { return limitType; }
    public void setLimitType(String limitType) { this.limitType = limitType; }
    public BigDecimal getTotalLimit() { return totalLimit; }
    public void setTotalLimit(BigDecimal totalLimit) { this.totalLimit = totalLimit; }
    public BigDecimal getUsedLimit() { return usedLimit; }
    public void setUsedLimit(BigDecimal usedLimit) { this.usedLimit = usedLimit; }
    public BigDecimal getAvailableLimit() { return availableLimit; }
    public void setAvailableLimit(BigDecimal availableLimit) { this.availableLimit = availableLimit; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getLimitStatus() { return limitStatus; }
    public void setLimitStatus(String limitStatus) { this.limitStatus = limitStatus; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
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
