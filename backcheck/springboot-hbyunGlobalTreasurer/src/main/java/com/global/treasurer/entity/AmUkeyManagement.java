package com.global.treasurer.entity;

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
 * U盾管理实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_ukey_management")
public class AmUkeyManagement implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * U盾序列号
     */
    @TableField("ukey_serial_no")
    private String ukeySerialNo;

    /**
     * U盾类型
     */
    @TableField("ukey_type")
    private String ukeyType;

    /**
     * U盾品牌
     */
    @TableField("ukey_brand")
    private String ukeyBrand;

    /**
     * 证书DN
     */
    @TableField("certificate_dn")
    private String certificateDn;

    /**
     * 证书序列号
     */
    @TableField("certificate_serial_no")
    private String certificateSerialNo;

    /**
     * 证书生效日期
     */
    @TableField("certificate_start_date")
    private LocalDate certificateStartDate;

    /**
     * 证书失效日期
     */
    @TableField("certificate_end_date")
    private LocalDate certificateEndDate;

    /**
     * 关联账户ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 使用用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * U盾状态：ACTIVE-活跃，INACTIVE-非活跃，LOCKED-锁定，EXPIRED-过期
     */
    @TableField("ukey_status")
    private String ukeyStatus;

    /**
     * 发放日期
     */
    @TableField("issue_date")
    private LocalDate issueDate;

    /**
     * 激活日期
     */
    @TableField("activation_date")
    private LocalDate activationDate;

    /**
     * 最后使用时间
     */
    @TableField("last_use_time")
    private LocalDateTime lastUseTime;

    /**
     * 使用次数
     */
    @TableField("use_count")
    private Integer useCount;

    /**
     * 错误次数
     */
    @TableField("error_count")
    private Integer errorCount;

    /**
     * 最大错误次数
     */
    @TableField("max_error_count")
    private Integer maxErrorCount;

    /**
     * 备注
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
    public String getUkeySerialNo() { return ukeySerialNo; }
    public void setUkeySerialNo(String ukeySerialNo) { this.ukeySerialNo = ukeySerialNo; }
    public String getUkeyType() { return ukeyType; }
    public void setUkeyType(String ukeyType) { this.ukeyType = ukeyType; }
    public String getUkeyBrand() { return ukeyBrand; }
    public void setUkeyBrand(String ukeyBrand) { this.ukeyBrand = ukeyBrand; }
    public String getCertificateDn() { return certificateDn; }
    public void setCertificateDn(String certificateDn) { this.certificateDn = certificateDn; }
    public String getCertificateSerialNo() { return certificateSerialNo; }
    public void setCertificateSerialNo(String certificateSerialNo) { this.certificateSerialNo = certificateSerialNo; }
    public LocalDate getCertificateStartDate() { return certificateStartDate; }
    public void setCertificateStartDate(LocalDate certificateStartDate) { this.certificateStartDate = certificateStartDate; }
    public LocalDate getCertificateEndDate() { return certificateEndDate; }
    public void setCertificateEndDate(LocalDate certificateEndDate) { this.certificateEndDate = certificateEndDate; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUkeyStatus() { return ukeyStatus; }
    public void setUkeyStatus(String ukeyStatus) { this.ukeyStatus = ukeyStatus; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDate getActivationDate() { return activationDate; }
    public void setActivationDate(LocalDate activationDate) { this.activationDate = activationDate; }
    public LocalDateTime getLastUseTime() { return lastUseTime; }
    public void setLastUseTime(LocalDateTime lastUseTime) { this.lastUseTime = lastUseTime; }
    public Integer getUseCount() { return useCount; }
    public void setUseCount(Integer useCount) { this.useCount = useCount; }
    public Integer getErrorCount() { return errorCount; }
    public void setErrorCount(Integer errorCount) { this.errorCount = errorCount; }
    public Integer getMaxErrorCount() { return maxErrorCount; }
    public void setMaxErrorCount(Integer maxErrorCount) { this.maxErrorCount = maxErrorCount; }
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
