package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算储备操作历史实体
 * 对应表: TBL_BUDGET_RESERVE_HISTORY
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("TBL_BUDGET_RESERVE_HISTORY")
public class BudgetReserveHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "HISTORY_ID", type = IdType.ASSIGN_UUID)
    private String historyId;

    /** 储备ID */
    @TableField("RESERVE_ID")
    private String reserveId;

    /** 操作类型: USE/RELEASE/TRANSFER/CREATE/UPDATE/DELETE/APPROVE/EXECUTE */
    @TableField("OPERATION_TYPE")
    private String operationType;

    /** 操作描述 */
    @TableField("OPERATION_DESC")
    private String operationDesc;

    /** 操作金额 */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /** 修改前的值 */
    @TableField("BEFORE_VALUE")
    private String beforeValue;

    /** 修改后的值 */
    @TableField("AFTER_VALUE")
    private String afterValue;

    /** 操作人 */
    @TableField("OPERATOR")
    private String operator;

    /** 操作时间 */
    @TableField("OPERATE_TIME")
    private Date operateTime;

    /** IP地址 */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    // ========== 显式 Getter/Setter ==========
    public String getHistoryId() { return historyId; }
    public void setHistoryId(String historyId) { this.historyId = historyId; }
    public String getReserveId() { return reserveId; }
    public void setReserveId(String reserveId) { this.reserveId = reserveId; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getOperationDesc() { return operationDesc; }
    public void setOperationDesc(String operationDesc) { this.operationDesc = operationDesc; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getBeforeValue() { return beforeValue; }
    public void setBeforeValue(String beforeValue) { this.beforeValue = beforeValue; }
    public String getAfterValue() { return afterValue; }
    public void setAfterValue(String afterValue) { this.afterValue = afterValue; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public Date getOperateTime() { return operateTime; }
    public void setOperateTime(Date operateTime) { this.operateTime = operateTime; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
