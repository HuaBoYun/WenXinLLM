package com.global.treasurer.vo;

// import lombok.Data; // 已移除

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据贴现VO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillDiscountVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long discountId;
    private String discountNumber;
    private String billNumber;
    private BigDecimal billAmount;
    private BigDecimal discountRate;
    private Integer discountPeriod;
    private BigDecimal discountInterest;
    private BigDecimal discountAmount;
    private String discountBank;
    private String discountBankAccount;
    private String discountType;
    private String discountPurpose;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date applicationDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expectedDiscountDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date actualDiscountDate;

    private String discountStatus;
    private String discountStatusName;
    private Long applicantId;
    private String applicantName;
    private Long approverId;
    private String approverName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvalDate;
    private String approvalComment;
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private String createBy;
    private String updateBy;
    private Long companyId;
    private String companyName;
    private Long deptId;
    private String deptName;

    /** 到期日期（来自票据信息表） */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDate;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getDiscountId() { return discountId; }
    public void setDiscountId(Long discountId) { this.discountId = discountId; }
    public String getDiscountNumber() { return discountNumber; }
    public void setDiscountNumber(String discountNumber) { this.discountNumber = discountNumber; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public Integer getDiscountPeriod() { return discountPeriod; }
    public void setDiscountPeriod(Integer discountPeriod) { this.discountPeriod = discountPeriod; }
    public BigDecimal getDiscountInterest() { return discountInterest; }
    public void setDiscountInterest(BigDecimal discountInterest) { this.discountInterest = discountInterest; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public String getDiscountBank() { return discountBank; }
    public void setDiscountBank(String discountBank) { this.discountBank = discountBank; }
    public String getDiscountBankAccount() { return discountBankAccount; }
    public void setDiscountBankAccount(String discountBankAccount) { this.discountBankAccount = discountBankAccount; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public String getDiscountPurpose() { return discountPurpose; }
    public void setDiscountPurpose(String discountPurpose) { this.discountPurpose = discountPurpose; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getExpectedDiscountDate() { return expectedDiscountDate; }
    public void setExpectedDiscountDate(Date expectedDiscountDate) { this.expectedDiscountDate = expectedDiscountDate; }
    public Date getActualDiscountDate() { return actualDiscountDate; }
    public void setActualDiscountDate(Date actualDiscountDate) { this.actualDiscountDate = actualDiscountDate; }
    public String getDiscountStatus() { return discountStatus; }
    public void setDiscountStatus(String discountStatus) { this.discountStatus = discountStatus; }
    public String getDiscountStatusName() { return discountStatusName; }
    public void setDiscountStatusName(String discountStatusName) { this.discountStatusName = discountStatusName; }
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
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }

}
