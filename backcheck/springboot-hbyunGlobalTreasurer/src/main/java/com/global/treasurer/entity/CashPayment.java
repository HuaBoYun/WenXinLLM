package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款单实体类
 * 对应表：TBL_CASH_PAYMENT
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@Data
@TableName("TBL_CASH_PAYMENT")
public class CashPayment {
    /**
     * 付款单ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("PAYMENT_ID")
    private Long paymentId;

    /**
     * 付款单号
     */
    @TableField("PAYMENT_NO")
    private String paymentNo;

    /**
     * 付款日期
     */
    @TableField("PAYMENT_DATE")
    private Date paymentDate;

    /**
     * 付款账户ID
     */
    @TableField("ACCOUNT_ID")
    private Long accountId;

    /**
     * 付款账户名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 收款方名称
     */
    @TableField("PAYEE_NAME")
    private String payeeName;

    /**
     * 收款方账号
     */
    @TableField("PAYEE_ACCOUNT")
    private String payeeAccount;

    /**
     * 收款方开户行
     */
    @TableField("PAYEE_BANK")
    private String payeeBank;

    /**
     * 付款金额
     */
    @TableField("PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;

    /**
     * 币种
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 付款方式(ONLINE_TRANSFER网银转账, BANK_DIRECT银企直联, CASH现金支付, CHECK支票支付)
     */
    @TableField("PAYMENT_METHOD")
    private String paymentMethod;

    /**
     * 付款用途
     */
    @TableField("PAYMENT_PURPOSE")
    private String paymentPurpose;

    /**
     * 付款状态(PENDING待审批, APPROVING审批中, APPROVED待执行, EXECUTING执行中, COMPLETED已完成, REJECTED已拒绝, CANCELLED已取消)
     */
    @TableField("PAYMENT_STATUS")
    private String paymentStatus;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    /**
     * 删除标志(0正常 1删除)
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /**
     * 创建人ID
     */
    @TableField("CREATED_BY")
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField("CREATED_BY_NAME")
    private String createdByName;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATED_BY")
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    // 显式添加getter和setter方法以确保编译通过
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * Jackson反序列化时使用：支持字符串转Long
     * 用于处理前端传递的字符串类型paymentId
     */
    @JsonSetter("paymentId")
    public void setPaymentIdString(String value) {
        if (value != null && !value.isEmpty()) {
            this.paymentId = Long.parseLong(value);
        }
    }

    /**
     * Jackson反序列化时使用：支持字符串转Long
     * 用于处理前端传递的字符串类型accountId
     */
    @JsonSetter("accountId")
    public void setAccountIdString(String value) {
        if (value != null && !value.isEmpty()) {
            this.accountId = Long.parseLong(value);
        }
    }

    /**
     * Jackson反序列化时使用：支持字符串转Long
     * 用于处理前端传递的字符串类型orgId
     */
    @JsonSetter("orgId")
    public void setOrgIdString(String value) {
        if (value != null && !value.isEmpty()) {
            this.orgId = Long.parseLong(value);
        }
    }

    // Getter and Setter methods for Lombok compatibility
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
