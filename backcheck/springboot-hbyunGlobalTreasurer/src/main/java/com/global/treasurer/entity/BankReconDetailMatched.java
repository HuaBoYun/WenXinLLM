package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账已匹配记录实体类
 * 对应表：TBL_BANK_RECON_DETAIL_MATCHED
 *
 * @author AI Developer
 * @date 2025-01-15
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BANK_RECON_DETAIL_MATCHED")
public class BankReconDetailMatched {
    @TableId(type = IdType.INPUT)
    @TableField("DETAIL_ID")
    private Long detailId;

    @TableField("RECONCILIATION_ID")
    private Long reconciliationId;

    @TableField("TRANSACTION_DATE")
    private Date transactionDate;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("BOOK_AMOUNT")
    private BigDecimal bookAmount;

    @TableField("BANK_AMOUNT")
    private BigDecimal bankAmount;

    @TableField("MATCH_TYPE")
    private String matchType;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_TIME")
    private Date createdTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }
    public Long getReconciliationId() { return reconciliationId; }
    public void setReconciliationId(Long reconciliationId) { this.reconciliationId = reconciliationId; }
    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getBookAmount() { return bookAmount; }
    public void setBookAmount(BigDecimal bookAmount) { this.bookAmount = bookAmount; }
    public BigDecimal getBankAmount() { return bankAmount; }
    public void setBankAmount(BigDecimal bankAmount) { this.bankAmount = bankAmount; }
    public String getMatchType() { return matchType; }
    public void setMatchType(String matchType) { this.matchType = matchType; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }

}
