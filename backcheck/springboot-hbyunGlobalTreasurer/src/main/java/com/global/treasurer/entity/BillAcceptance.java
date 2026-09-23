package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行承兑实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_ACCEPTANCE")
public class BillAcceptance implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 承兑ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long acceptanceId;

    /** 承兑编号 */
    private String acceptanceNo;

    /** 票据类型 */
    private String billType;

    /** 票据号码 */
    private String billNo;

    /** 票据金额 */
    private BigDecimal billAmount;

    /** 币种 */
    private String currencyCode;

    /** 承兑期限 */
    private Integer acceptancePeriod;

    /** 期限单位 */
    private String periodUnit;

    /** 出票日期 */
    private Date issueDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 承兑费率 */
    private BigDecimal acceptanceRate;

    /** 承兑费用 */
    private BigDecimal acceptanceFee;

    /** 出票人名称 */
    private String drawerName;

    /** 收款人名称 */
    private String payeeName;

    /** 承兑人名称 */
    private String acceptorName;

    /** 金融机构ID */
    private Long financialInstitutionId;

    /** 金融机构名称 */
    private String financialInstitutionName;

    /** 承兑状态 */
    private String acceptanceStatus;

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


    public Long getAcceptanceId() { return acceptanceId; }
    public void setAcceptanceId(Long acceptanceId) { this.acceptanceId = acceptanceId; }
    public String getAcceptanceNo() { return acceptanceNo; }
    public void setAcceptanceNo(String acceptanceNo) { this.acceptanceNo = acceptanceNo; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getAcceptancePeriod() { return acceptancePeriod; }
    public void setAcceptancePeriod(Integer acceptancePeriod) { this.acceptancePeriod = acceptancePeriod; }
    public String getPeriodUnit() { return periodUnit; }
    public void setPeriodUnit(String periodUnit) { this.periodUnit = periodUnit; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public BigDecimal getAcceptanceRate() { return acceptanceRate; }
    public void setAcceptanceRate(BigDecimal acceptanceRate) { this.acceptanceRate = acceptanceRate; }
    public BigDecimal getAcceptanceFee() { return acceptanceFee; }
    public void setAcceptanceFee(BigDecimal acceptanceFee) { this.acceptanceFee = acceptanceFee; }
    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }
    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public Long getFinancialInstitutionId() { return financialInstitutionId; }
    public void setFinancialInstitutionId(Long financialInstitutionId) { this.financialInstitutionId = financialInstitutionId; }
    public String getFinancialInstitutionName() { return financialInstitutionName; }
    public void setFinancialInstitutionName(String financialInstitutionName) { this.financialInstitutionName = financialInstitutionName; }
    public String getAcceptanceStatus() { return acceptanceStatus; }
    public void setAcceptanceStatus(String acceptanceStatus) { this.acceptanceStatus = acceptanceStatus; }
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
