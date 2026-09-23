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
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_CREDIT_LIMIT")
public class TblCreditLimit implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 额度ID */
    @TableId(value = "LIMIT_ID", type = IdType.AUTO)
    private Long limitId;

    /** 额度编号 */
    private String limitNo;

    /** 合同ID */
    private Long contractId;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 额度类型 */
    private String limitType;

    /** 总额度 */
    private BigDecimal totalLimit;

    /** 已用额度 */
    private BigDecimal usedLimit;

    /** 冻结额度 */
    private BigDecimal frozenLimit;

    /** 可用额度 */
    private BigDecimal availableLimit;

    /** 币种 */
    private String currencyCode;

    /** 起始日期 */
    private Date startDate;

    /** 到期日期 */
    private Date endDate;

    /** 额度状态(NORMAL-正常,FROZEN-冻结,SUSPENDED-暂停,CANCELLED-已取消) */
    private String limitStatus;

    /** 利率 */
    private BigDecimal interestRate;

    /** 担保方式 */
    private String guaranteeMethod;

    /** 额度用途 */
    private String purpose;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建时间 */
    private Date createdTime;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getLimitId() { return limitId; }
    public void setLimitId(Long limitId) { this.limitId = limitId; }
    public String getLimitNo() { return limitNo; }
    public void setLimitNo(String limitNo) { this.limitNo = limitNo; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getLimitType() { return limitType; }
    public void setLimitType(String limitType) { this.limitType = limitType; }
    public BigDecimal getTotalLimit() { return totalLimit; }
    public void setTotalLimit(BigDecimal totalLimit) { this.totalLimit = totalLimit; }
    public BigDecimal getUsedLimit() { return usedLimit; }
    public void setUsedLimit(BigDecimal usedLimit) { this.usedLimit = usedLimit; }
    public BigDecimal getFrozenLimit() { return frozenLimit; }
    public void setFrozenLimit(BigDecimal frozenLimit) { this.frozenLimit = frozenLimit; }
    public BigDecimal getAvailableLimit() { return availableLimit; }
    public void setAvailableLimit(BigDecimal availableLimit) { this.availableLimit = availableLimit; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getLimitStatus() { return limitStatus; }
    public void setLimitStatus(String limitStatus) { this.limitStatus = limitStatus; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getGuaranteeMethod() { return guaranteeMethod; }
    public void setGuaranteeMethod(String guaranteeMethod) { this.guaranteeMethod = guaranteeMethod; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
