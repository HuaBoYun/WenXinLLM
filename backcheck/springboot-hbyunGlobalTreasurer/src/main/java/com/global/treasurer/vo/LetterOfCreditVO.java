package com.global.treasurer.vo;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 信用证VO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class LetterOfCreditVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 信用证ID */
    private Long lcId;

    /** 信用证编号 */
    private String lcNumber;

    /** 信用证类型 */
    private String lcType;

    /** 信用证类型名称 */
    private String lcTypeName;

    /** 信用证金额 */
    private BigDecimal lcAmount;

    /** 币种 */
    private String currency;

    /** 开证日期 */
    private Date issueDate;

    /** 有效期 */
    private Date expiryDate;

    /** 申请人 */
    private String applicant;

    /** 受益人 */
    private String beneficiary;

    /** 开证银行 */
    private String issuingBank;

    /** 通知银行 */
    private String advisingBank;

    /** 信用证状态 */
    private String lcStatus;

    /** 信用证状态名称 */
    private String lcStatusName;

    /** 提交时间 */
    private Date submitTime;

    /** 审批意见 */
    private String approvalComment;

    /** 审批时间 */
    private Date approvalTime;

    /** 公司ID */
    private String companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getLcId() { return lcId; }
    public void setLcId(Long lcId) { this.lcId = lcId; }
    public String getLcNumber() { return lcNumber; }
    public void setLcNumber(String lcNumber) { this.lcNumber = lcNumber; }
    public String getLcType() { return lcType; }
    public void setLcType(String lcType) { this.lcType = lcType; }
    public String getLcTypeName() { return lcTypeName; }
    public void setLcTypeName(String lcTypeName) { this.lcTypeName = lcTypeName; }
    public BigDecimal getLcAmount() { return lcAmount; }
    public void setLcAmount(BigDecimal lcAmount) { this.lcAmount = lcAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getBeneficiary() { return beneficiary; }
    public void setBeneficiary(String beneficiary) { this.beneficiary = beneficiary; }
    public String getIssuingBank() { return issuingBank; }
    public void setIssuingBank(String issuingBank) { this.issuingBank = issuingBank; }
    public String getAdvisingBank() { return advisingBank; }
    public void setAdvisingBank(String advisingBank) { this.advisingBank = advisingBank; }
    public String getLcStatus() { return lcStatus; }
    public void setLcStatus(String lcStatus) { this.lcStatus = lcStatus; }
    public String getLcStatusName() { return lcStatusName; }
    public void setLcStatusName(String lcStatusName) { this.lcStatusName = lcStatusName; }
    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }
    public String getApprovalComment() { return approvalComment; }
    public void setApprovalComment(String approvalComment) { this.approvalComment = approvalComment; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

}
