package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行直连配置实体类
 *
 * @author AI Assistant
 * @date 2026-03-04
 */
@TableName("TC_BANK_DIRECT_CONNECTION")
public class TcBankDirectConnection implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    /**
     * 账户ID
     */
    @TableField("ACCOUNT_ID")
    private String accountId;

    /**
     * 银行代码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 连接状态
     */
    @TableField("CONNECTION_STATUS")
    private String connectionStatus;

    /**
     * 查询权限
     */
    @TableField("QUERY_PERMISSION")
    private Integer queryPermission;

    /**
     * 支付权限
     */
    @TableField("PAYMENT_PERMISSION")
    private Integer paymentPermission;

    /**
     * 收款权限
     */
    @TableField("RECEIVE_PERMISSION")
    private Integer receivePermission;

    /**
     * 对账权限
     */
    @TableField("RECONCILIATION_PERMISSION")
    private Integer reconciliationPermission;

    /**
     * 支持的业务类型
     */
    @TableField("SUPPORTED_BUSINESS_TYPES")
    private String supportedBusinessTypes;

    /**
     * 单笔交易限额
     */
    @TableField("TRANSACTION_LIMIT_SINGLE")
    private BigDecimal transactionLimitSingle;

    /**
     * 日交易限额
     */
    @TableField("TRANSACTION_LIMIT_DAILY")
    private BigDecimal transactionLimitDaily;

    /**
     * 月交易限额
     */
    @TableField("TRANSACTION_LIMIT_MONTHLY")
    private BigDecimal transactionLimitMonthly;

    /**
     * 营业时间开始
     */
    @TableField("OPERATION_TIME_START")
    private String operationTimeStart;

    /**
     * 营业时间结束
     */
    @TableField("OPERATION_TIME_END")
    private String operationTimeEnd;

    /**
     * 最后连接时间
     */
    @TableField("LAST_CONNECTION_TIME")
    private Date lastConnectionTime;

    /**
     * 连接测试结果
     */
    @TableField("CONNECTION_TEST_RESULT")
    private String connectionTestResult;

    /**
     * 服务可用性
     */
    @TableField("SERVICE_AVAILABILITY")
    private String serviceAvailability;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 状态
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建用户
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新用户
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Integer versionNo;

    // 默认无参构造函数
    public TcBankDirectConnection() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Integer getQueryPermission() {
        return queryPermission;
    }

    public void setQueryPermission(Integer queryPermission) {
        this.queryPermission = queryPermission;
    }

    public Integer getPaymentPermission() {
        return paymentPermission;
    }

    public void setPaymentPermission(Integer paymentPermission) {
        this.paymentPermission = paymentPermission;
    }

    public Integer getReceivePermission() {
        return receivePermission;
    }

    public void setReceivePermission(Integer receivePermission) {
        this.receivePermission = receivePermission;
    }

    public Integer getReconciliationPermission() {
        return reconciliationPermission;
    }

    public void setReconciliationPermission(Integer reconciliationPermission) {
        this.reconciliationPermission = reconciliationPermission;
    }

    public String getSupportedBusinessTypes() {
        return supportedBusinessTypes;
    }

    public void setSupportedBusinessTypes(String supportedBusinessTypes) {
        this.supportedBusinessTypes = supportedBusinessTypes;
    }

    public BigDecimal getTransactionLimitSingle() {
        return transactionLimitSingle;
    }

    public void setTransactionLimitSingle(BigDecimal transactionLimitSingle) {
        this.transactionLimitSingle = transactionLimitSingle;
    }

    public BigDecimal getTransactionLimitDaily() {
        return transactionLimitDaily;
    }

    public void setTransactionLimitDaily(BigDecimal transactionLimitDaily) {
        this.transactionLimitDaily = transactionLimitDaily;
    }

    public BigDecimal getTransactionLimitMonthly() {
        return transactionLimitMonthly;
    }

    public void setTransactionLimitMonthly(BigDecimal transactionLimitMonthly) {
        this.transactionLimitMonthly = transactionLimitMonthly;
    }

    public String getOperationTimeStart() {
        return operationTimeStart;
    }

    public void setOperationTimeStart(String operationTimeStart) {
        this.operationTimeStart = operationTimeStart;
    }

    public String getOperationTimeEnd() {
        return operationTimeEnd;
    }

    public void setOperationTimeEnd(String operationTimeEnd) {
        this.operationTimeEnd = operationTimeEnd;
    }

    public Date getLastConnectionTime() {
        return lastConnectionTime;
    }

    public void setLastConnectionTime(Date lastConnectionTime) {
        this.lastConnectionTime = lastConnectionTime;
    }

    public String getConnectionTestResult() {
        return connectionTestResult;
    }

    public void setConnectionTestResult(String connectionTestResult) {
        this.connectionTestResult = connectionTestResult;
    }

    public String getServiceAvailability() {
        return serviceAvailability;
    }

    public void setServiceAvailability(String serviceAvailability) {
        this.serviceAvailability = serviceAvailability;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
}
