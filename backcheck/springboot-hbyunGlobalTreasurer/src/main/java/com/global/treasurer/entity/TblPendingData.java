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
 * 待结算数据实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PENDING_DATA")
public class TblPendingData implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 待结算ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long pendingId;

    /** 业务编号 */
    private String businessNo;

    /** 业务类型 */
    private String businessType;

    /** 结算金额 */
    private BigDecimal settlementAmount;

    /** 币种代码 */
    private String currencyCode;

    /** 收款人账号 */
    private String counterpartyAccount;

    /** 收款人名称 */
    private String counterpartyName;

    /** 收款人银行 */
    private String counterpartyBank;

    /** 优先级(HIGH-高,MEDIUM-中,LOW-低) */
    private String priority;

    /** 预计结算日期 */
    private Date expectedSettlementDate;

    /** 实际结算日期 */
    private Date actualSettlementDate;

    /** 结算状态(PENDING-待结算,PROCESSING-处理中,COMPLETED-已完成,FAILED-失败,CANCELLED-已取消) */
    private String settlementStatus;

    /** 审批状态(DRAFT-草稿,PENDING-待审批,APPROVED-已审批,REJECTED-已拒绝) */
    private String approvalStatus;

    /** 失败原因 */
    private String failureReason;

    /** 重试次数 */
    private Integer retryCount;

    /** 业务描述 */
    private String businessDescription;

    /** 附件路径 */
    private String attachmentPath;

    /** 备注 */
    private String remark;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

    /** 创建人 */
    @TableField("CREATE_BY")
    private Long createdBy;

    /** 创建人姓名 */
    @TableField("CREATE_BY_NAME")
    private String createdByName;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField("UPDATE_BY")
    private Long updatedBy;

    /** 更新人姓名 */
    @TableField("UPDATE_BY_NAME")
    private String updatedByName;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updatedTime;

    /** 组织ID */
    private Long orgId;

    /** 组织名称 */
    private String orgName;

    /** 审批人 */
    private Long approvalBy;

    /** 审批人姓名 */
    private String approvalByName;

    /** 审批时间 */
    private Date approvalTime;

    /** 审批意见 */
    private String approvalOpinion;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getPendingId() { return pendingId; }
    public void setPendingId(Long pendingId) { this.pendingId = pendingId; }
    public String getBusinessNo() { return businessNo; }
    public void setBusinessNo(String businessNo) { this.businessNo = businessNo; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public BigDecimal getSettlementAmount() { return settlementAmount; }
    public void setSettlementAmount(BigDecimal settlementAmount) { this.settlementAmount = settlementAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getCounterpartyAccount() { return counterpartyAccount; }
    public void setCounterpartyAccount(String counterpartyAccount) { this.counterpartyAccount = counterpartyAccount; }
    public String getCounterpartyName() { return counterpartyName; }
    public void setCounterpartyName(String counterpartyName) { this.counterpartyName = counterpartyName; }
    public String getCounterpartyBank() { return counterpartyBank; }
    public void setCounterpartyBank(String counterpartyBank) { this.counterpartyBank = counterpartyBank; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public Date getExpectedSettlementDate() { return expectedSettlementDate; }
    public void setExpectedSettlementDate(Date expectedSettlementDate) { this.expectedSettlementDate = expectedSettlementDate; }
    public Date getActualSettlementDate() { return actualSettlementDate; }
    public void setActualSettlementDate(Date actualSettlementDate) { this.actualSettlementDate = actualSettlementDate; }
    public String getSettlementStatus() { return settlementStatus; }
    public void setSettlementStatus(String settlementStatus) { this.settlementStatus = settlementStatus; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public String getBusinessDescription() { return businessDescription; }
    public void setBusinessDescription(String businessDescription) { this.businessDescription = businessDescription; }
    public String getAttachmentPath() { return attachmentPath; }
    public void setAttachmentPath(String attachmentPath) { this.attachmentPath = attachmentPath; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
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
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
    public Long getApprovalBy() { return approvalBy; }
    public void setApprovalBy(Long approvalBy) { this.approvalBy = approvalBy; }
    public String getApprovalByName() { return approvalByName; }
    public void setApprovalByName(String approvalByName) { this.approvalByName = approvalByName; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }

}
