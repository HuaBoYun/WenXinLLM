package com.global.treasurer.vo;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 保函VO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class GuaranteeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 保函ID (数据库中为VARCHAR2类型) */
    private String guaranteeId;

    /** 保函编号 */
    private String guaranteeNumber;

    /** 保函类型 */
    private String guaranteeType;

    /** 保函类型名称 */
    private String guaranteeTypeName;

    /** 保函金额 */
    private BigDecimal guaranteeAmount;

    /** 币种 */
    private String currency;

    /** 开立日期 */
    private Date issueDate;

    /** 有效期 */
    private Date expiryDate;

    /** 申请人 */
    private String applicant;

    /** 受益人 */
    private String beneficiary;

    /** 担保银行 */
    private String guaranteeBank;

    /** 开立银行 (前端字段名，与guaranteeBank相同) */
    private String issuingBank;

    /** 保函状态 */
    private String guaranteeStatus;

    /** 保函状态名称 */
    private String guaranteeStatusName;

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


    public String getGuaranteeId() { return guaranteeId; }
    public void setGuaranteeId(String guaranteeId) { this.guaranteeId = guaranteeId; }
    public String getGuaranteeNumber() { return guaranteeNumber; }
    public void setGuaranteeNumber(String guaranteeNumber) { this.guaranteeNumber = guaranteeNumber; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public String getGuaranteeTypeName() { return guaranteeTypeName; }
    public void setGuaranteeTypeName(String guaranteeTypeName) { this.guaranteeTypeName = guaranteeTypeName; }
    public BigDecimal getGuaranteeAmount() { return guaranteeAmount; }
    public void setGuaranteeAmount(BigDecimal guaranteeAmount) { this.guaranteeAmount = guaranteeAmount; }
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
    public String getGuaranteeBank() { return guaranteeBank; }
    public void setGuaranteeBank(String guaranteeBank) { this.guaranteeBank = guaranteeBank; }
    public String getIssuingBank() { return issuingBank; }
    public void setIssuingBank(String issuingBank) { this.issuingBank = issuingBank; }
    public String getGuaranteeStatus() { return guaranteeStatus; }
    public void setGuaranteeStatus(String guaranteeStatus) { this.guaranteeStatus = guaranteeStatus; }
    public String getGuaranteeStatusName() { return guaranteeStatusName; }
    public void setGuaranteeStatusName(String guaranteeStatusName) { this.guaranteeStatusName = guaranteeStatusName; }
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
