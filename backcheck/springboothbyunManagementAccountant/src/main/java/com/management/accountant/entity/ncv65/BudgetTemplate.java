package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算模板实体类
 * 
 * @description 预算模板管理，支持预算编制、报告生成等各种模板的管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_TEMPLATE")
public class BudgetTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 模板编码
     */
    @TableField("TEMPLATE_CODE")
    private String templateCode;

    /**
     * 模板名称
     */
    @TableField("TEMPLATE_NAME")
    private String templateName;

    /**
     * 模板类型
     */
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    /**
     * 模板分类
     */
    @TableField("TEMPLATE_CATEGORY")
    private String templateCategory;

    /**
     * 模板用途
     */
    @TableField("TEMPLATE_PURPOSE")
    private String templatePurpose;

    /**
     * 适用范围
     */
    @TableField("APPLICABLE_SCOPE")
    private String applicableScope;

    /**
     * 适用组织类型
     */
    @TableField("APPLICABLE_ORG_TYPE")
    private String applicableOrgType;

    /**
     * 适用行业
     */
    @TableField("APPLICABLE_INDUSTRY")
    private String applicableIndustry;

    /**
     * 模板版本
     */
    @TableField("TEMPLATE_VERSION")
    private String templateVersion;

    /**
     * 父模板ID
     */
    @TableField("PARENT_TEMPLATE_ID")
    private String parentTemplateId;

    /**
     * 基础模板ID
     */
    @TableField("BASE_TEMPLATE_ID")
    private String baseTemplateId;

    /**
     * 模板状态
     */
    @TableField("TEMPLATE_STATUS")
    private String templateStatus;

    /**
     * 审批状态
     */
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    /**
     * 发布状态
     */
    @TableField("PUBLISH_STATUS")
    private String publishStatus;

    /**
     * 模板内容
     */
    @TableField("TEMPLATE_CONTENT")
    private String templateContent;

    /**
     * 模板结构
     */
    @TableField("TEMPLATE_STRUCTURE")
    private String templateStructure;

    /**
     * 字段定义
     */
    @TableField("FIELD_DEFINITIONS")
    private String fieldDefinitions;

    /**
     * 验证规则
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 计算公式
     */
    @TableField("CALCULATION_FORMULAS")
    private String calculationFormulas;

    /**
     * 格式化规则
     */
    @TableField("FORMAT_RULES")
    private String formatRules;

    /**
     * 样式定义
     */
    @TableField("STYLE_DEFINITIONS")
    private String styleDefinitions;

    /**
     * 布局配置
     */
    @TableField("LAYOUT_CONFIG")
    private String layoutConfig;

    /**
     * 权限配置
     */
    @TableField("PERMISSION_CONFIG")
    private String permissionConfig;

    /**
     * 工作流配置
     */
    @TableField("WORKFLOW_CONFIG")
    private String workflowConfig;

    /**
     * 默认值配置
     */
    @TableField("DEFAULT_VALUES")
    private String defaultValues;

    /**
     * 选项配置
     */
    @TableField("OPTIONS_CONFIG")
    private String optionsConfig;

    /**
     * 数据源配置
     */
    @TableField("DATA_SOURCE_CONFIG")
    private String dataSourceConfig;

    /**
     * 导入配置
     */
    @TableField("IMPORT_CONFIG")
    private String importConfig;

    /**
     * 导出配置
     */
    @TableField("EXPORT_CONFIG")
    private String exportConfig;

    /**
     * 打印配置
     */
    @TableField("PRINT_CONFIG")
    private String printConfig;

    /**
     * 预览配置
     */
    @TableField("PREVIEW_CONFIG")
    private String previewConfig;

    /**
     * 是否系统模板
     */
    @TableField("IS_SYSTEM_TEMPLATE")
    private Boolean isSystemTemplate;

    /**
     * 是否默认模板
     */
    @TableField("IS_DEFAULT_TEMPLATE")
    private Boolean isDefaultTemplate;

    /**
     * 是否公共模板
     */
    @TableField("IS_PUBLIC_TEMPLATE")
    private Boolean isPublicTemplate;

    /**
     * 是否可编辑
     */
    @TableField("IS_EDITABLE")
    private Boolean isEditable;

    /**
     * 是否可复制
     */
    @TableField("IS_COPYABLE")
    private Boolean isCopyable;

    /**
     * 是否可删除
     */
    @TableField("IS_DELETABLE")
    private Boolean isDeletable;

    /**
     * 是否启用版本控制
     */
    @TableField("IS_VERSION_CONTROL")
    private Boolean isVersionControl;

    /**
     * 使用次数
     */
    @TableField("USAGE_COUNT")
    private Integer usageCount;

    /**
     * 下载次数
     */
    @TableField("DOWNLOAD_COUNT")
    private Integer downloadCount;

    /**
     * 评分
     */
    @TableField("RATING")
    private Double rating;

    /**
     * 评价次数
     */
    @TableField("RATING_COUNT")
    private Integer ratingCount;

    /**
     * 最后使用时间
     */
    @TableField("LAST_USED_TIME")
    private LocalDateTime lastUsedTime;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 维护人ID
     */
    @TableField("MAINTAINER_ID")
    private String maintainerId;

    /**
     * 维护人姓名
     */
    @TableField("MAINTAINER_NAME")
    private String maintainerName;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private String approverId;

    /**
     * 审批人姓名
     */
    @TableField("APPROVER_NAME")
    private String approverName;

    /**
     * 发布人ID
     */
    @TableField("PUBLISHER_ID")
    private String publisherId;

    /**
     * 发布人姓名
     */
    @TableField("PUBLISHER_NAME")
    private String publisherName;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    /**
     * 生效时间
     */
    @TableField("EFFECTIVE_TIME")
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @TableField("EXPIRY_TIME")
    private LocalDateTime expiryTime;

    /**
     * 归档时间
     */
    @TableField("ARCHIVE_TIME")
    private LocalDateTime archiveTime;

    /**
     * 模板描述
     */
    @TableField("TEMPLATE_DESCRIPTION")
    private String templateDescription;

    /**
     * 使用说明
     */
    @TableField("USAGE_INSTRUCTIONS")
    private String usageInstructions;

    /**
     * 变更日志
     */
    @TableField("CHANGE_LOG")
    private String changeLog;

    /**
     * 标签
     */
    @TableField("TAGS")
    private String tags;

    /**
     * 关键词
     */
    @TableField("KEYWORDS")
    private String keywords;

    /**
     * 文件路径
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 文件名
     */
    @TableField("FILE_NAME")
    private String fileName;

    /**
     * 文件大小
     */
    @TableField("FILE_SIZE")
    private Long fileSize;

    /**
     * 文件类型
     */
    @TableField("FILE_TYPE")
    private String fileType;

    /**
     * 文件MD5
     */
    @TableField("FILE_MD5")
    private String fileMd5;

    /**
     * 缩略图路径
     */
    @TableField("THUMBNAIL_PATH")
    private String thumbnailPath;

    /**
     * 预览图路径
     */
    @TableField("PREVIEW_PATH")
    private String previewPath;

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
     * 是否删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Boolean isDeleted;

    /**
     * 版本号
     */
    @Version
    @TableField("VERSION")
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
     * 模板类型常量
     */
    public static final String TEMPLATE_TYPE_BUDGET_PREPARATION = "budget_preparation";
    public static final String TEMPLATE_TYPE_BUDGET_REPORT = "budget_report";
    public static final String TEMPLATE_TYPE_BUDGET_ANALYSIS = "budget_analysis";
    public static final String TEMPLATE_TYPE_BUDGET_FORM = "budget_form";
    public static final String TEMPLATE_TYPE_WORKFLOW = "workflow";
    public static final String TEMPLATE_TYPE_DASHBOARD = "dashboard";

    /**
     * 模板分类常量
     */
    public static final String TEMPLATE_CATEGORY_STANDARD = "standard";
    public static final String TEMPLATE_CATEGORY_INDUSTRY = "industry";
    public static final String TEMPLATE_CATEGORY_CUSTOM = "custom";
    public static final String TEMPLATE_CATEGORY_SYSTEM = "system";

    /**
     * 模板状态常量
     */
    public static final String TEMPLATE_STATUS_DRAFT = "draft";
    public static final String TEMPLATE_STATUS_ACTIVE = "active";
    public static final String TEMPLATE_STATUS_INACTIVE = "inactive";
    public static final String TEMPLATE_STATUS_ARCHIVED = "archived";

    /**
     * 审批状态常量
     */
    public static final String APPROVAL_STATUS_DRAFT = "draft";
    public static final String APPROVAL_STATUS_SUBMITTED = "submitted";
    public static final String APPROVAL_STATUS_APPROVED = "approved";
    public static final String APPROVAL_STATUS_REJECTED = "rejected";

    /**
     * 发布状态常量
     */
    public static final String PUBLISH_STATUS_UNPUBLISHED = "unpublished";
    public static final String PUBLISH_STATUS_PUBLISHED = "published";
    public static final String PUBLISH_STATUS_ARCHIVED = "archived";
}
