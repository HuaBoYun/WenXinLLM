package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 财资公共模块 - 虚拟账户表
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcVirtualAccount", description = "虚拟账户管理")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TC_VIRTUAL_ACCOUNT")
public class TcVirtualAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 账户编码
     */
    @TableField("ACCOUNT_CODE")
    @ApiModelProperty(value = "账户编码", required = true)
    private String accountCode;

    /**
     * 账户名称
     */
    @TableField("ACCOUNT_NAME")
    @ApiModelProperty(value = "账户名称", required = true)
    private String accountName;

    /**
     * 账户类型
     */
    @TableField("ACCOUNT_TYPE")
    @ApiModelProperty(value = "账户类型", required = true)
    private String accountType;

    /**
     * 平台名称
     */
    @TableField("PLATFORM_NAME")
    @ApiModelProperty(value = "平台名称")
    private String platformName;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NO")
    @ApiModelProperty(value = "账户号码")
    private String accountNo;

    /**
     * 账户持有人
     */
    @TableField("ACCOUNT_HOLDER")
    @ApiModelProperty(value = "账户持有人")
    private String accountHolder;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODE")
    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    /**
     * 余额
     */
    @TableField("BALANCE")
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    /**
     * 可用余额
     */
    @TableField("AVAILABLE_BALANCE")
    @ApiModelProperty(value = "可用余额")
    private BigDecimal availableBalance;

    /**
     * 冻结余额
     */
    @TableField("FROZEN_BALANCE")
    @ApiModelProperty(value = "冻结余额")
    private BigDecimal frozenBalance;

    /**
     * 最后同步时间
     */
    @TableField("LAST_SYNC_TIME")
    @ApiModelProperty(value = "最后同步时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastSyncTime;

    /**
     * 同步状态
     */
    @TableField("SYNC_STATUS")
    @ApiModelProperty(value = "同步状态")
    private String syncStatus;

    /**
     * 同步频率
     */
    @TableField("SYNC_FREQUENCY")
    @ApiModelProperty(value = "同步频率")
    private String syncFrequency;

    /**
     * 关联银行账户
     */
    @TableField("RELATED_BANK_ACCOUNT")
    @ApiModelProperty(value = "关联银行账户")
    private String relatedBankAccount;

    /**
     * API配置
     */
    @TableField("API_CONFIG")
    @ApiModelProperty(value = "API配置")
    private String apiConfig;

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
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcVirtualAccount ofId(String id) {
        TcVirtualAccount account = new TcVirtualAccount();
        account.id = id;
        return account;
    }

    /**
     * 根据账户编码创建实例
     */
    public static TcVirtualAccount ofAccountCode(String accountCode) {
        TcVirtualAccount account = new TcVirtualAccount();
        account.accountCode = accountCode;
        return account;
    }

    /**
     * 根据账户类型创建实例
     */
    public static TcVirtualAccount ofAccountType(String accountType) {
        TcVirtualAccount account = new TcVirtualAccount();
        account.accountType = accountType;
        return account;
    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getAvailableBalance() {
        return this.availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getFrozenBalance() {
        return this.frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public String getCurrency() {
        return this.currencyCode;
    }

    public void setCurrency(String currency) {
        this.currencyCode = currency;
    }

    public String getCreateBy() {
        return this.createUser;
    }

    public void setCreateBy(String createBy) {
        this.createUser = createBy;
    }

    public String getUpdateBy() {
        return this.updateUser;
    }

    public void setUpdateBy(String updateBy) {
        this.updateUser = updateBy;
    }

    public Integer getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getSyncStatus() {
        return this.syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
}
