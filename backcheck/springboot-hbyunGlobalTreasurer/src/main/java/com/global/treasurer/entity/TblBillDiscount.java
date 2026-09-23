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
 * 票据贴现实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BILL_DISCOUNT")
public class TblBillDiscount implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "DISCOUNT_ID", type = IdType.INPUT)
    private Long discountId;

    @TableField("DISCOUNT_NUMBER")
    private String discountNumber;

    @TableField("BILL_NUMBER")
    private String billNumber;

    @TableField("BILL_AMOUNT")
    private BigDecimal billAmount;

    @TableField("DISCOUNT_RATE")
    private BigDecimal discountRate;

    @TableField("DISCOUNT_PERIOD")
    private Integer discountPeriod;

    @TableField("DISCOUNT_INTEREST")
    private BigDecimal discountInterest;

    @TableField("DISCOUNT_AMOUNT")
    private BigDecimal discountAmount;

    @TableField("DISCOUNT_BANK")
    private String discountBank;

    @TableField("APPLICATION_DATE")
    private Date applicationDate;

    @TableField("EXPECTED_DISCOUNT_DATE")
    private Date expectedDiscountDate;

    @TableField("ACTUAL_DISCOUNT_DATE")
    private Date actualDiscountDate;

    @TableField("DISCOUNT_STATUS")
    private String discountStatus;

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
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getExpectedDiscountDate() { return expectedDiscountDate; }
    public void setExpectedDiscountDate(Date expectedDiscountDate) { this.expectedDiscountDate = expectedDiscountDate; }
    public Date getActualDiscountDate() { return actualDiscountDate; }
    public void setActualDiscountDate(Date actualDiscountDate) { this.actualDiscountDate = actualDiscountDate; }
    public String getDiscountStatus() { return discountStatus; }
    public void setDiscountStatus(String discountStatus) { this.discountStatus = discountStatus; }
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
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }

}
