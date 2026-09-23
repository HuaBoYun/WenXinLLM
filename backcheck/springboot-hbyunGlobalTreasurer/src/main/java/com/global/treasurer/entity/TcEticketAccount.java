package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 财资公共模块 - 电票账户设置表
 *
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcEticketAccount", description = "电票账户设置管理")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除,使用手动编写的getter/setter
// @NoArgsConstructor // 已移除,使用手动编写的getter/setter
@TableName("TC_ETICKET_ACCOUNT")
public class TcEticketAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 银行账户ID
     */
    @TableField("BANK_ACCOUNT_ID")
    @ApiModelProperty(value = "银行账户ID")
    private String bankAccountId;

    /**
     * 银行代码
     */
    @TableField("BANK_CODE")
    @ApiModelProperty(value = "银行代码")
    private String bankCode;

    /**
     * 银行名称
     */
    @TableField("BANK_NAME")
    @ApiModelProperty(value = "银行名称")
    private String bankName;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NO")
    @ApiModelProperty(value = "账户号码")
    private String accountNo;

    /**
     * 客户号
     */
    @TableField("CUSTOMER_NO")
    @ApiModelProperty(value = "客户号")
    private String customerNo;

    /**
     * 电票状态
     */
    @TableField("ETICKET_STATUS")
    @ApiModelProperty(value = "电票状态")
    private String eticketStatus;

    /**
     * 业务范围
     */
    @TableField("BUSINESS_SCOPE")
    @ApiModelProperty(value = "业务范围")
    private String businessScope;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODES")
    @ApiModelProperty(value = "币种代码")
    private String currencyCodes;

    /**
     * 日限额
     */
    @TableField("DAILY_LIMIT")
    @ApiModelProperty(value = "日限额")
    private BigDecimal dailyLimit;

    /**
     * 月限额
     */
    @TableField("MONTHLY_LIMIT")
    @ApiModelProperty(value = "月限额")
    private BigDecimal monthlyLimit;

    /**
     * 接口地址
     */
    @TableField("INTERFACE_URL")
    @ApiModelProperty(value = "接口地址")
    private String interfaceUrl;

    /**
     * 接口端口
     */
    @TableField("INTERFACE_PORT")
    @ApiModelProperty(value = "接口端口")
    private Integer interfacePort;

    /**
     * 证书路径
     */
    @TableField("CERT_PATH")
    @ApiModelProperty(value = "证书路径")
    private String certPath;

    /**
     * 证书密码
     */
    @TableField("CERT_PASSWORD")
    @ApiModelProperty(value = "证书密码")
    private String certPassword;

    /**
     * 操作员信息
     */
    @TableField("OPERATOR_INFO")
    @ApiModelProperty(value = "操作员信息")
    private String operatorInfo;

    /**
     * 超时设置
     */
    @TableField("TIMEOUT_SETTING")
    @ApiModelProperty(value = "超时设置")
    private Integer timeoutSetting;

    /**
     * 重试次数
     */
    @TableField("RETRY_COUNT")
    @ApiModelProperty(value = "重试次数")
    private Integer retryCount;

    /**
     * 最后连接时间
     */
    @TableField("LAST_CONNECT_TIME")
    @ApiModelProperty(value = "最后连接时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastConnectTime;

    /**
     * 连接状态
     */
    @TableField("CONNECTION_STATUS")
    @ApiModelProperty(value = "连接状态")
    private String connectionStatus;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    /**
     * 更新人（别名）
     */
    @ApiModelProperty(value = "更新人（别名）", hidden = true)
    @TableField(exist = false)
    private String updateBy;

    /**
     * 账户名称（别名）
     */
    @ApiModelProperty(value = "账户名称（别名）")
    @TableField(exist = false)
    private String accountName;

    /**
     * 账户号（别名）
     */
    @ApiModelProperty(value = "账户号（别名）")
    @TableField(exist = false)
    private String accountNumber;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcEticketAccount ofId(String id) {
        TcEticketAccount account = new TcEticketAccount();
        account.id = id;
        return account;
    }

    /**
     * 根据银行代码创建实例
     */
    public static TcEticketAccount ofBankCode(String bankCode) {
        TcEticketAccount account = new TcEticketAccount();
        account.bankCode = bankCode;
        return account;
    }

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(String bankAccountId) { this.bankAccountId = bankAccountId; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    public String getCustomerNo() { return customerNo; }
    public void setCustomerNo(String customerNo) { this.customerNo = customerNo; }
    public String getEticketStatus() { return eticketStatus; }
    public void setEticketStatus(String eticketStatus) { this.eticketStatus = eticketStatus; }
    public String getBusinessScope() { return businessScope; }
    public void setBusinessScope(String businessScope) { this.businessScope = businessScope; }
    public String getCurrencyCodes() { return currencyCodes; }
    public void setCurrencyCodes(String currencyCodes) { this.currencyCodes = currencyCodes; }
    public BigDecimal getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(BigDecimal dailyLimit) { this.dailyLimit = dailyLimit; }
    public BigDecimal getMonthlyLimit() { return monthlyLimit; }
    public void setMonthlyLimit(BigDecimal monthlyLimit) { this.monthlyLimit = monthlyLimit; }
    public String getInterfaceUrl() { return interfaceUrl; }
    public void setInterfaceUrl(String interfaceUrl) { this.interfaceUrl = interfaceUrl; }
    public Integer getInterfacePort() { return interfacePort; }
    public void setInterfacePort(Integer interfacePort) { this.interfacePort = interfacePort; }
    public String getCertPath() { return certPath; }
    public void setCertPath(String certPath) { this.certPath = certPath; }
    public String getCertPassword() { return certPassword; }
    public void setCertPassword(String certPassword) { this.certPassword = certPassword; }
    public String getOperatorInfo() { return operatorInfo; }
    public void setOperatorInfo(String operatorInfo) { this.operatorInfo = operatorInfo; }
    public Integer getTimeoutSetting() { return timeoutSetting; }
    public void setTimeoutSetting(Integer timeoutSetting) { this.timeoutSetting = timeoutSetting; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Date getLastConnectTime() { return lastConnectTime; }
    public void setLastConnectTime(Date lastConnectTime) { this.lastConnectTime = lastConnectTime; }
    public String getConnectionStatus() { return connectionStatus; }
    public void setConnectionStatus(String connectionStatus) { this.connectionStatus = connectionStatus; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }
}
