package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 第三方账户管理实体类
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_THIRD_PARTY_ACCOUNT")
public class TblThirdPartyAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID - 使用雪花算法自动生成，兼容达梦数据库
     * 使用 @JsonSerialize 将 Long 序列化为 String，避免 JavaScript 大数字精度丢失
     */
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 账户编码
     */
    private String accountCode;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 第三方系统
     */
    private String thirdPartySystem;

    /**
     * 账户类型
     */
    private String accountType;

    /**
     * 账户标识
     */
    private String accountIdentifier;

    /**
     * 认证配置(JSON格式)
     */
    private String authConfig;

    /**
     * 连接状态
     */
    private String connectionStatus;

    /**
     * 最后同步时间
     */
    private Date lastSyncTime;

    /**
     * API接口地址
     */
    private String apiUrl;

    /**
     * 同步频率
     */
    private String syncFrequency;

    /**
     * 账户密钥
     */
    private String accountSecret;

    /**
     * 账户描述
     */
    private String description;

    /**
     * 是否启用(1-启用 0-禁用)
     */
    private Integer isEnabled;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountCode() { return accountCode; }
    public void setAccountCode(String accountCode) { this.accountCode = accountCode; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getThirdPartySystem() { return thirdPartySystem; }
    public void setThirdPartySystem(String thirdPartySystem) { this.thirdPartySystem = thirdPartySystem; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getAccountIdentifier() { return accountIdentifier; }
    public void setAccountIdentifier(String accountIdentifier) { this.accountIdentifier = accountIdentifier; }
    public String getAuthConfig() { return authConfig; }
    public void setAuthConfig(String authConfig) { this.authConfig = authConfig; }
    public String getConnectionStatus() { return connectionStatus; }
    public void setConnectionStatus(String connectionStatus) { this.connectionStatus = connectionStatus; }
    public Date getLastSyncTime() { return lastSyncTime; }
    public void setLastSyncTime(Date lastSyncTime) { this.lastSyncTime = lastSyncTime; }
    public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }
    public String getSyncFrequency() { return syncFrequency; }
    public void setSyncFrequency(String syncFrequency) { this.syncFrequency = syncFrequency; }
    public String getAccountSecret() { return accountSecret; }
    public void setAccountSecret(String accountSecret) { this.accountSecret = accountSecret; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
