package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退租申请实体类
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@TableName("TBL_LEASE_RETURN")
public class TblLeaseReturn implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 退租ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long returnId;

    /** 租赁ID */
    private Long leaseId;

    /** 退租类型(NORMAL-正常到期,EARLY-提前退租,BREACH-违约退租) */
    private String returnType;

    /** 退租日期 */
    private Date returnDate;

    /** 资产处置方式(RETURN-归还出租方,PURCHASE-承租方购买,TRANSFER-第三方转让) */
    private String assetDisposal;

    /** 资产残值 */
    private BigDecimal residualValue;

    /** 违约金 */
    private BigDecimal penaltyAmount;

    /** 结算金额 */
    private BigDecimal settlementAmount;

    /** 退租原因 */
    private String returnReason;

    /** 申请状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,REJECTED-已拒绝,COMPLETED-已完成) */
    private String status;

    /** 附件路径(多个用逗号分隔) */
    private String attachments;

    /** 审批意见 */
    private String approvalComments;

    /** 审批人ID */
    private Long approvedBy;

    /** 审批时间 */
    private Date approvedTime;

    /** 创建人ID */
    private Long createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人ID */
    private Long updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    // Getters and Setters
    public Long getReturnId() { return returnId; }
    public void setReturnId(Long returnId) { this.returnId = returnId; }

    public Long getLeaseId() { return leaseId; }
    public void setLeaseId(Long leaseId) { this.leaseId = leaseId; }

    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public String getAssetDisposal() { return assetDisposal; }
    public void setAssetDisposal(String assetDisposal) { this.assetDisposal = assetDisposal; }

    public BigDecimal getResidualValue() { return residualValue; }
    public void setResidualValue(BigDecimal residualValue) { this.residualValue = residualValue; }

    public BigDecimal getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(BigDecimal penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public BigDecimal getSettlementAmount() { return settlementAmount; }
    public void setSettlementAmount(BigDecimal settlementAmount) { this.settlementAmount = settlementAmount; }

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }

    public String getApprovalComments() { return approvalComments; }
    public void setApprovalComments(String approvalComments) { this.approvalComments = approvalComments; }

    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }

    public Date getApprovedTime() { return approvedTime; }
    public void setApprovedTime(Date approvedTime) { this.approvedTime = approvedTime; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }

    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }

    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
}

