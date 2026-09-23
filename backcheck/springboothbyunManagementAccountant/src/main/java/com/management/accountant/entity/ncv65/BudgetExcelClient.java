package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - Excel客户端实体类
 * 
 * @description Excel客户端管理实体，支持Excel插件的配置和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_EXCEL_CLIENT")
public class BudgetExcelClient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 客户端编码
     */
    @TableField("CLIENT_CODE")
    private String clientCode;

    /**
     * 客户端名称
     */
    @TableField("CLIENT_NAME")
    private String clientName;

    /**
     * 客户端版本
     */
    @TableField("CLIENT_VERSION")
    private String clientVersion;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 机器码
     */
    @TableField("MACHINE_CODE")
    private String machineCode;

    /**
     * IP地址
     */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /**
     * MAC地址
     */
    @TableField("MAC_ADDRESS")
    private String macAddress;

    /**
     * 客户端状态：ACTIVE-激活，INACTIVE-停用，LOCKED-锁定
     */
    @TableField("CLIENT_STATUS")
    private String clientStatus;

    /**
     * 最后登录时间
     */
    @TableField("LAST_LOGIN_TIME")
    private LocalDateTime lastLoginTime;

    /**
     * 最后活动时间
     */
    @TableField("LAST_ACTIVITY_TIME")
    private LocalDateTime lastActivityTime;

    /**
     * 在线状态：ONLINE-在线，OFFLINE-离线
     */
    @TableField("ONLINE_STATUS")
    private String onlineStatus;

    /**
     * 授权到期时间
     */
    @TableField("LICENSE_EXPIRY_TIME")
    private LocalDateTime licenseExpiryTime;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // ==================== 常量定义 ====================

    /**
     * 客户端状态常量
     */
    public static final String CLIENT_STATUS_ACTIVE = "ACTIVE";
    public static final String CLIENT_STATUS_INACTIVE = "INACTIVE";
    public static final String CLIENT_STATUS_LOCKED = "LOCKED";

    /**
     * 在线状态常量
     */
    public static final String ONLINE_STATUS_ONLINE = "ONLINE";
    public static final String ONLINE_STATUS_OFFLINE = "OFFLINE";
}
