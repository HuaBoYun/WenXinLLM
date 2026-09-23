package com.management.accountant.entity.mobile;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动功能配置实体类
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_mobile_function_config")
public class MobileFunctionConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 功能配置ID，主键
     */
    @TableId(value = "function_config_id", type = IdType.ASSIGN_ID)
    private String functionConfigId;

    /**
     * 应用配置ID，外键关联
     */
    @TableField("app_config_id")
    private String appConfigId;

    /**
     * 功能名称
     */
    @TableField("function_name")
    private String functionName;

    /**
     * 功能编码
     */
    @TableField("function_code")
    private String functionCode;

    /**
     * 功能类型：MENU-菜单功能/BUTTON-按钮功能/PAGE-页面功能/WIDGET-组件功能/SERVICE-服务功能
     */
    @TableField("function_type")
    private String functionType;

    /**
     * 功能分类：CORE-核心功能/BUSINESS-业务功能/TOOL-工具功能/SYSTEM-系统功能
     */
    @TableField("function_category")
    private String functionCategory;

    /**
     * 功能模块
     */
    @TableField("function_module")
    private String functionModule;

    /**
     * 功能路径
     */
    @TableField("function_path")
    private String functionPath;

    /**
     * 功能URL
     */
    @TableField("function_url")
    private String functionUrl;

    /**
     * 功能图标
     */
    @TableField("function_icon")
    private String functionIcon;

    /**
     * 功能图片URL
     */
    @TableField("function_image_url")
    private String functionImageUrl;

    /**
     * 功能描述
     */
    @TableField("function_description")
    private String functionDescription;

    /**
     * 功能配置JSON，存储详细配置信息
     */
    @TableField("function_config_json")
    private String functionConfigJson;

    /**
     * 功能参数，JSON格式存储
     */
    @TableField("function_params")
    private String functionParams;

    /**
     * 功能权限，JSON格式存储
     */
    @TableField("function_permissions")
    private String functionPermissions;

    /**
     * 功能角色，JSON格式存储
     */
    @TableField("function_roles")
    private String functionRoles;

    /**
     * 功能用户组，JSON格式存储
     */
    @TableField("function_user_groups")
    private String functionUserGroups;

    /**
     * 功能依赖，JSON格式存储
     */
    @TableField("function_dependencies")
    private String functionDependencies;

    /**
     * 功能前置条件，JSON格式存储
     */
    @TableField("function_preconditions")
    private String functionPreconditions;

    /**
     * 功能后置操作，JSON格式存储
     */
    @TableField("function_post_actions")
    private String functionPostActions;

    /**
     * 功能验证规则，JSON格式存储
     */
    @TableField("function_validation_rules")
    private String functionValidationRules;

    /**
     * 功能缓存配置，JSON格式存储
     */
    @TableField("function_cache_config")
    private String functionCacheConfig;

    /**
     * 功能日志配置，JSON格式存储
     */
    @TableField("function_log_config")
    private String functionLogConfig;

    /**
     * 功能监控配置，JSON格式存储
     */
    @TableField("function_monitor_config")
    private String functionMonitorConfig;

    /**
     * 功能性能配置，JSON格式存储
     */
    @TableField("function_performance_config")
    private String functionPerformanceConfig;

    /**
     * 功能安全配置，JSON格式存储
     */
    @TableField("function_security_config")
    private String functionSecurityConfig;

    /**
     * 是否启用：0-否/1-是
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否可见：0-否/1-是
     */
    @TableField("is_visible")
    private Boolean isVisible;

    /**
     * 是否必需：0-否/1-是
     */
    @TableField("is_required")
    private Boolean isRequired;

    /**
     * 是否支持离线：0-否/1-是
     */
    @TableField("is_offline_support")
    private Boolean isOfflineSupport;

    /**
     * 是否支持缓存：0-否/1-是
     */
    @TableField("is_cache_support")
    private Boolean isCacheSupport;

    /**
     * 是否支持推送：0-否/1-是
     */
    @TableField("is_push_support")
    private Boolean isPushSupport;

    /**
     * 父功能ID
     */
    @TableField("parent_function_id")
    private String parentFunctionId;

    /**
     * 功能层级
     */
    @TableField("function_level")
    private Integer functionLevel;

    /**
     * 功能路径（层级路径）
     */
    @TableField("function_tree_path")
    private String functionTreePath;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 功能版本
     */
    @TableField("function_version")
    private String functionVersion;

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
     * 功能状态：ACTIVE-激活/INACTIVE-停用/TESTING-测试中/DEPRECATED-已废弃
     */
    @TableField("function_status")
    private String functionStatus;

    /**
     * 功能优先级：HIGH-高/MEDIUM-中/LOW-低
     */
    @TableField("function_priority")
    private String functionPriority;

    /**
     * 功能重要性：CRITICAL-关键/IMPORTANT-重要/NORMAL-普通/LOW-低
     */
    @TableField("function_importance")
    private String functionImportance;

    /**
     * 功能复杂度：HIGH-高/MEDIUM-中/LOW-低
     */
    @TableField("function_complexity")
    private String functionComplexity;

    /**
     * 功能稳定性：STABLE-稳定/BETA-测试版/ALPHA-内测版/EXPERIMENTAL-实验性
     */
    @TableField("function_stability")
    private String functionStability;

    /**
     * 功能兼容性：FULL-完全兼容/PARTIAL-部分兼容/LIMITED-有限兼容/NONE-不兼容
     */
    @TableField("function_compatibility")
    private String functionCompatibility;

    /**
     * 功能使用次数
     */
    @TableField("usage_count")
    private Long usageCount;

    /**
     * 功能使用时长（分钟）
     */
    @TableField("usage_duration")
    private Long usageDuration;

    /**
     * 功能错误次数
     */
    @TableField("error_count")
    private Long errorCount;

    /**
     * 功能成功率
     */
    @TableField("success_rate")
    private Double successRate;

    /**
     * 功能响应时间（毫秒）
     */
    @TableField("response_time")
    private Long responseTime;

    /**
     * 功能内存使用（MB）
     */
    @TableField("memory_usage")
    private Double memoryUsage;

    /**
     * 功能CPU使用率
     */
    @TableField("cpu_usage")
    private Double cpuUsage;

    /**
     * 功能网络使用（KB）
     */
    @TableField("network_usage")
    private Long networkUsage;

    /**
     * 最后使用时间
     */
    @TableField("last_used_time")
    private LocalDateTime lastUsedTime;

    /**
     * 最后更新时间
     */
    @TableField("last_updated_time")
    private LocalDateTime lastUpdatedTime;

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
     * 功能开发者
     */
    @TableField("function_developer")
    private String functionDeveloper;

    /**
     * 功能负责人
     */
    @TableField("function_owner")
    private String functionOwner;

    /**
     * 功能联系人
     */
    @TableField("function_contact")
    private String functionContact;

    /**
     * 功能联系邮箱
     */
    @TableField("function_email")
    private String functionEmail;

    /**
     * 功能联系电话
     */
    @TableField("function_phone")
    private String functionPhone;

    /**
     * 功能文档URL
     */
    @TableField("function_doc_url")
    private String functionDocUrl;

    /**
     * 功能帮助URL
     */
    @TableField("function_help_url")
    private String functionHelpUrl;

    /**
     * 功能视频URL
     */
    @TableField("function_video_url")
    private String functionVideoUrl;

    /**
     * 功能截图URL，多个URL用逗号分隔
     */
    @TableField("function_screenshot_urls")
    private String functionScreenshotUrls;

    /**
     * 功能标签，多个标签用逗号分隔
     */
    @TableField("function_tags")
    private String functionTags;

    /**
     * 功能关键词，多个关键词用逗号分隔
     */
    @TableField("function_keywords")
    private String functionKeywords;

    /**
     * 功能更新日志
     */
    @TableField("function_changelog")
    private String functionChangelog;

    /**
     * 功能已知问题
     */
    @TableField("function_known_issues")
    private String functionKnownIssues;

    /**
     * 功能计划改进
     */
    @TableField("function_planned_improvements")
    private String functionPlannedImprovements;

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
