package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据贴现实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_DISCOUNT")
public class BillDiscount implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 贴现ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long discountId;

    /** 贴现编号 */
    private String discountNo;

    /** 票据类型 */
    private String billType;

    /** 票据号码 */
    private String billNo;

    /** 票据金额 */
    private BigDecimal billAmount;

    /** 币种 */
    private String currencyCode;

    /** 贴现率 */
    private BigDecimal discountRate;

    /** 贴现金额 */
    private BigDecimal discountAmount;

    /** 贴现期限 */
    private Integer discountPeriod;

    /** 票据出票日期 */
    private Date billIssueDate;

    /** 票据到期日期 */
    private Date billMaturityDate;

    /** 贴现日期 */
    private Date discountDate;

    /** 承兑人名称 */
    private String acceptorName;

    /** 出票人名称 */
    private String drawerName;

    /** 收款人名称 */
    private String payeeName;

    /** 金融机构ID */
    private Long financialInstitutionId;

    /** 金融机构名称 */
    private String financialInstitutionName;

    /** 贴现状态 */
    private String discountStatus;

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


    public Long getDiscountId() { return discountId; }
    public void setDiscountId(Long discountId) { this.discountId = discountId; }
    public String getDiscountNo() { return discountNo; }
    public void setDiscountNo(String discountNo) { this.discountNo = discountNo; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public Integer getDiscountPeriod() { return discountPeriod; }
    public void setDiscountPeriod(Integer discountPeriod) { this.discountPeriod = discountPeriod; }
    public Date getBillIssueDate() { return billIssueDate; }
    public void setBillIssueDate(Date billIssueDate) { this.billIssueDate = billIssueDate; }
    public Date getBillMaturityDate() { return billMaturityDate; }
    public void setBillMaturityDate(Date billMaturityDate) { this.billMaturityDate = billMaturityDate; }
    public Date getDiscountDate() { return discountDate; }
    public void setDiscountDate(Date discountDate) { this.discountDate = discountDate; }
    public String getAcceptorName() { return acceptorName; }
    public void setAcceptorName(String acceptorName) { this.acceptorName = acceptorName; }
    public String getDrawerName() { return drawerName; }
    public void setDrawerName(String drawerName) { this.drawerName = drawerName; }
    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }
    public Long getFinancialInstitutionId() { return financialInstitutionId; }
    public void setFinancialInstitutionId(Long financialInstitutionId) { this.financialInstitutionId = financialInstitutionId; }
    public String getFinancialInstitutionName() { return financialInstitutionName; }
    public void setFinancialInstitutionName(String financialInstitutionName) { this.financialInstitutionName = financialInstitutionName; }
    public String getDiscountStatus() { return discountStatus; }
    public void setDiscountStatus(String discountStatus) { this.discountStatus = discountStatus; }
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
