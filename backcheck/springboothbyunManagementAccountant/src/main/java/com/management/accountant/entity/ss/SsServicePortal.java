package com.management.accountant.entity.ss;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 服务门户实体类
 * 管理统一服务门户配置，提供个性化门户服务
 * 
 * @author AI Assistant
 * @since 2025-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ss_service_portal")
public class SsServicePortal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 门户ID，主键
     */
    @TableId(value = "portal_id", type = IdType.AUTO)
    private Long portalId;

    /**
     * 门户名称
     */
    @TableField("portal_name")
    private String portalName;

    /**
     * 门户编码
     */
    @TableField("portal_code")
    private String portalCode;

    /**
     * 门户类型：EMPLOYEE-员工门户/CUSTOMER-客户门户/PARTNER-合作伙伴门户/ADMIN-管理门户
     */
    @TableField("portal_type")
    private String portalType;

    /**
     * 门户状态：ACTIVE-活跃/INACTIVE-非活跃/MAINTENANCE-维护中/ARCHIVED-已归档
     */
    @TableField("portal_status")
    private String portalStatus;

    /**
     * 门户URL
     */
    @TableField("portal_url")
    private String portalUrl;

    /**
     * 门户描述
     */
    @TableField("portal_description")
    private String portalDescription;

    /**
     * 门户图标
     */
    @TableField("portal_icon")
    private String portalIcon;

    /**
     * 门户横幅
     */
    @TableField("portal_banner")
    private String portalBanner;

    /**
     * 布局配置，JSON格式存储
     */
    @TableField("layout_config")
    private String layoutConfig;

    /**
     * 主题配置，JSON格式存储
     */
    @TableField("theme_config")
    private String themeConfig;

    /**
     * 个性化配置，JSON格式存储
     */
    @TableField("personalization_config")
    private String personalizationConfig;

    /**
     * 服务目录，JSON格式存储
     */
    @TableField("service_catalog")
    private String serviceCatalog;

    /**
     * 组件配置，JSON格式存储
     */
    @TableField("widget_config")
    private String widgetConfig;

    /**
     * 导航配置，JSON格式存储
     */
    @TableField("navigation_config")
    private String navigationConfig;

    /**
     * 搜索配置，JSON格式存储
     */
    @TableField("search_config")
    private String searchConfig;

    /**
     * 通知配置，JSON格式存储
     */
    @TableField("notification_config")
    private String notificationConfig;

    /**
     * 访问权限，JSON格式存储
     */
    @TableField("access_permissions")
    private String accessPermissions;

    /**
     * 使用分析，JSON格式存储
     */
    @TableField("usage_analytics")
    private String usageAnalytics;

    /**
     * 门户语言
     */
    @TableField("portal_language")
    private String portalLanguage;

    /**
     * 门户时区
     */
    @TableField("portal_timezone")
    private String portalTimezone;

    /**
     * 门户版本
     */
    @TableField("portal_version")
    private String portalVersion;

    /**
     * 门户优先级
     */
    @TableField("portal_priority")
    private Integer portalPriority;

    /**
     * 门户标签
     */
    @TableField("portal_tags")
    private String portalTags;

    /**
     * 门户分类
     */
    @TableField("portal_category")
    private String portalCategory;

    /**
     * 门户所有者ID
     */
    @TableField("portal_owner_id")
    private Long portalOwnerId;

    /**
     * 门户所有者名称
     */
    @TableField("portal_owner_name")
    private String portalOwnerName;

    /**
     * 门户管理员ID
     */
    @TableField("portal_admin_id")
    private Long portalAdminId;

    /**
     * 门户管理员名称
     */
    @TableField("portal_admin_name")
    private String portalAdminName;

    /**
     * 部门ID
     */
    @TableField("department_id")
    private Long departmentId;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 公司ID
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 访问次数
     */
    @TableField("access_count")
    private Long accessCount;

    /**
     * 最后访问时间
     */
    @TableField("last_access_time")
    private LocalDateTime lastAccessTime;

    /**
     * 最后访问用户ID
     */
    @TableField("last_access_user_id")
    private Long lastAccessUserId;

    /**
     * 最后访问用户名称
     */
    @TableField("last_access_user_name")
    private String lastAccessUserName;

    /**
     * 最后访问IP
     */
    @TableField("last_access_ip")
    private String lastAccessIp;

    /**
     * 激活时间
     */
    @TableField("activation_time")
    private LocalDateTime activationTime;

    /**
     * 停用时间
     */
    @TableField("deactivation_time")
    private LocalDateTime deactivationTime;

    /**
     * 维护开始时间
     */
    @TableField("maintenance_start_time")
    private LocalDateTime maintenanceStartTime;

    /**
     * 维护结束时间
     */
    @TableField("maintenance_end_time")
    private LocalDateTime maintenanceEndTime;

    /**
     * 维护原因
     */
    @TableField("maintenance_reason")
    private String maintenanceReason;

    /**
     * 维护结果
     */
    @TableField("maintenance_result")
    private String maintenanceResult;

    /**
     * 性能指标，JSON格式存储
     */
    @TableField("performance_metrics")
    private String performanceMetrics;

    /**
     * 用户反馈，JSON格式存储
     */
    @TableField("user_feedback")
    private String userFeedback;

    /**
     * 安全配置，JSON格式存储
     */
    @TableField("security_config")
    private String securityConfig;

    /**
     * 集成配置，JSON格式存储
     */
    @TableField("integration_config")
    private String integrationConfig;

    /**
     * 备份配置，JSON格式存储
     */
    @TableField("backup_config")
    private String backupConfig;

    /**
     * 监控配置，JSON格式存储
     */
    @TableField("monitoring_config")
    private String monitoringConfig;

    /**
     * 是否激活：0-否/1-是
     */
    @TableField("is_active")
    private Integer isActive;

    /**
     * 是否默认门户：0-否/1-是
     */
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 是否公开：0-否/1-是
     */
    @TableField("is_public")
    private Integer isPublic;

    /**
     * 是否可定制：0-否/1-是
     */
    @TableField("is_customizable")
    private Integer isCustomizable;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 扩展字段1
     */
    @TableField("ext_field1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("ext_field2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("ext_field3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("ext_field4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("ext_field5")
    private String extField5;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 是否删除：0-否/1-是
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}
