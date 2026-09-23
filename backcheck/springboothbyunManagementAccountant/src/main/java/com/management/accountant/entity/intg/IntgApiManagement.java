package com.management.accountant.entity.intg;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API接口管理实体类
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_intg_api_management")
public class IntgApiManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * API管理ID，主键
     */
    @TableId(value = "api_id", type = IdType.ASSIGN_ID)
    private String apiId;

    /**
     * API编码，唯一标识
     */
    @TableField("api_code")
    private String apiCode;

    /**
     * API名称
     */
    @TableField("api_name")
    private String apiName;

    /**
     * API版本
     */
    @TableField("api_version")
    private String apiVersion;

    /**
     * API类型：REST/SOAP/GRAPHQL/RPC
     */
    @TableField("api_type")
    private String apiType;

    /**
     * API分类：BUSINESS-业务接口/SYSTEM-系统接口/INTEGRATION-集成接口
     */
    @TableField("api_category")
    private String apiCategory;

    /**
     * 接口路径
     */
    @TableField("endpoint_path")
    private String endpointPath;

    /**
     * HTTP方法：GET/POST/PUT/DELETE/PATCH
     */
    @TableField("http_method")
    private String httpMethod;

    /**
     * 请求格式：JSON/XML/FORM/MULTIPART
     */
    @TableField("request_format")
    private String requestFormat;

    /**
     * 响应格式：JSON/XML/TEXT/BINARY
     */
    @TableField("response_format")
    private String responseFormat;

    /**
     * 请求结构，JSON Schema格式
     */
    @TableField("request_schema")
    private String requestSchema;

    /**
     * 响应结构，JSON Schema格式
     */
    @TableField("response_schema")
    private String responseSchema;

    /**
     * 是否需要认证：0-否/1-是
     */
    @TableField("authentication_required")
    private Boolean authenticationRequired;

    /**
     * 授权配置，JSON格式存储
     */
    @TableField("authorization_config")
    private String authorizationConfig;

    /**
     * 限流配置，JSON格式存储
     */
    @TableField("rate_limit_config")
    private String rateLimitConfig;

    /**
     * 缓存配置，JSON格式存储
     */
    @TableField("caching_config")
    private String cachingConfig;

    /**
     * 验证规则，JSON格式存储
     */
    @TableField("validation_rules")
    private String validationRules;

    /**
     * API文档
     */
    @TableField("documentation")
    private String documentation;

    /**
     * 请求示例
     */
    @TableField("example_request")
    private String exampleRequest;

    /**
     * 响应示例
     */
    @TableField("example_response")
    private String exampleResponse;

    /**
     * 错误码定义，JSON格式存储
     */
    @TableField("error_codes")
    private String errorCodes;

    /**
     * 是否已废弃：0-否/1-是
     */
    @TableField("is_deprecated")
    private Boolean isDeprecated;

    /**
     * 废弃日期
     */
    @TableField("deprecation_date")
    private LocalDateTime deprecationDate;

    /**
     * 替代API ID
     */
    @TableField("replacement_api_id")
    private String replacementApiId;

    /**
     * 是否启用监控：0-否/1-是
     */
    @TableField("monitoring_enabled")
    private Boolean monitoringEnabled;

    /**
     * 日志级别：DEBUG/INFO/WARN/ERROR
     */
    @TableField("logging_level")
    private String loggingLevel;

    /**
     * 状态：ACTIVE-激活/INACTIVE-停用/TESTING-测试中
     */
    @TableField("status")
    private String status;

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
     * API描述
     */
    @TableField("api_description")
    private String apiDescription;

    /**
     * 服务提供者
     */
    @TableField("service_provider")
    private String serviceProvider;

    /**
     * 服务消费者
     */
    @TableField("service_consumer")
    private String serviceConsumer;

    /**
     * 接口负责人
     */
    @TableField("interface_owner")
    private String interfaceOwner;

    /**
     * 接口联系人
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 联系邮箱
     */
    @TableField("contact_email")
    private String contactEmail;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 业务域
     */
    @TableField("business_domain")
    private String businessDomain;

    /**
     * 技术域
     */
    @TableField("technical_domain")
    private String technicalDomain;

    /**
     * 优先级：HIGH-高/MEDIUM-中/LOW-低
     */
    @TableField("priority")
    private String priority;

    /**
     * 重要性：CRITICAL-关键/IMPORTANT-重要/NORMAL-普通
     */
    @TableField("importance")
    private String importance;

    /**
     * 稳定性：STABLE-稳定/BETA-测试/ALPHA-内测
     */
    @TableField("stability")
    private String stability;

    /**
     * 兼容性：BACKWARD-向后兼容/FORWARD-向前兼容/BREAKING-破坏性变更
     */
    @TableField("compatibility")
    private String compatibility;

    /**
     * 安全级别：PUBLIC-公开/INTERNAL-内部/CONFIDENTIAL-机密/SECRET-秘密
     */
    @TableField("security_level")
    private String securityLevel;

    /**
     * 数据分类：PUBLIC-公开/INTERNAL-内部/SENSITIVE-敏感/RESTRICTED-受限
     */
    @TableField("data_classification")
    private String dataClassification;

    /**
     * 合规要求
     */
    @TableField("compliance_requirements")
    private String complianceRequirements;

    /**
     * SLA要求，JSON格式存储
     */
    @TableField("sla_requirements")
    private String slaRequirements;

    /**
     * 性能要求，JSON格式存储
     */
    @TableField("performance_requirements")
    private String performanceRequirements;

    /**
     * 可用性要求
     */
    @TableField("availability_requirements")
    private String availabilityRequirements;

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
