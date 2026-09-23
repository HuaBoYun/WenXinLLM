package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收款单实体类
 * 对应表：TBL_CASH_RECEIPT
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@Data
@TableName("TBL_CASH_RECEIPT")
public class CashReceipt {
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("RECEIPT_ID")
    private Long receiptId;

    @TableField("RECEIPT_NO")
    private String receiptNo;

    @TableField("RECEIPT_DATE")
    private Date receiptDate;

    @TableField(value = "ACCOUNT_ID", insertStrategy = FieldStrategy.IGNORED)
    private Long accountId;

    @TableField("ACCOUNT_NAME")
    private String accountName;

    @TableField("PAYER_NAME")
    private String payerName;

    @TableField("PAYER_ACCOUNT")
    private String payerAccount;

    @TableField("RECEIPT_AMOUNT")
    private BigDecimal receiptAmount;

    @TableField("CURRENCY_CODE")
    private String currencyCode;

    @TableField("RECEIPT_PURPOSE")
    private String receiptPurpose;

    @TableField("RECEIPT_STATUS")
    private String receiptStatus;

    @TableField("REMARK")
    private String remark;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_BY")
    private Long createdBy;

    @TableField("CREATED_BY_NAME")
    private String createdByName;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private Long updatedBy;

    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    @TableField("UPDATED_TIME")
    private Date updatedTime;

    // 显式添加getter和setter方法以确保编译通过
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
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
     * 用于处理前端传递的字符串类型receiptId
     */
    @JsonSetter("receiptId")
    public void setReceiptIdString(String value) {
        if (value != null && !value.isEmpty()) {
            this.receiptId = Long.parseLong(value);
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

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }
}
