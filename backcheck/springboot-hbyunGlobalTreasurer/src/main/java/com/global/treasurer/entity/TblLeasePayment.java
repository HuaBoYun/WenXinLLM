package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 租金计划实体类
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@TableName("TBL_LEASE_PAYMENT")
public class TblLeasePayment implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 付款ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long paymentId;

    /** 租赁ID */
    private Long leaseId;

    /** 期数 */
    private Integer period;

    /** 应付日期 */
    private Date dueDate;

    /** 本金 */
    private BigDecimal principal;

    /** 利息 */
    private BigDecimal interest;

    /** 应付金额 */
    private BigDecimal amount;

    /** 实付金额 */
    private BigDecimal paidAmount;

    /** 付款日期 */
    private Date paidDate;

    /** 付款状态(PENDING-待付,PAID-已付,OVERDUE-逾期) */
    private String status;

    /** 付款备注 */
    private String remark;

    /** 创建人ID */
    private Long createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人ID */
    private Long updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    // Getters and Setters
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Long getLeaseId() { return leaseId; }
    public void setLeaseId(Long leaseId) { this.leaseId = leaseId; }

    public Integer getPeriod() { return period; }
    public void setPeriod(Integer period) { this.period = period; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public BigDecimal getPrincipal() { return principal; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }

    public BigDecimal getInterest() { return interest; }
    public void setInterest(BigDecimal interest) { this.interest = interest; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public Date getPaidDate() { return paidDate; }
    public void setPaidDate(Date paidDate) { this.paidDate = paidDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }

    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }

    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
}

