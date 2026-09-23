package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 期货保证金变动记录实体类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUTURES_MARGIN_RECORD")
public class TblFuturesMarginRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "RECORD_ID", type = IdType.AUTO)
    private Long recordId;

    /**
     * 交易ID
     */
    @TableField(value = "TRANSACTION_ID")
    private Long transactionId;

    /**
     * 操作类型(ADD-追加, WITHDRAW-提取, CALL-追加通知, RETURN-返还)
     */
    @TableField(value = "OPERATION_TYPE")
    private String operationType;

    /**
     * 变动金额
     */
    @TableField(value = "AMOUNT")
    private BigDecimal amount;

    /**
     * 变动前余额
     */
    @TableField(value = "BEFORE_BALANCE")
    private BigDecimal beforeBalance;

    /**
     * 变动后余额
     */
    @TableField(value = "AFTER_BALANCE")
    private BigDecimal afterBalance;

    /**
     * 变动原因
     */
    @TableField(value = "REASON")
    private String reason;

    /**
     * 操作人
     */
    @TableField(value = "OPERATOR")
    private String operator;

    /**
     * 操作时间
     */
    @TableField(value = "OPERATE_TIME")
    private Date operateTime;

    /**
     * 备注
     */
    @TableField(value = "REMARK")
    private String remark;

    /**
     * 机构ID
     */
    @TableField(value = "ORG_ID")
    private Long orgId;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getBeforeBalance() { return beforeBalance; }
    public void setBeforeBalance(BigDecimal beforeBalance) { this.beforeBalance = beforeBalance; }
    public BigDecimal getAfterBalance() { return afterBalance; }
    public void setAfterBalance(BigDecimal afterBalance) { this.afterBalance = afterBalance; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public Date getOperateTime() { return operateTime; }
    public void setOperateTime(Date operateTime) { this.operateTime = operateTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

}
