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
 * 银企接口配置实体类
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除

@TableName("TC_BANK_INTERFACE_CONFIG")
public class TcBankInterfaceConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

    /**
     * 银行代码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 接口类型
     */
    @TableField("INTERFACE_TYPE")
    private String interfaceType;

    /**
     * 接口名称
     */
    @TableField("INTERFACE_NAME")
    private String interfaceName;

    /**
     * 业务类型
     */
    @TableField("BUSINESS_TYPE")
    private String businessType;

    /**
     * 单据来源
     */
    @TableField("DOCUMENT_SOURCE")
    private String documentSource;

    /**
     * 单据类型
     */
    @TableField("DOCUMENT_TYPE")
    private String documentType;

    /**
     * 接口地址
     */
    @TableField("INTERFACE_URL")
    private String interfaceUrl;

    /**
     * 接口端口
     */
    @TableField("INTERFACE_PORT")
    private Integer interfacePort;

    /**
     * 协议类型
     */
    @TableField("PROTOCOL_TYPE")
    private String protocolType;

    /**
     * 认证类型
     */
    @TableField("AUTH_TYPE")
    private String authType;

    /**
     * 证书路径
     */
    @TableField("CERT_PATH")
    private String certPath;

    /**
     * 证书密码
     */
    @TableField("CERT_PASSWORD")
    private String certPassword;

    /**
     * 密钥路径
     */
    @TableField("KEY_PATH")
    private String keyPath;

    /**
     * 签名方式
     */
    @TableField("SIGNATURE_METHOD")
    private String signatureMethod;

    /**
     * 加密方式
     */
    @TableField("ENCRYPTION_METHOD")
    private String encryptionMethod;

    /**
     * 超时时间(秒)
     */
    @TableField("TIMEOUT_SECONDS")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @TableField("RETRY_COUNT")
    private Integer retryCount;

    /**
     * 优先级
     */
    @TableField("PRIORITY_LEVEL")
    private Integer priorityLevel;

    /**
     * 备用接口ID
     */
    @TableField("BACKUP_INTERFACE_ID")
    private String backupInterfaceId;

    /**
     * 是否激活(1-是,0-否)
     */
    @TableField("IS_ACTIVE")
    private String isActive;

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
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getInterfaceType() { return interfaceType; }
    public void setInterfaceType(String interfaceType) { this.interfaceType = interfaceType; }
    public String getInterfaceName() { return interfaceName; }
    public void setInterfaceName(String interfaceName) { this.interfaceName = interfaceName; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getDocumentSource() { return documentSource; }
    public void setDocumentSource(String documentSource) { this.documentSource = documentSource; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getInterfaceUrl() { return interfaceUrl; }
    public void setInterfaceUrl(String interfaceUrl) { this.interfaceUrl = interfaceUrl; }
    public Integer getInterfacePort() { return interfacePort; }
    public void setInterfacePort(Integer interfacePort) { this.interfacePort = interfacePort; }
    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
    public String getCertPath() { return certPath; }
    public void setCertPath(String certPath) { this.certPath = certPath; }
    public String getCertPassword() { return certPassword; }
    public void setCertPassword(String certPassword) { this.certPassword = certPassword; }
    public String getKeyPath() { return keyPath; }
    public void setKeyPath(String keyPath) { this.keyPath = keyPath; }
    public String getSignatureMethod() { return signatureMethod; }
    public void setSignatureMethod(String signatureMethod) { this.signatureMethod = signatureMethod; }
    public String getEncryptionMethod() { return encryptionMethod; }
    public void setEncryptionMethod(String encryptionMethod) { this.encryptionMethod = encryptionMethod; }
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Integer getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(Integer priorityLevel) { this.priorityLevel = priorityLevel; }
    public String getBackupInterfaceId() { return backupInterfaceId; }
    public void setBackupInterfaceId(String backupInterfaceId) { this.backupInterfaceId = backupInterfaceId; }
    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }
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
