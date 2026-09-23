package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 银企联配置实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_BANK_INTERFACE_CONFIG")
public class TblBankInterfaceConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    // 无参构造方法，用于 Jackson 反序列化
    public TblBankInterfaceConfig() {}

    /** 配置ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long configId;

    /** 银行名称 */
    private String bankName;

    /** 银行代码 */
    private String bankCode;

    /** 接口类型 */
    private String interfaceType;

    /** 接口版本 */
    private String interfaceVersion;

    /** 接口名称 */
    private String interfaceName;

    /** 协议类型(HTTPS/SFTP/WEBSERVICE/SOCKET) */
    private String protocolType;

    /** API地址（兼容旧字段） */
    private String apiUrl;

    /** 接口地址 */
    private String endpointUrl;

    /** 健康检查地址 */
    private String healthCheckUrl;

    /** 认证类型(NONE/BASIC/TOKEN/CERTIFICATE) */
    private String authType;

    /** 认证信息 */
    private String authToken;

    /** 认证配置(JSON) */
    private String authConfig;

    /** SSL配置(JSON) */
    private String sslConfig;

    /** 连接超时时间(秒) */
    private Integer connectionTimeout;

    /** 读取超时时间(秒) */
    private Integer readTimeout;

    /** 最大重试次数 */
    private Integer maxRetryCount;

    /** 优先级 */
    private Integer priority;

    /** 负载均衡权重 */
    private Integer loadBalanceWeight;

    /** 健康检查间隔(秒) */
    private Integer healthCheckInterval;

    /** 健康状态(HEALTHY/UNHEALTHY/UNKNOWN) */
    private String healthStatus;

    /** 是否启用(0-禁用,1-启用) */
    private Integer isEnabled;

    /** 最后连接时间 */
    private Date lastConnectTime;

    /** 最后连接状态(SUCCESS-成功,FAILED-失败) */
    private String lastConnectStatus;

    /** 最后连接信息 */
    private String lastConnectMessage;

    /** 最后健康检查时间 */
    private Date lastHealthCheckTime;

    /** 配置描述 */
    private String configDescription;

    /** 备注 */
    private String remark;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

    /** 创建人 */
    @TableField("CREATE_BY")
    private Long createBy;

    /** 创建人姓名 */
    @TableField("CREATE_BY_NAME")
    private String createByName;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新人 */
    @TableField("UPDATE_BY")
    private Long updateBy;

    /** 更新人姓名 */
    @TableField("UPDATE_BY_NAME")
    private String updateByName;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /** 组织ID */
    private Long orgId;

    /** 组织名称 */
    private String orgName;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getConfigId() { return configId; }
    public void setConfigId(Long configId) { this.configId = configId; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getInterfaceType() { return interfaceType; }
    public void setInterfaceType(String interfaceType) { this.interfaceType = interfaceType; }
    public String getInterfaceVersion() { return interfaceVersion; }
    public void setInterfaceVersion(String interfaceVersion) { this.interfaceVersion = interfaceVersion; }
    public String getInterfaceName() { return interfaceName; }
    public void setInterfaceName(String interfaceName) { this.interfaceName = interfaceName; }
    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
    public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public String getHealthCheckUrl() { return healthCheckUrl; }
    public void setHealthCheckUrl(String healthCheckUrl) { this.healthCheckUrl = healthCheckUrl; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }
    public String getAuthConfig() { return authConfig; }
    public void setAuthConfig(String authConfig) { this.authConfig = authConfig; }
    public String getSslConfig() { return sslConfig; }
    public void setSslConfig(String sslConfig) { this.sslConfig = sslConfig; }
    public Integer getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(Integer connectionTimeout) { this.connectionTimeout = connectionTimeout; }
    public Integer getReadTimeout() { return readTimeout; }
    public void setReadTimeout(Integer readTimeout) { this.readTimeout = readTimeout; }
    public Integer getMaxRetryCount() { return maxRetryCount; }
    public void setMaxRetryCount(Integer maxRetryCount) { this.maxRetryCount = maxRetryCount; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getLoadBalanceWeight() { return loadBalanceWeight; }
    public void setLoadBalanceWeight(Integer loadBalanceWeight) { this.loadBalanceWeight = loadBalanceWeight; }
    public Integer getHealthCheckInterval() { return healthCheckInterval; }
    public void setHealthCheckInterval(Integer healthCheckInterval) { this.healthCheckInterval = healthCheckInterval; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Date getLastConnectTime() { return lastConnectTime; }
    public void setLastConnectTime(Date lastConnectTime) { this.lastConnectTime = lastConnectTime; }
    public String getLastConnectStatus() { return lastConnectStatus; }
    public void setLastConnectStatus(String lastConnectStatus) { this.lastConnectStatus = lastConnectStatus; }
    public String getLastConnectMessage() { return lastConnectMessage; }
    public void setLastConnectMessage(String lastConnectMessage) { this.lastConnectMessage = lastConnectMessage; }
    public Date getLastHealthCheckTime() { return lastHealthCheckTime; }
    public void setLastHealthCheckTime(Date lastHealthCheckTime) { this.lastHealthCheckTime = lastHealthCheckTime; }
    public String getConfigDescription() { return configDescription; }
    public void setConfigDescription(String configDescription) { this.configDescription = configDescription; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public String getCreateByName() { return createByName; }
    public void setCreateByName(String createByName) { this.createByName = createByName; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }
    public String getUpdateByName() { return updateByName; }
    public void setUpdateByName(String updateByName) { this.updateByName = updateByName; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
}
