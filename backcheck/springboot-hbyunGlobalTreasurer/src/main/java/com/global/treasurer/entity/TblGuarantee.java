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
 * 保函实体类
 * 对应数据库表 TBL_GUARANTEE
 *
 * 数据库表实际字段：
 * - COMPANY_ID VARCHAR2(10) NOT NULL (主键之一)
 * - GUARANTEE_ID VARCHAR2(10) NOT NULL (主键之一)
 * - GUARANTEE_TARGET VARCHAR2(100)
 * - GUARANTEE_AMOUNT NUMBER
 * - GUARANTEE_DATE DATE
 * - GUARANTEE_STATUS VARCHAR2(20)
 * - GUARANTEE_TYPE VARCHAR2(20)
 * - CREATE_TIME DATE
 * - GUARANTEED_PARTY_ID VARCHAR(50)
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_GUARANTEE")
public class TblGuarantee implements Serializable {
    private static final long serialVersionUID = 1L;

    // ========== 以下为数据库中实际存在的字段 ==========

    /** 公司ID (数据库字段: COMPANY_ID) */
    private String companyId;

    /** 保函ID (数据库字段: GUARANTEE_ID) */
    @TableId(type = IdType.INPUT)
    private String guaranteeId;

    /** 保函目标/受益人 (数据库字段: GUARANTEE_TARGET) */
    private String guaranteeTarget;

    /** 保函金额 (数据库字段: GUARANTEE_AMOUNT) */
    private BigDecimal guaranteeAmount;

    /** 保函日期 (数据库字段: GUARANTEE_DATE) */
    private Date guaranteeDate;

    /** 保函状态 (数据库字段: GUARANTEE_STATUS) */
    private String guaranteeStatus;

    /** 保函类型 (数据库字段: GUARANTEE_TYPE) */
    private String guaranteeType;

    /** 创建时间 (数据库字段: CREATE_TIME) */
    private Date createTime;

    /** 被担保方ID/申请人 (数据库字段: GUARANTEED_PARTY_ID) */
    private String guaranteedPartyId;

    // ========== 以下为非数据库字段，用于前端展示 ==========

    /** 保函编号（映射到guaranteeId） */
    @TableField(exist = false)
    private String guaranteeNumber;

    /** 币种（非数据库字段） */
    @TableField(exist = false)
    private String currency;

    /** 开立日期（映射到guaranteeDate） */
    @TableField(exist = false)
    private Date issueDate;

    /** 有效期（映射到guaranteeDate） */
    @TableField(exist = false)
    private Date expiryDate;

    /** 申请人（映射到guaranteedPartyId） */
    @TableField(exist = false)
    private String applicant;

    /** 受益人（映射到guaranteeTarget） */
    @TableField(exist = false)
    private String beneficiary;

    /** 担保银行（非数据库字段） */
    @TableField(exist = false)
    private String guaranteeBank;

    /** 备注（非数据库字段） */
    @TableField(exist = false)
    private String remark;

    /** 创建人（非数据库字段） */
    @TableField(exist = false)
    private String createBy;

    /** 更新时间（非数据库字段） */
    @TableField(exist = false)
    private Date updateTime;

    /** 删除标志（非数据库字段） */
    @TableField(exist = false)
    private Integer deleteFlag;

    /** 提交时间（非数据库字段） */
    @TableField(exist = false)
    private Date submitTime;

    /** 审批意见（非数据库字段） */
    @TableField(exist = false)
    private String approvalComment;

    /** 审批时间（非数据库字段） */
    @TableField(exist = false)
    private Date approvalTime;

    /** 释放原因（非数据库字段） */
    @TableField(exist = false)
    private String releaseReason;

    /** 释放日期（非数据库字段） */
    @TableField(exist = false)
    private Date releaseDate;

    /** 取消原因（非数据库字段） */
    @TableField(exist = false)
    private String cancelReason;


    // Getter 和 Setter 方法

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getGuaranteeId() { return guaranteeId; }
    public void setGuaranteeId(String guaranteeId) { this.guaranteeId = guaranteeId; }

    public String getGuaranteeTarget() { return guaranteeTarget; }
    public void setGuaranteeTarget(String guaranteeTarget) { this.guaranteeTarget = guaranteeTarget; }

    public BigDecimal getGuaranteeAmount() { return guaranteeAmount; }
    public void setGuaranteeAmount(BigDecimal guaranteeAmount) { this.guaranteeAmount = guaranteeAmount; }

    public Date getGuaranteeDate() { return guaranteeDate; }
    public void setGuaranteeDate(Date guaranteeDate) { this.guaranteeDate = guaranteeDate; }

    public String getGuaranteeStatus() { return guaranteeStatus; }
    public void setGuaranteeStatus(String guaranteeStatus) { this.guaranteeStatus = guaranteeStatus; }

    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getGuaranteedPartyId() { return guaranteedPartyId; }
    public void setGuaranteedPartyId(String guaranteedPartyId) { this.guaranteedPartyId = guaranteedPartyId; }

    public String getGuaranteeNumber() { return guaranteeNumber; }
    public void setGuaranteeNumber(String guaranteeNumber) { this.guaranteeNumber = guaranteeNumber; }

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

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }

    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }

    public String getApprovalComment() { return approvalComment; }
    public void setApprovalComment(String approvalComment) { this.approvalComment = approvalComment; }

    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }

    public String getReleaseReason() { return releaseReason; }
    public void setReleaseReason(String releaseReason) { this.releaseReason = releaseReason; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
}
