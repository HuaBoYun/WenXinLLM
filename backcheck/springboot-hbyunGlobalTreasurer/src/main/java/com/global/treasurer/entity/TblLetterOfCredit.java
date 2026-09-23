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
 * 信用证实体类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_LETTER_OF_CREDIT")
public class TblLetterOfCredit implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 信用证ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long lcId;

    /** 信用证编号 */
    private String lcNumber;

    /** 信用证类型 */
    private String lcType;

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

    /** 提交时间（非数据库字段） */
    @TableField(exist = false)
    private Date submitTime;

    /** 审批意见（非数据库字段） */
    @TableField(exist = false)
    private String approvalComment;

    /** 审批时间（非数据库字段） */
    @TableField(exist = false)
    private Date approvalTime;

    /** 公司ID（非数据库字段） */
    @TableField(exist = false)
    private String companyId;

    /** 公司名称（非数据库字段） */
    @TableField(exist = false)
    private String companyName;

    /** 备注 */
    private String remark;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getLcId() { return lcId; }
    public void setLcId(Long lcId) { this.lcId = lcId; }
    public String getLcNumber() { return lcNumber; }
    public void setLcNumber(String lcNumber) { this.lcNumber = lcNumber; }
    public String getLcType() { return lcType; }
    public void setLcType(String lcType) { this.lcType = lcType; }
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
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

}
