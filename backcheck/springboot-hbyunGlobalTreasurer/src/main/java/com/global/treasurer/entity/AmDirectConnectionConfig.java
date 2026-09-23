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
 * 银企直连配置实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_direct_connection_config")
public class AmDirectConnectionConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账户ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 连接类型：EBPP，SWIFT，PROPRIETARY
     */
    @TableField("connection_type")
    private String connectionType;

    /**
     * 接口地址
     */
    @TableField("interface_url")
    private String interfaceUrl;

    /**
     * 接口版本
     */
    @TableField("interface_version")
    private String interfaceVersion;

    /**
     * 通信协议
     */
    @TableField("communication_protocol")
    private String communicationProtocol;

    /**
     * 超时时间（秒）
     */
    @TableField("timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @TableField("retry_times")
    private Integer retryTimes;

    /**
     * 证书路径
     */
    @TableField("certificate_path")
    private String certificatePath;

    /**
     * 证书密码
     */
    @TableField("certificate_password")
    private String certificatePassword;

    /**
     * 加密算法
     */
    @TableField("encryption_algorithm")
    private String encryptionAlgorithm;

    /**
     * 签名算法
     */
    @TableField("signature_algorithm")
    private String signatureAlgorithm;

    /**
     * 连接状态：ACTIVE-活跃，INACTIVE-非活跃，ERROR-错误
     */
    @TableField("connection_status")
    private String connectionStatus;

    /**
     * 最后测试时间
     */
    @TableField("last_test_time")
    private LocalDateTime lastTestTime;

    /**
     * 最后测试结果：SUCCESS-成功，FAILED-失败
     */
    @TableField("last_test_result")
    private String lastTestResult;

    /**
     * 授权开始日期
     */
    @TableField("authorization_start_date")
    private LocalDate authorizationStartDate;

    /**
     * 授权结束日期
     */
    @TableField("authorization_end_date")
    private LocalDate authorizationEndDate;

    /**
     * 授权操作（JSON数组）
     */
    @TableField("authorized_operations")
    private String authorizedOperations;

    /**
     * 日限额
     */
    @TableField("daily_limit")
    private BigDecimal dailyLimit;

    /**
     * 单笔限额
     */
    @TableField("single_limit")
    private BigDecimal singleLimit;

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
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getConnectionType() { return connectionType; }
    public void setConnectionType(String connectionType) { this.connectionType = connectionType; }
    public String getInterfaceUrl() { return interfaceUrl; }
    public void setInterfaceUrl(String interfaceUrl) { this.interfaceUrl = interfaceUrl; }
    public String getInterfaceVersion() { return interfaceVersion; }
    public void setInterfaceVersion(String interfaceVersion) { this.interfaceVersion = interfaceVersion; }
    public String getCommunicationProtocol() { return communicationProtocol; }
    public void setCommunicationProtocol(String communicationProtocol) { this.communicationProtocol = communicationProtocol; }
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public Integer getRetryTimes() { return retryTimes; }
    public void setRetryTimes(Integer retryTimes) { this.retryTimes = retryTimes; }
    public String getCertificatePath() { return certificatePath; }
    public void setCertificatePath(String certificatePath) { this.certificatePath = certificatePath; }
    public String getCertificatePassword() { return certificatePassword; }
    public void setCertificatePassword(String certificatePassword) { this.certificatePassword = certificatePassword; }
    public String getEncryptionAlgorithm() { return encryptionAlgorithm; }
    public void setEncryptionAlgorithm(String encryptionAlgorithm) { this.encryptionAlgorithm = encryptionAlgorithm; }
    public String getSignatureAlgorithm() { return signatureAlgorithm; }
    public void setSignatureAlgorithm(String signatureAlgorithm) { this.signatureAlgorithm = signatureAlgorithm; }
    public String getConnectionStatus() { return connectionStatus; }
    public void setConnectionStatus(String connectionStatus) { this.connectionStatus = connectionStatus; }
    public LocalDateTime getLastTestTime() { return lastTestTime; }
    public void setLastTestTime(LocalDateTime lastTestTime) { this.lastTestTime = lastTestTime; }
    public String getLastTestResult() { return lastTestResult; }
    public void setLastTestResult(String lastTestResult) { this.lastTestResult = lastTestResult; }
    public LocalDate getAuthorizationStartDate() { return authorizationStartDate; }
    public void setAuthorizationStartDate(LocalDate authorizationStartDate) { this.authorizationStartDate = authorizationStartDate; }
    public LocalDate getAuthorizationEndDate() { return authorizationEndDate; }
    public void setAuthorizationEndDate(LocalDate authorizationEndDate) { this.authorizationEndDate = authorizationEndDate; }
    public String getAuthorizedOperations() { return authorizedOperations; }
    public void setAuthorizedOperations(String authorizedOperations) { this.authorizedOperations = authorizedOperations; }
    public BigDecimal getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(BigDecimal dailyLimit) { this.dailyLimit = dailyLimit; }
    public BigDecimal getSingleLimit() { return singleLimit; }
    public void setSingleLimit(BigDecimal singleLimit) { this.singleLimit = singleLimit; }
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
