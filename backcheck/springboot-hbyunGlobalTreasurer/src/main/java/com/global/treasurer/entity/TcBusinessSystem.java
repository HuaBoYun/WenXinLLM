package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除,使用手动编写的getter/setter

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务系统注册实体类
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TC_BUSINESS_SYSTEM")
public class TcBusinessSystem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 系统名称
     */
    @TableField("SYSTEM_NAME")
    private String systemName;

    /**
     * 系统编码
     */
    @TableField("SYSTEM_CODE")
    private String systemCode;

    /**
     * 系统类型
     */
    @TableField("SYSTEM_TYPE")
    private String systemType;

    /**
     * 接口地址
     */
    @TableField("API_ENDPOINT")
    private String apiUrl;

    /**
     * 认证方式
     */
    @TableField("AUTH_TYPE")
    private String authType;

    /**
     * 认证配置JSON
     */
    @TableField("AUTH_CONFIG")
    private String authConfig;

    /**
     * 系统描述
     */
    @TableField("SYSTEM_DESC")
    private String systemDesc;

    /**
     * 状态(0-禁用,1-启用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 连接状态(OFFLINE-离线,ONLINE-在线,ERROR-错误)
     */
    @TableField("CONNECTION_STATUS")
    private String connectionStatus;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
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
     * 最后心跳时间
     */
    @TableField("LAST_HEARTBEAT")
    private Date lastHeartbeat;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Integer versionNo;

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthConfig() {
        return authConfig;
    }

    public void setAuthConfig(String authConfig) {
        this.authConfig = authConfig;
    }

    public String getSystemDesc() {
        return systemDesc;
    }

    public void setSystemDesc(String systemDesc) {
        this.systemDesc = systemDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
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

    public Date getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Date lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}