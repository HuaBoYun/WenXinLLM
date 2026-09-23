package com.global.treasurer.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 全球司库-直联授权实体类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("TBL_GT_DIRECT_CONNECT_AUTH")
public class TblGtDirectConnectAuth implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 授权ID
     */
    @TableId(value = "AUTH_ID", type = IdType.ASSIGN_ID)
    private Long authId;

    /**
     * 关联账户ID
     */
    @TableField("ACCOUNT_ID")
    private BigDecimal accountId;

    /**
     * 账户名称（非表字段，通过 JOIN TBL_GT_ACCOUNT_INFO 获取）
     */
    @TableField(exist = false)
    private String accountName;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NO")
    private String accountNo;

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
     * 直联平台
     */
    @TableField("DIRECT_PLATFORM")
    private String directPlatform;

    /**
     * 授权类型
     */
    @TableField("AUTH_TYPE")
    private String authType;

    /**
     * 授权范围
     */
    @TableField("AUTH_SCOPE")
    private String authScope;

    /**
     * 开始日期
     */
    @TableField("START_DATE")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    private LocalDate endDate;

    /**
     * 授权状态
     */
    @TableField("AUTH_STATUS")
    private String authStatus;

    /**
     * 授权人ID
     */
    @TableField("AUTHORIZER_ID")
    private BigDecimal authorizerId;

    /**
     * 授权人姓名
     */
    @TableField("AUTHORIZER_NAME")
    private String authorizerName;

    /**
     * 授权日期
     */
    @TableField("AUTH_DATE")
    private LocalDate authDate;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private BigDecimal approverId;

    /**
     * 审批日期
     */
    @TableField("APPROVAL_DATE")
    private LocalDate approvalDate;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_OPINION")
    private String approvalOpinion;

    /**
     * 附件
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 授权限额
     */
    @TableField("AUTH_LIMIT")
    private BigDecimal authLimit;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

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
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDate expiryDate;

    /**
     * 最后使用时间
     */
    @TableField("LAST_USED_TIME")
    private LocalDateTime lastUsedTime;

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

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAuthId() { return authId; }
    public void setAuthId(Long authId) { this.authId = authId; }
    public BigDecimal getAccountId() { return accountId; }
    public void setAccountId(BigDecimal accountId) { this.accountId = accountId; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getDirectPlatform() { return directPlatform; }
    public void setDirectPlatform(String directPlatform) { this.directPlatform = directPlatform; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
    public String getAuthScope() { return authScope; }
    public void setAuthScope(String authScope) { this.authScope = authScope; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getAuthStatus() { return authStatus; }
    public void setAuthStatus(String authStatus) { this.authStatus = authStatus; }
    public BigDecimal getAuthorizerId() { return authorizerId; }
    public void setAuthorizerId(BigDecimal authorizerId) { this.authorizerId = authorizerId; }
    public String getAuthorizerName() { return authorizerName; }
    public void setAuthorizerName(String authorizerName) { this.authorizerName = authorizerName; }
    public LocalDate getAuthDate() { return authDate; }
    public void setAuthDate(LocalDate authDate) { this.authDate = authDate; }
    public BigDecimal getApproverId() { return approverId; }
    public void setApproverId(BigDecimal approverId) { this.approverId = approverId; }
    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }
    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public BigDecimal getOrgId() { return orgId; }
    public void setOrgId(BigDecimal orgId) { this.orgId = orgId; }
    public BigDecimal getAuthLimit() { return authLimit; }
    public void setAuthLimit(BigDecimal authLimit) { this.authLimit = authLimit; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getCertificatePath() { return certificatePath; }
    public void setCertificatePath(String certificatePath) { this.certificatePath = certificatePath; }
    public String getCertificatePassword() { return certificatePassword; }
    public void setCertificatePassword(String certificatePassword) { this.certificatePassword = certificatePassword; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public LocalDateTime getLastUsedTime() { return lastUsedTime; }
    public void setLastUsedTime(LocalDateTime lastUsedTime) { this.lastUsedTime = lastUsedTime; }
    public BigDecimal getCreateUser() { return createUser; }
    public void setCreateUser(BigDecimal createUser) { this.createUser = createUser; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public BigDecimal getUpdateUser() { return updateUser; }
    public void setUpdateUser(BigDecimal updateUser) { this.updateUser = updateUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
