package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 伙伴银行账户实体类
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除

@TableName("TC_PARTNER_BANK_ACCOUNT")
public class TcPartnerBankAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

    /**
     * 伙伴ID
     */
    @TableField("PARTNER_ID")
    private String partnerId;

    /**
     * 账户名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 银行代码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 银行名称
     */
    @TableField("BANK_NAME")
    private String bankName;

    /**
     * 银行分支机构名称
     */
    @TableField("BANK_BRANCH_NAME")
    private String bankBranchName;

    /**
     * 银行分支机构代码
     */
    @TableField("BANK_BRANCH_CODE")
    private String bankBranchCode;

    /**
     * SWIFT代码
     */
    @TableField("SWIFT_CODE")
    private String swiftCode;

    /**
     * 银行地址
     */
    @TableField("BANK_ADDRESS")
    private String bankAddress;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 账户类型
     */
    @TableField("ACCOUNT_TYPE")
    private String accountType;

    /**
     * 账户用途
     */
    @TableField("ACCOUNT_PURPOSE")
    private String accountPurpose;

    /**
     * 账户状态
     */
    @TableField("ACCOUNT_STATUS")
    private String accountStatus;

    /**
     * 收款权限(1-有,0-无)
     */
    @TableField("RECEIVE_PERMISSION")
    private String receivePermission;

    /**
     * 付款权限(1-有,0-无)
     */
    @TableField("PAY_PERMISSION")
    private String payPermission;

    /**
     * 查询权限(1-有,0-无)
     */
    @TableField("QUERY_PERMISSION")
    private String queryPermission;

    /**
     * 操作权限(1-有,0-无)
     */
    @TableField("OPERATION_PERMISSION")
    private String operationPermission;

    /**
     * 是否主账户(1-是,0-否)
     */
    @TableField("IS_PRIMARY_ACCOUNT")
    private String isPrimaryAccount;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 状态(1-启用,0-停用)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Integer versionNo;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankBranchName() { return bankBranchName; }
    public void setBankBranchName(String bankBranchName) { this.bankBranchName = bankBranchName; }
    public String getBankBranchCode() { return bankBranchCode; }
    public void setBankBranchCode(String bankBranchCode) { this.bankBranchCode = bankBranchCode; }
    public String getSwiftCode() { return swiftCode; }
    public void setSwiftCode(String swiftCode) { this.swiftCode = swiftCode; }
    public String getBankAddress() { return bankAddress; }
    public void setBankAddress(String bankAddress) { this.bankAddress = bankAddress; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getAccountPurpose() { return accountPurpose; }
    public void setAccountPurpose(String accountPurpose) { this.accountPurpose = accountPurpose; }
    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    public String getReceivePermission() { return receivePermission; }
    public void setReceivePermission(String receivePermission) { this.receivePermission = receivePermission; }
    public String getPayPermission() { return payPermission; }
    public void setPayPermission(String payPermission) { this.payPermission = payPermission; }
    public String getQueryPermission() { return queryPermission; }
    public void setQueryPermission(String queryPermission) { this.queryPermission = queryPermission; }
    public String getOperationPermission() { return operationPermission; }
    public void setOperationPermission(String operationPermission) { this.operationPermission = operationPermission; }
    public String getIsPrimaryAccount() { return isPrimaryAccount; }
    public void setIsPrimaryAccount(String isPrimaryAccount) { this.isPrimaryAccount = isPrimaryAccount; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
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
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

}
