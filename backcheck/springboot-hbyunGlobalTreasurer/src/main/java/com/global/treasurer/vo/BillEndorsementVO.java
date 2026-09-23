package com.global.treasurer.vo;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据背书VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillEndorsementVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long endorsementId;
    private String endorsementNumber;
    private String billNumber;
    private String endorsementType;
    private String endorsementTypeName;
    private BigDecimal billAmount;
    private String endorserName;
    private String endorserAccount;
    private String endorseeName;
    private String endorseeAccount;
    private String endorseeBank;
    private String contactPhone;
    private Date applicationDate;
    private Date expectedExecutionDate;
    private Date actualExecutionDate;
    private String endorsementStatus;
    private String endorsementStatusName;
    private Long applicantId;
    private String applicantName;
    private Long approverId;
    private String approverName;
    private Date approvalDate;
    private String approvalComment;
    private String endorsementPurpose;
    private Integer isContinuous;
    private String riskLevel;
    private String riskLevelName;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
    private Long companyId;
    private String companyName;
    private Long deptId;
    private String deptName;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getEndorsementId() { return endorsementId; }
    public void setEndorsementId(Long endorsementId) { this.endorsementId = endorsementId; }
    public String getEndorsementNumber() { return endorsementNumber; }
    public void setEndorsementNumber(String endorsementNumber) { this.endorsementNumber = endorsementNumber; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getEndorsementType() { return endorsementType; }
    public void setEndorsementType(String endorsementType) { this.endorsementType = endorsementType; }
    public String getEndorsementTypeName() { return endorsementTypeName; }
    public void setEndorsementTypeName(String endorsementTypeName) { this.endorsementTypeName = endorsementTypeName; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public String getEndorserName() { return endorserName; }
    public void setEndorserName(String endorserName) { this.endorserName = endorserName; }
    public String getEndorserAccount() { return endorserAccount; }
    public void setEndorserAccount(String endorserAccount) { this.endorserAccount = endorserAccount; }
    public String getEndorseeName() { return endorseeName; }
    public void setEndorseeName(String endorseeName) { this.endorseeName = endorseeName; }
    public String getEndorseeAccount() { return endorseeAccount; }
    public void setEndorseeAccount(String endorseeAccount) { this.endorseeAccount = endorseeAccount; }
    public String getEndorseeBank() { return endorseeBank; }
    public void setEndorseeBank(String endorseeBank) { this.endorseeBank = endorseeBank; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getExpectedExecutionDate() { return expectedExecutionDate; }
    public void setExpectedExecutionDate(Date expectedExecutionDate) { this.expectedExecutionDate = expectedExecutionDate; }
    public Date getActualExecutionDate() { return actualExecutionDate; }
    public void setActualExecutionDate(Date actualExecutionDate) { this.actualExecutionDate = actualExecutionDate; }
    public String getEndorsementStatus() { return endorsementStatus; }
    public void setEndorsementStatus(String endorsementStatus) { this.endorsementStatus = endorsementStatus; }
    public String getEndorsementStatusName() { return endorsementStatusName; }
    public void setEndorsementStatusName(String endorsementStatusName) { this.endorsementStatusName = endorsementStatusName; }
    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public Long getApproverId() { return approverId; }
    public void setApproverId(Long approverId) { this.approverId = approverId; }
    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
    public String getApprovalComment() { return approvalComment; }
    public void setApprovalComment(String approvalComment) { this.approvalComment = approvalComment; }
    public String getEndorsementPurpose() { return endorsementPurpose; }
    public void setEndorsementPurpose(String endorsementPurpose) { this.endorsementPurpose = endorsementPurpose; }
    public Integer getIsContinuous() { return isContinuous; }
    public void setIsContinuous(Integer isContinuous) { this.isContinuous = isContinuous; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRiskLevelName() { return riskLevelName; }
    public void setRiskLevelName(String riskLevelName) { this.riskLevelName = riskLevelName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
}
