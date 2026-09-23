package com.global.treasurer.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除,使用手动编写的getter/setter
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 全球司库-U盾信息实体类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
// @Data // 已移除,使用手动编写的getter/setter
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_GT_UKEY_INFO")
public class TblGtUKeyInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * U盾ID
     */
    @TableId(value = "UKEY_ID", type = IdType.ASSIGN_ID)
    private Long ukeyId;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private BigDecimal orgId;

    /**
     * 关联账户ID
     */
    @TableField("ACCOUNT_ID")
    private BigDecimal accountId;

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
     * U盾编号
     */
    @TableField("UKEY_NO")
    private String ukeyNo;

    /**
     * U盾类型
     */
    @TableField("UKEY_TYPE")
    private String ukeyType;

    /**
     * 银行编码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 证书路径
     */
    @TableField("CERTIFICATE_PATH")
    private String certificatePath;

    /**
     * 证书密码
     */
    @TableField("CERTIFICATE_PASSWORD")
    private String certificatePassword;

    /**
     * 持有人姓名
     */
    @TableField("HOLDER_NAME")
    private String holderName;

    /**
     * 持有人身份证号
     */
    @TableField("HOLDER_ID_CARD")
    private String holderIdCard;

    /**
     * 联系电话
     */
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    /**
     * U盾状态: ACTIVE-正常, LOCKED-锁定, LOST-挂失, EXPIRED-过期
     */
    @TableField("UKEY_STATUS")
    private String ukeyStatus;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    /**
     * 到期日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDate expiryDate;

    /**
     * 是否锁定: 1-锁定, 0-未锁定
     */
    @TableField("IS_LOCKED")
    private Integer isLocked;

    /**
     * 最后使用时间
     */
    @TableField("LAST_USED_TIME")
    private LocalDateTime lastUsedTime;

    /**
     * 设备描述
     */
    @TableField("DEVICE_DESCRIPTION")
    private String deviceDescription;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

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

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getUkeyId() {
        return ukeyId;
    }

    public void setUkeyId(Long ukeyId) {
        this.ukeyId = ukeyId;
    }

    public BigDecimal getOrgId() {
        return orgId;
    }

    public void setOrgId(BigDecimal orgId) {
        this.orgId = orgId;
    }

    public BigDecimal getAccountId() {
        return accountId;
    }

    public void setAccountId(BigDecimal accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUkeyNo() {
        return ukeyNo;
    }

    public void setUkeyNo(String ukeyNo) {
        this.ukeyNo = ukeyNo;
    }

    public String getUkeyType() {
        return ukeyType;
    }

    public void setUkeyType(String ukeyType) {
        this.ukeyType = ukeyType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getHolderIdCard() {
        return holderIdCard;
    }

    public void setHolderIdCard(String holderIdCard) {
        this.holderIdCard = holderIdCard;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getUkeyStatus() {
        return ukeyStatus;
    }

    public void setUkeyStatus(String ukeyStatus) {
        this.ukeyStatus = ukeyStatus;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public LocalDateTime getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(LocalDateTime lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getCreateUser() {
        return createUser;
    }

    public void setCreateUser(BigDecimal createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(BigDecimal updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
