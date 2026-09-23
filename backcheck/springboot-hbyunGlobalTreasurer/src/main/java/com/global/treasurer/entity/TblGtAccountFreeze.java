package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 全球司库-账户冻结记录实体类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("TBL_GT_ACCOUNT_FREEZE")
public class TblGtAccountFreeze implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_ID)
    private Long recordId;

    /**
     * 关联账户ID
     */
    @TableField("ACCOUNT_ID")
    private BigDecimal accountId;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 账户名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 冻结类型
     */
    @TableField("FREEZE_TYPE")
    private String freezeType;

    /**
     * 冻结金额
     */
    @TableField("FREEZE_AMOUNT")
    private BigDecimal freezeAmount;

    /**
     * 已解冻金额
     */
    @TableField("UNFREEZE_AMOUNT")
    private BigDecimal unfreezeAmount;

    /**
     * 冻结状态
     */
    @TableField("FREEZE_STATUS")
    private String freezeStatus;

    /**
     * 冻结原因
     */
    @TableField("FREEZE_REASON")
    private String freezeReason;

    /**
     * 冻结日期
     */
    @TableField("FREEZE_DATE")
    private LocalDate freezeDate;

    /**
     * 解冻日期
     */
    @TableField("UNFREEZE_DATE")
    private LocalDate unfreezeDate;

    /**
     * 解冻原因
     */
    @TableField("UNFREEZE_REASON")
    private String unfreezeReason;

    /**
     * 执行机构
     */
    @TableField("EXECUTION_ORG")
    private String executionOrg;

    /**
     * 文书号
     */
    @TableField("DOCUMENT_NO")
    private String documentNo;

    /**
     * 操作人ID
     */
    @TableField("OPERATOR_ID")
    private BigDecimal operatorId;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public BigDecimal getAccountId() { return accountId; }
    public void setAccountId(BigDecimal accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getFreezeType() { return freezeType; }
    public void setFreezeType(String freezeType) { this.freezeType = freezeType; }
    public BigDecimal getFreezeAmount() { return freezeAmount; }
    public void setFreezeAmount(BigDecimal freezeAmount) { this.freezeAmount = freezeAmount; }
    public BigDecimal getUnfreezeAmount() { return unfreezeAmount; }
    public void setUnfreezeAmount(BigDecimal unfreezeAmount) { this.unfreezeAmount = unfreezeAmount; }
    public String getFreezeStatus() { return freezeStatus; }
    public void setFreezeStatus(String freezeStatus) { this.freezeStatus = freezeStatus; }
    public String getFreezeReason() { return freezeReason; }
    public void setFreezeReason(String freezeReason) { this.freezeReason = freezeReason; }
    public LocalDate getFreezeDate() { return freezeDate; }
    public void setFreezeDate(LocalDate freezeDate) { this.freezeDate = freezeDate; }
    public LocalDate getUnfreezeDate() { return unfreezeDate; }
    public void setUnfreezeDate(LocalDate unfreezeDate) { this.unfreezeDate = unfreezeDate; }
    public String getUnfreezeReason() { return unfreezeReason; }
    public void setUnfreezeReason(String unfreezeReason) { this.unfreezeReason = unfreezeReason; }
    public String getExecutionOrg() { return executionOrg; }
    public void setExecutionOrg(String executionOrg) { this.executionOrg = executionOrg; }
    public String getDocumentNo() { return documentNo; }
    public void setDocumentNo(String documentNo) { this.documentNo = documentNo; }
    public BigDecimal getOperatorId() { return operatorId; }
    public void setOperatorId(BigDecimal operatorId) { this.operatorId = operatorId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
