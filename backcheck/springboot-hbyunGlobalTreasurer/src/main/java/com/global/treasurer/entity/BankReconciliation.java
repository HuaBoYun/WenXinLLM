package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行对账主表实体类
 * 对应表：TBL_BANK_RECONCILIATION
 *
 * @author AI Developer
 * @date 2025-01-15
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BANK_RECONCILIATION")
public class BankReconciliation {
    @TableId(type = IdType.INPUT)
    @TableField("RECONCILIATION_ID")
    private Long reconciliationId;

    @TableField("RECONCILIATION_PERIOD")
    private String reconciliationPeriod;

    @TableField("ACCOUNT_ID")
    private Long accountId;

    @TableField("BANK_NAME")
    private String bankName;

    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    @TableField("BOOK_BALANCE")
    private BigDecimal bookBalance;

    @TableField("BANK_BALANCE")
    private BigDecimal bankBalance;

    @TableField("DIFFERENCE_AMOUNT")
    private BigDecimal differenceAmount;

    @TableField("MATCH_STATUS")
    private String matchStatus;

    @TableField("RECONCILIATION_STATUS")
    private String reconciliationStatus;

    @TableField("RECONCILIATION_DATE")
    private Date reconciliationDate;

    @TableField("OPERATOR_ID")
    private Long operatorId;

    @TableField("OPERATOR_NAME")
    private String operatorName;

    @TableField("REMARK")
    private String remark;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_BY")
    private Long createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private Long updatedBy;

    @TableField("UPDATED_TIME")
    private Date updatedTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getReconciliationId() { return reconciliationId; }
    public void setReconciliationId(Long reconciliationId) { this.reconciliationId = reconciliationId; }
    public String getReconciliationPeriod() { return reconciliationPeriod; }
    public void setReconciliationPeriod(String reconciliationPeriod) { this.reconciliationPeriod = reconciliationPeriod; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getBookBalance() { return bookBalance; }
    public void setBookBalance(BigDecimal bookBalance) { this.bookBalance = bookBalance; }
    public BigDecimal getBankBalance() { return bankBalance; }
    public void setBankBalance(BigDecimal bankBalance) { this.bankBalance = bankBalance; }
    public BigDecimal getDifferenceAmount() { return differenceAmount; }
    public void setDifferenceAmount(BigDecimal differenceAmount) { this.differenceAmount = differenceAmount; }
    public String getMatchStatus() { return matchStatus; }
    public void setMatchStatus(String matchStatus) { this.matchStatus = matchStatus; }
    public String getReconciliationStatus() { return reconciliationStatus; }
    public void setReconciliationStatus(String reconciliationStatus) { this.reconciliationStatus = reconciliationStatus; }
    public Date getReconciliationDate() { return reconciliationDate; }
    public void setReconciliationDate(Date reconciliationDate) { this.reconciliationDate = reconciliationDate; }
    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }

}
