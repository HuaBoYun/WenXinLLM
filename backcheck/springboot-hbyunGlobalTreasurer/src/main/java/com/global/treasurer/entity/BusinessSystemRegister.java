package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 业务系统注册实体类
 * 对应表：TBL_BUSINESS_SYSTEM_REGISTER
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@Data
@TableName("TBL_BUSINESS_SYSTEM_REGISTER")
public class BusinessSystemRegister {
    /**
     * 系统ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("SYSTEM_ID")
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
     * 系统类型(FINANCE财务系统, BANK银行系统, ERP系统, CRM系统, THIRD_PARTY第三方系统)
     */
    @TableField("SYSTEM_TYPE")
    private String systemType;

    /**
     * 系统描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * API地址
     */
    @TableField("API_URL")
    private String apiUrl;

    /**
     * 认证方式(OAUTH2, API_KEY, BASIC_AUTH, JWT)
     */
    @TableField("AUTH_TYPE")
    private String authType;

    /**
     * 认证配置(JSON格式)
     */
    @TableField("AUTH_CONFIG")
    private String authConfig;

    /**
     * 连接状态(ONLINE在线, OFFLINE离线, ERROR异常)
     */
    @TableField("CONNECTION_STATUS")
    private String connectionStatus;

    /**
     * 最后心跳时间
     */
    @TableField("LAST_HEARTBEAT")
    private Date lastHeartbeat;

    /**
     * 状态(1启用, 0禁用)
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 创建人ID
     */
    @TableField("CREATED_BY")
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField("CREATED_BY_NAME")
    private String createdByName;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATED_BY")
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 删除标记(0未删除, 1已删除)
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    // Getter and Setter methods for Lombok compatibility
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }
}
