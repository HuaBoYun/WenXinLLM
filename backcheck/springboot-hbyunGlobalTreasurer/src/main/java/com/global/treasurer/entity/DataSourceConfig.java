package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除,使用手动编写的getter/setter

import java.io.Serializable;
import java.util.Date;

/**
 * 数据源配置实体类
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TC_DATA_SOURCE_CONFIG")
public class DataSourceConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    /**
     * 数据源ID (用于前端显示)
     */
    @TableField(exist = false)
    private Long dataSourceId;

    /**
     * 数据源编码
     */
    private String sourceCode;

    /**
     * 数据源名称
     */
    private String sourceName;

    /**
     * 数据源类型
     */
    private String sourceType;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 连接配置(JSON格式存储连接信息)
     */
    private String connectionConfig;

    /**
     * API配置(JSON格式存储API信息)
     */
    private String apiConfig;

    /**
     * 同步频率
     */
    private String syncFrequency;

    /**
     * 最后同步时间
     */
    private Date lastSyncTime;

    /**
     * 同步状态
     */
    private String syncStatus;

    /**
     * 错误次数
     */
    private Integer errorCount;

    /**
     * 最大错误次数
     */
    private Integer maxErrorCount;

    /**
     * 超时秒数
     */
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 是否激活(1-是 0-否)
     */
    private String isActive;

    /**
     * 状态(1-启用 0-禁用)
     */
    private String status;

    /**
     * 是否启用 (用于前端显示,对应status)
     */
    @TableField(exist = false)
    private Integer isEnabled;

    /**
     * 环境类型 (DEV-开发环境 TEST-测试环境 PROD-生产环境)
     */
    @TableField(exist = false)
    private String environment;

    /**
     * 连接状态 (NORMAL-正常 ERROR-异常 UNTESTED-未测试)
     */
    @TableField(exist = false)
    private String connectionStatus;

    /**
     * 最后测试时间
     */
    @TableField(exist = false)
    private Date lastTestTime;

    /**
     * 连接地址 (从connectionConfig解析出来,用于前端显示)
     */
    @TableField(exist = false)
    private String connectionUrl;

    /**
     * 数据库名称 (从connectionConfig解析出来,用于前端显示)
     */
    @TableField(exist = false)
    private String databaseName;

    /**
     * 用户名 (从connectionConfig解析出来,用于前端显示)
     */
    @TableField(exist = false)
    private String username;

    /**
     * 密码 (从connectionConfig解析出来,用于前端显示)
     */
    @TableField(exist = false)
    private String password;

    /**
     * 驱动类名 (从connectionConfig解析出来,用于前端显示)
     */
    @TableField(exist = false)
    private String driverClassName;

    /**
     * 配置描述
     */
    @TableField(exist = false)
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 版本号
     */
    private Integer versionNo;

    /**
     * 创建人 (兼容字段)
     */
    @TableField(exist = false)
    private String createBy;

    /**
     * 更新人 (兼容字段)
     */
    @TableField(exist = false)
    private String updateBy;

    // 完整的getter和setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getConnectionConfig() {
        return connectionConfig;
    }

    public void setConnectionConfig(String connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public String getApiConfig() {
        return apiConfig;
    }

    public void setApiConfig(String apiConfig) {
        this.apiConfig = apiConfig;
    }

    public String getSyncFrequency() {
        return syncFrequency;
    }

    public void setSyncFrequency(String syncFrequency) {
        this.syncFrequency = syncFrequency;
    }

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Integer getMaxErrorCount() {
        return maxErrorCount;
    }

    public void setMaxErrorCount(Integer maxErrorCount) {
        this.maxErrorCount = maxErrorCount;
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Date getLastTestTime() {
        return lastTestTime;
    }

    public void setLastTestTime(Date lastTestTime) {
        this.lastTestTime = lastTestTime;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}