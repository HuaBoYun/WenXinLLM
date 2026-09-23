package com.management.accountant.entity.mobile;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动应用配置实体类
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_mobile_app_config")
public class MobileAppConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用配置ID，主键
     */
    @TableId(value = "app_config_id", type = IdType.ASSIGN_ID)
    private String appConfigId;

    /**
     * 应用名称
     */
    @TableField("app_name")
    private String appName;

    /**
     * 应用编码
     */
    @TableField("app_code")
    private String appCode;

    /**
     * 应用版本
     */
    @TableField("app_version")
    private String appVersion;

    /**
     * 应用类型：NATIVE-原生应用/HYBRID-混合应用/WEB-Web应用/PWA-渐进式Web应用
     */
    @TableField("app_type")
    private String appType;

    /**
     * 应用平台：ANDROID-安卓/IOS-苹果/WINDOWS-Windows/ALL-全平台
     */
    @TableField("app_platform")
    private String appPlatform;

    /**
     * 应用包名
     */
    @TableField("app_package")
    private String appPackage;

    /**
     * 应用图标URL
     */
    @TableField("app_icon_url")
    private String appIconUrl;

    /**
     * 应用启动页URL
     */
    @TableField("app_splash_url")
    private String appSplashUrl;

    /**
     * 应用主页URL
     */
    @TableField("app_home_url")
    private String appHomeUrl;

    /**
     * 应用下载URL
     */
    @TableField("app_download_url")
    private String appDownloadUrl;

    /**
     * 应用更新URL
     */
    @TableField("app_update_url")
    private String appUpdateUrl;

    /**
     * 应用配置JSON，存储详细配置信息
     */
    @TableField("app_config_json")
    private String appConfigJson;

    /**
     * 主题配置，JSON格式存储
     */
    @TableField("theme_config")
    private String themeConfig;

    /**
     * 布局配置，JSON格式存储
     */
    @TableField("layout_config")
    private String layoutConfig;

    /**
     * 导航配置，JSON格式存储
     */
    @TableField("navigation_config")
    private String navigationConfig;

    /**
     * 菜单配置，JSON格式存储
     */
    @TableField("menu_config")
    private String menuConfig;

    /**
     * 权限配置，JSON格式存储
     */
    @TableField("permission_config")
    private String permissionConfig;

    /**
     * 安全配置，JSON格式存储
     */
    @TableField("security_config")
    private String securityConfig;

    /**
     * 网络配置，JSON格式存储
     */
    @TableField("network_config")
    private String networkConfig;

    /**
     * 缓存配置，JSON格式存储
     */
    @TableField("cache_config")
    private String cacheConfig;

    /**
     * 日志配置，JSON格式存储
     */
    @TableField("log_config")
    private String logConfig;

    /**
     * 推送配置，JSON格式存储
     */
    @TableField("push_config")
    private String pushConfig;

    /**
     * 分析配置，JSON格式存储
     */
    @TableField("analytics_config")
    private String analyticsConfig;

    /**
     * 是否启用：0-否/1-是
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否默认应用：0-否/1-是
     */
    @TableField("is_default")
    private Boolean isDefault;

    /**
     * 是否强制更新：0-否/1-是
     */
    @TableField("is_force_update")
    private Boolean isForceUpdate;

    /**
     * 是否支持离线：0-否/1-是
     */
    @TableField("is_offline_support")
    private Boolean isOfflineSupport;

    /**
     * 最小支持版本
     */
    @TableField("min_support_version")
    private String minSupportVersion;

    /**
     * 最大支持版本
     */
    @TableField("max_support_version")
    private String maxSupportVersion;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 下架时间
     */
    @TableField("unpublish_time")
    private LocalDateTime unpublishTime;

    /**
     * 状态：DRAFT-草稿/PUBLISHED-已发布/UNPUBLISHED-已下架/ARCHIVED-已归档
     */
    @TableField("status")
    private String status;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @TableField("version")
    private Integer version;

    // 扩展字段

    /**
     * 应用描述
     */
    @TableField("app_description")
    private String appDescription;

    /**
     * 应用分类：BUSINESS-业务应用/TOOL-工具应用/GAME-游戏应用/EDUCATION-教育应用
     */
    @TableField("app_category")
    private String appCategory;

    /**
     * 应用标签，多个标签用逗号分隔
     */
    @TableField("app_tags")
    private String appTags;

    /**
     * 应用关键词，多个关键词用逗号分隔
     */
    @TableField("app_keywords")
    private String appKeywords;

    /**
     * 应用开发者
     */
    @TableField("app_developer")
    private String appDeveloper;

    /**
     * 应用联系人
     */
    @TableField("app_contact")
    private String appContact;

    /**
     * 应用联系邮箱
     */
    @TableField("app_email")
    private String appEmail;

    /**
     * 应用联系电话
     */
    @TableField("app_phone")
    private String appPhone;

    /**
     * 应用官网
     */
    @TableField("app_website")
    private String appWebsite;

    /**
     * 应用大小（字节）
     */
    @TableField("app_size")
    private Long appSize;

    /**
     * 应用评分（1-5分）
     */
    @TableField("app_rating")
    private Double appRating;

    /**
     * 应用下载次数
     */
    @TableField("download_count")
    private Long downloadCount;

    /**
     * 应用安装次数
     */
    @TableField("install_count")
    private Long installCount;

    /**
     * 应用活跃用户数
     */
    @TableField("active_user_count")
    private Long activeUserCount;

    /**
     * 应用使用时长（分钟）
     */
    @TableField("usage_duration")
    private Long usageDuration;

    /**
     * 应用崩溃率
     */
    @TableField("crash_rate")
    private Double crashRate;

    /**
     * 应用启动时间（毫秒）
     */
    @TableField("startup_time")
    private Long startupTime;

    /**
     * 应用内存使用（MB）
     */
    @TableField("memory_usage")
    private Double memoryUsage;

    /**
     * 应用CPU使用率
     */
    @TableField("cpu_usage")
    private Double cpuUsage;

    /**
     * 应用网络使用（KB）
     */
    @TableField("network_usage")
    private Long networkUsage;

    /**
     * 应用存储使用（MB）
     */
    @TableField("storage_usage")
    private Double storageUsage;

    /**
     * 应用电池使用（mAh）
     */
    @TableField("battery_usage")
    private Double batteryUsage;

    /**
     * 应用权限列表，JSON格式存储
     */
    @TableField("app_permissions")
    private String appPermissions;

    /**
     * 应用依赖列表，JSON格式存储
     */
    @TableField("app_dependencies")
    private String appDependencies;

    /**
     * 应用环境变量，JSON格式存储
     */
    @TableField("app_environment")
    private String appEnvironment;

    /**
     * 应用启动参数，JSON格式存储
     */
    @TableField("app_launch_params")
    private String appLaunchParams;

    /**
     * 应用更新日志
     */
    @TableField("app_changelog")
    private String appChangelog;

    /**
     * 应用隐私政策
     */
    @TableField("privacy_policy")
    private String privacyPolicy;

    /**
     * 应用使用条款
     */
    @TableField("terms_of_service")
    private String termsOfService;

    /**
     * 扩展属性，JSON格式存储
     */
    @TableField("extended_attributes")
    private String extendedAttributes;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
}
