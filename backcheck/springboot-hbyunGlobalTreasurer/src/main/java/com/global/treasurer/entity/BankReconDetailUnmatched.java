package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账未匹配记录实体类
 * 对应表：TBL_BANK_RECON_DETAIL_UNMATCHED
 *
 * @author AI Developer
 * @date 2025-01-15
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BANK_RECON_DETAIL_UNMATCHED")
public class BankReconDetailUnmatched {
    @TableId(type = IdType.INPUT)
    @TableField("DETAIL_ID")
    private Long detailId;

    @TableField("RECONCILIATION_ID")
    private Long reconciliationId;

    @TableField("SOURCE")
    private String source;

    @TableField("TRANSACTION_DATE")
    private Date transactionDate;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("AMOUNT")
    private BigDecimal amount;

    @TableField("MATCHED")
    private Integer matched;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_TIME")
    private Date createdTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }
    public Long getReconciliationId() { return reconciliationId; }
    public void setReconciliationId(Long reconciliationId) { this.reconciliationId = reconciliationId; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getMatched() { return matched; }
    public void setMatched(Integer matched) { this.matched = matched; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
