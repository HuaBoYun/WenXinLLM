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
 * 账户档案信息实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_account_archive")
public class AmAccountArchive implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账户编号
     */
    @TableField("account_no")
    private String accountNo;

    /**
     * 账户名称
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 账户英文名称
     */
    @TableField("account_english_name")
    private String accountEnglishName;

    /**
     * 银行机构ID
     */
    @TableField("bank_institution_id")
    private Long bankInstitutionId;

    /**
     * 账户类型ID
     */
    @TableField("account_type_id")
    private Long accountTypeId;

    /**
     * 币种代码
     */
    @TableField("currency_code")
    private String currencyCode;

    /**
     * 账户性质：BASIC-基本户，GENERAL-一般户，SPECIAL-专用户，TEMPORARY-临时户
     */
    @TableField("account_nature")
    private String accountNature;

    /**
     * 账户用途：OPERATING-经营，INVESTMENT-投资，FINANCING-融资，SETTLEMENT-结算
     */
    @TableField("account_purpose")
    private String accountPurpose;

    /**
     * 开户日期
     */
    @TableField("opening_date")
    private LocalDate openingDate;

    /**
     * 销户日期
     */
    @TableField("closing_date")
    private LocalDate closingDate;

    /**
     * 账户状态：NORMAL-正常，FROZEN-冻结，CLOSED-销户，SUSPENDED-暂停
     */
    @TableField("account_status")
    private String accountStatus;

    /**
     * 当前余额
     */
    @TableField("current_balance")
    private BigDecimal currentBalance;

    /**
     * 可用余额
     */
    @TableField("available_balance")
    private BigDecimal availableBalance;

    /**
     * 冻结金额
     */
    @TableField("frozen_amount")
    private BigDecimal frozenAmount;

    /**
     * 最低余额限制
     */
    @TableField("minimum_balance")
    private BigDecimal minimumBalance;

    /**
     * 透支额度
     */
    @TableField("overdraft_limit")
    private BigDecimal overdraftLimit;

    /**
     * 所属组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 责任人ID
     */
    @TableField("responsible_person_id")
    private Long responsiblePersonId;

    /**
     * 使用部门ID
     */
    @TableField("department_id")
    private Long departmentId;

    /**
     * 成本中心
     */
    @TableField("cost_center")
    private String costCenter;

    /**
     * 是否支持银企直连
     */
    @TableField("support_direct_connection")
    private String supportDirectConnection;

    /**
     * 银企直连状态：NOT_AUTHORIZED-未授权，AUTHORIZED-已授权，EXPIRED-已过期
     */
    @TableField("direct_connection_status")
    private String directConnectionStatus;

    /**
     * 直连授权到期日期
     */
    @TableField("direct_connection_expiry_date")
    private LocalDate directConnectionExpiryDate;

    /**
     * 印鉴组合ID
     */
    @TableField("seal_combination_id")
    private Long sealCombinationId;

    /**
     * 账户开户许可证核准号
     */
    @TableField("account_license_no")
    private String accountLicenseNo;

    /**
     * 开户银行联行号
     */
    @TableField("bank_clearing_code")
    private String bankClearingCode;

    /**
     * 开户银行SWIFT代码
     */
    @TableField("bank_swift_code")
    private String bankSwiftCode;

    /**
     * 账户备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：1-有效，0-无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 版本号
     */
    @TableField("version_no")
    private Long versionNo;

    /**
     * 客户端IP
     */
    @TableField("client_ip")
    private String clientIp;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountEnglishName() { return accountEnglishName; }
    public void setAccountEnglishName(String accountEnglishName) { this.accountEnglishName = accountEnglishName; }
    public Long getBankInstitutionId() { return bankInstitutionId; }
    public void setBankInstitutionId(Long bankInstitutionId) { this.bankInstitutionId = bankInstitutionId; }
    public Long getAccountTypeId() { return accountTypeId; }
    public void setAccountTypeId(Long accountTypeId) { this.accountTypeId = accountTypeId; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getAccountNature() { return accountNature; }
    public void setAccountNature(String accountNature) { this.accountNature = accountNature; }
    public String getAccountPurpose() { return accountPurpose; }
    public void setAccountPurpose(String accountPurpose) { this.accountPurpose = accountPurpose; }
    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDate closingDate) { this.closingDate = closingDate; }
    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }
    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }
    public BigDecimal getFrozenAmount() { return frozenAmount; }
    public void setFrozenAmount(BigDecimal frozenAmount) { this.frozenAmount = frozenAmount; }
    public BigDecimal getMinimumBalance() { return minimumBalance; }
    public void setMinimumBalance(BigDecimal minimumBalance) { this.minimumBalance = minimumBalance; }
    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(BigDecimal overdraftLimit) { this.overdraftLimit = overdraftLimit; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getResponsiblePersonId() { return responsiblePersonId; }
    public void setResponsiblePersonId(Long responsiblePersonId) { this.responsiblePersonId = responsiblePersonId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getCostCenter() { return costCenter; }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }
    public String getSupportDirectConnection() { return supportDirectConnection; }
    public void setSupportDirectConnection(String supportDirectConnection) { this.supportDirectConnection = supportDirectConnection; }
    public String getDirectConnectionStatus() { return directConnectionStatus; }
    public void setDirectConnectionStatus(String directConnectionStatus) { this.directConnectionStatus = directConnectionStatus; }
    public LocalDate getDirectConnectionExpiryDate() { return directConnectionExpiryDate; }
    public void setDirectConnectionExpiryDate(LocalDate directConnectionExpiryDate) { this.directConnectionExpiryDate = directConnectionExpiryDate; }
    public Long getSealCombinationId() { return sealCombinationId; }
    public void setSealCombinationId(Long sealCombinationId) { this.sealCombinationId = sealCombinationId; }
    public String getAccountLicenseNo() { return accountLicenseNo; }
    public void setAccountLicenseNo(String accountLicenseNo) { this.accountLicenseNo = accountLicenseNo; }
    public String getBankClearingCode() { return bankClearingCode; }
    public void setBankClearingCode(String bankClearingCode) { this.bankClearingCode = bankClearingCode; }
    public String getBankSwiftCode() { return bankSwiftCode; }
    public void setBankSwiftCode(String bankSwiftCode) { this.bankSwiftCode = bankSwiftCode; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
}
