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
 * 全球司库-账户信息实体类
 *
 * @author AI Developer
 * @since 2026-01-15
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("TBL_GT_ACCOUNT_INFO")
public class TblGtAccountInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    @TableId(value = "ACCOUNT_ID", type = IdType.ASSIGN_ID)
    private Long accountId;

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
     * 账户英文名称
     */
    @TableField("ACCOUNT_NAME_ENG")
    private String accountNameEng;

    /**
     * 账户类型: BASIC-基本账户, GENERAL-一般账户, SPECIAL-专用账户, TEMPORARY-临时账户
     */
    @TableField("ACCOUNT_TYPE")
    private String accountType;

    /**
     * 银行编码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 银行名称
     */
    @TableField("BANK_NAME")
    private String bankName;

    /**
     * 分行编码
     */
    @TableField("BRANCH_CODE")
    private String branchCode;

    /**
     * 分行名称
     */
    @TableField("BRANCH_NAME")
    private String branchName;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 账户余额
     */
    @TableField("BALANCE")
    private BigDecimal balance;

    /**
     * 可用余额
     */
    @TableField("AVAILABLE_BALANCE")
    private BigDecimal availableBalance;

    /**
     * 冻结余额
     */
    @TableField("FROZEN_BALANCE")
    private BigDecimal frozenBalance;

    /**
     * 账户状态: ACTIVE-活跃, INACTIVE-非活跃, FROZEN-冻结, CLOSED-关闭
     */
    @TableField("ACCOUNT_STATUS")
    private String accountStatus;

    /**
     * 开户日期
     */
    @TableField("OPEN_DATE")
    private LocalDate openDate;

    /**
     * 销户日期
     */
    @TableField("CLOSE_DATE")
    private LocalDate closeDate;

    /**
     * 是否默认账户: 1-是, 0-否
     */
    @TableField("IS_DEFAULT")
    private Integer isDefault;

    /**
     * 是否直联: 1-是, 0-否
     */
    @TableField("IS_DIRECT_CONNECT")
    private Integer isDirectConnect;

    /**
     * 直联类型: API-接口直联, FILE-文件直联
     */
    @TableField("DIRECT_CONNECT_TYPE")
    private String directConnectType;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 机构ID
     */
    @TableField("ORG_ID")
    private BigDecimal orgId;

    /**
     * 创建人ID
     */
    @TableField("CREATE_USER")
    private BigDecimal createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATE_USER")
    private BigDecimal updateUser;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 删除标志: 1-已删除, 0-未删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountNameEng() { return accountNameEng; }
    public void setAccountNameEng(String accountNameEng) { this.accountNameEng = accountNameEng; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }
    public BigDecimal getFrozenBalance() { return frozenBalance; }
    public void setFrozenBalance(BigDecimal frozenBalance) { this.frozenBalance = frozenBalance; }
    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    public LocalDate getOpenDate() { return openDate; }
    public void setOpenDate(LocalDate openDate) { this.openDate = openDate; }
    public LocalDate getCloseDate() { return closeDate; }
    public void setCloseDate(LocalDate closeDate) { this.closeDate = closeDate; }
    public Integer getIsDefault() { return isDefault; }
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }
    public Integer getIsDirectConnect() { return isDirectConnect; }
    public void setIsDirectConnect(Integer isDirectConnect) { this.isDirectConnect = isDirectConnect; }
    public String getDirectConnectType() { return directConnectType; }
    public void setDirectConnectType(String directConnectType) { this.directConnectType = directConnectType; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public BigDecimal getOrgId() { return orgId; }
    public void setOrgId(BigDecimal orgId) { this.orgId = orgId; }
    public BigDecimal getCreateUser() { return createUser; }
    public void setCreateUser(BigDecimal createUser) { this.createUser = createUser; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public BigDecimal getUpdateUser() { return updateUser; }
    public void setUpdateUser(BigDecimal updateUser) { this.updateUser = updateUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
}
