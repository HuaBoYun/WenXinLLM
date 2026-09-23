package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据背书实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_ENDORSEMENT")
public class TblBillEndorsement implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ENDORSEMENT_ID", type = IdType.INPUT)
    private Long endorsementId;

    @TableField("ENDORSEMENT_NUMBER")
    private String endorsementNumber;

    @TableField("BILL_NUMBER")
    private String billNumber;

    @TableField("ENDORSEMENT_TYPE")
    private String endorsementType;

    @TableField("BILL_AMOUNT")
    private BigDecimal billAmount;

    @TableField("ENDORSER_NAME")
    private String endorserName;

    @TableField("ENDORSER_ACCOUNT")
    private String endorserAccount;

    @TableField("ENDORSEE_NAME")
    private String endorseeName;

    @TableField("ENDORSEE_ACCOUNT")
    private String endorseeAccount;

    @TableField("ENDORSEE_BANK")
    private String endorseeBank;

    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @TableField("APPLICATION_DATE")
    private Date applicationDate;

    @TableField("EXPECTED_EXECUTION_DATE")
    private Date expectedExecutionDate;

    @TableField("ACTUAL_EXECUTION_DATE")
    private Date actualExecutionDate;

    @TableField("ENDORSEMENT_STATUS")
    private String endorsementStatus;

    @TableField("APPLICANT_ID")
    private Long applicantId;

    @TableField("APPLICANT_NAME")
    private String applicantName;

    @TableField("APPROVER_ID")
    private Long approverId;

    @TableField("APPROVER_NAME")
    private String approverName;

    @TableField("APPROVAL_DATE")
    private Date approvalDate;

    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    @TableField("ENDORSEMENT_PURPOSE")
    private String endorsementPurpose;

    @TableField("IS_CONTINUOUS")
    private Integer isContinuous;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("DEPT_ID")
    private Long deptId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getEndorsementId() { return endorsementId; }
    public void setEndorsementId(Long endorsementId) { this.endorsementId = endorsementId; }
    public String getEndorsementNumber() { return endorsementNumber; }
    public void setEndorsementNumber(String endorsementNumber) { this.endorsementNumber = endorsementNumber; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getEndorsementType() { return endorsementType; }
    public void setEndorsementType(String endorsementType) { this.endorsementType = endorsementType; }
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
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
}
