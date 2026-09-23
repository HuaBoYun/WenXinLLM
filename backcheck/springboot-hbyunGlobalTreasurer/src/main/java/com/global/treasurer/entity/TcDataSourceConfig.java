package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除,使用手动编写的getter/setter
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.io.Serializable;
import java.util.Date;

/**
 * 财资公共模块 - 数据源配置表
 *
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcDataSourceConfig", description = "数据源配置管理")
// @Data // 已移除,使用手动编写的getter/setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("TC_DATA_SOURCE_CONFIG")
public class TcDataSourceConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 数据源编码
     */
    @TableField("SOURCE_CODE")
    @ApiModelProperty(value = "数据源编码", required = true)
    private String sourceCode;

    /**
     * 数据源名称（对应前端的dataSourceName）
     */
    @TableField("SOURCE_NAME")
    @ApiModelProperty(value = "数据源名称", required = true)
    private String sourceName;

    /**
     * 数据源类型
     */
    @TableField("SOURCE_TYPE")
    @ApiModelProperty(value = "数据源类型", required = true)
    private String sourceType;

    /**
     * 数据类型
     */
    @TableField("DATA_TYPE")
    @ApiModelProperty(value = "数据类型", required = true)
    private String dataType;

    /**
     * 连接配置
     */
    @TableField("CONNECTION_CONFIG")
    @ApiModelProperty(value = "连接配置")
    private String connectionConfig;

    /**
     * API配置
     */
    @TableField("API_CONFIG")
    @ApiModelProperty(value = "API配置")
    private String apiConfig;

    /**
     * 连接地址（对应前端的connectionUrl）
     * 注意：此字段在数据库表中可能不存在，已设置为exist=false
     */
    @TableField(value = "CONNECTION_URL", exist = false)
    @ApiModelProperty(value = "连接地址")
    private String connectionUrl;

    /**
     * 用户名
     * 注意：此字段在数据库表中可能不存在，已设置为exist=false
     */
    @TableField(value = "USERNAME", exist = false)
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     * 注意：此字段在数据库表中可能不存在，已设置为exist=false
     */
    @TableField(value = "PASSWORD", exist = false)
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 环境类型（对应前端的environment）
     * 注意：此字段在数据库表中可能不存在，已设置为exist=false
     */
    @TableField(value = "ENVIRONMENT", exist = false)
    @ApiModelProperty(value = "环境类型")
    private String environment;

    /**
     * 连接状态（对应前端的connectionStatus）
     * 注意：此字段在数据库表中可能不存在，已设置为exist=false
     */
    @TableField(value = "CONNECTION_STATUS", exist = false)
    @ApiModelProperty(value = "连接状态")
    private String connectionStatus;

    /**
     * 最后测试时间（对应前端的lastTestTime）
     * 注意：此字段在数据库表中可能不存在，已设置为exist=false
     */
    @TableField(value = "LAST_TEST_TIME", exist = false)
    @ApiModelProperty(value = "最后测试时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastTestTime;

    /**
     * 是否启用（对应前端的isEnabled）
     * 注意：此字段在数据库表中可能不存在，已设置为exist=false
     */
    @TableField(value = "IS_ENABLED", exist = false)
    @ApiModelProperty(value = "是否启用：1-是，0-否")
    private String isEnabled;

    /**
     * 描述（对应前端的description）
     * 注意：此字段在数据库表中可能不存在，已设置为exist=false
     */
    @TableField(value = "DESCRIPTION", exist = false)
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 同步频率
     */
    @TableField("SYNC_FREQUENCY")
    @ApiModelProperty(value = "同步频率")
    private String syncFrequency;

    /**
     * 最后同步时间
     */
    @TableField("LAST_SYNC_TIME")
    @ApiModelProperty(value = "最后同步时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastSyncTime;

    /**
     * 同步状态
     */
    @TableField("SYNC_STATUS")
    @ApiModelProperty(value = "同步状态")
    private String syncStatus;

    /**
     * 错误次数
     */
    @TableField("ERROR_COUNT")
    @ApiModelProperty(value = "错误次数")
    private Integer errorCount;

    /**
     * 最大错误次数
     */
    @TableField("MAX_ERROR_COUNT")
    @ApiModelProperty(value = "最大错误次数")
    private Integer maxErrorCount;

    /**
     * 超时时间（秒）
     */
    @TableField("TIMEOUT_SECONDS")
    @ApiModelProperty(value = "超时时间（秒）")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @TableField("RETRY_COUNT")
    @ApiModelProperty(value = "重试次数")
    private Integer retryCount;

    /**
     * 是否激活：1-是，0-否
     */
    @TableField("IS_ACTIVE")
    @ApiModelProperty(value = "是否激活：1-是，0-否")
    private String isActive;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcDataSourceConfig ofId(String id) {
        TcDataSourceConfig dataSourceConfig = new TcDataSourceConfig();
        dataSourceConfig.setId(id);
        return dataSourceConfig;
    }

    /**
     * 根据数据源编码创建实例
     */
    public static TcDataSourceConfig ofSourceCode(String sourceCode) {
        TcDataSourceConfig dataSourceConfig = new TcDataSourceConfig();
        dataSourceConfig.setSourceCode(sourceCode);
        return dataSourceConfig;
    }

    /**
     * 根据数据源类型创建实例
     */
    public static TcDataSourceConfig ofSourceType(String sourceType) {
        TcDataSourceConfig dataSourceConfig = new TcDataSourceConfig();
        dataSourceConfig.setSourceType(sourceType);
        return dataSourceConfig;
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
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

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
