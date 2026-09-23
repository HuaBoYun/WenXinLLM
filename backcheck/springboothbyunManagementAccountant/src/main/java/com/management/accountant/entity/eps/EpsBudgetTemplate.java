package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预算模板表
 * 对应表：tbl_eps_budget_template
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_template")
public class EpsBudgetTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID，主键
     */
    @TableId(value = "template_id", type = IdType.AUTO)
    private Long templateId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 模板编码，唯一标识
     */
    @TableField("template_code")
    private String templateCode;

    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 模板描述
     */
    @TableField("template_description")
    private String templateDescription;

    /**
     * 模板类型：STANDARD-标准模板/CUSTOM-自定义模板/INDUSTRY-行业模板
     */
    @TableField("template_type")
    private String templateType;

    /**
     * 模板分类：REVENUE-收入预算/COST-成本预算/EXPENSE-费用预算/CAPEX-资本预算
     */
    @TableField("template_category")
    private String templateCategory;

    /**
     * 适用组织类型：ALL-全部/COMPANY-公司/DEPARTMENT-部门/PROJECT-项目
     */
    @TableField("applicable_org_type")
    private String applicableOrgType;

    /**
     * 适用行业：ALL-全部/MANUFACTURING-制造业/SERVICE-服务业/RETAIL-零售业
     */
    @TableField("applicable_industry")
    private String applicableIndustry;

    /**
     * 模板结构，JSON格式存储
     */
    @TableField("template_structure")
    private String templateStructure;

    /**
     * 表头配置，JSON格式存储
     */
    @TableField("header_config")
    private String headerConfig;

    /**
     * 列配置，JSON格式存储
     */
    @TableField("column_config")
    private String columnConfig;

    /**
     * 行配置，JSON格式存储
     */
    @TableField("row_config")
    private String rowConfig;

    /**
     * 样式配置，JSON格式存储
     */
    @TableField("style_config")
    private String styleConfig;

    /**
     * 验证规则，JSON格式存储
     */
    @TableField("validation_rules")
    private String validationRules;

    /**
     * 计算公式，JSON格式存储
     */
    @TableField("calculation_formulas")
    private String calculationFormulas;

    /**
     * 数据源配置，JSON格式存储
     */
    @TableField("data_source_config")
    private String dataSourceConfig;

    /**
     * 权限配置，JSON格式存储
     */
    @TableField("permission_config")
    private String permissionConfig;

    /**
     * 是否系统模板：0-否/1-是
     */
    @TableField("is_system")
    private Integer isSystem;

    /**
     * 是否可编辑：0-否/1-是
     */
    @TableField("is_editable")
    private Integer isEditable;

    /**
     * 是否共享：0-否/1-是
     */
    @TableField("is_shared")
    private Integer isShared;

    /**
     * 是否默认模板：0-否/1-是
     */
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 版本号
     */
    @TableField("version_number")
    private String versionNumber;

    /**
     * 父模板ID
     */
    @TableField("parent_template_id")
    private Long parentTemplateId;

    /**
     * 模板来源：SYSTEM-系统内置/USER-用户创建/IMPORT-导入
     */
    @TableField("template_source")
    private String templateSource;

    /**
     * 使用次数
     */
    @TableField("usage_count")
    private Integer usageCount;

    /**
     * 最后使用时间
     */
    @TableField("last_used_time")
    private LocalDateTime lastUsedTime;

    /**
     * 标签，多个标签用逗号分隔
     */
    @TableField("tags")
    private String tags;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态：DRAFT-草稿/ACTIVE-激活/INACTIVE-停用/ARCHIVED-归档
     */
    @TableField("status")
    private String status;

    /**
     * 生效日期
     */
    @TableField("effective_date")
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    @TableField("expiry_date")
    private LocalDateTime expiryDate;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
