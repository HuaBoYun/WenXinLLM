package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算模型实体类
 * 
 * @description 预算模型管理实体，支持7类预算模型
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_MODEL")
public class BudgetModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 模型编码
     */
    @TableField("MODEL_CODE")
    private String modelCode;

    /**
     * 模型名称
     */
    @TableField("MODEL_NAME")
    private String modelName;

    /**
     * 模型类型：revenue-收入，cost-成本，expense-费用，investment-投资，cash-现金，comprehensive-综合，custom-自定义
     */
    @TableField("MODEL_TYPE")
    private String modelType;

    /**
     * 模型分类
     */
    @TableField("MODEL_CATEGORY")
    private String modelCategory;

    /**
     * 模型描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 组织体系ID
     */
    @TableField("STRUCTURE_ID")
    private String structureId;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /**
     * 预算期间类型：monthly-月度，quarterly-季度，yearly-年度
     */
    @TableField("PERIOD_TYPE")
    private String periodType;

    /**
     * 模型配置（JSON格式）
     */
    @TableField("MODEL_CONFIG")
    private String modelConfig;

    /**
     * 维度配置（JSON格式）
     */
    @TableField("DIMENSION_CONFIG")
    private String dimensionConfig;

    /**
     * 指标配置（JSON格式）
     */
    @TableField("INDICATOR_CONFIG")
    private String indicatorConfig;

    /**
     * 公式配置（JSON格式）
     */
    @TableField("FORMULA_CONFIG")
    private String formulaConfig;

    /**
     * 审批流程ID
     */
    @TableField("WORKFLOW_ID")
    private String workflowId;

    /**
     * 模型状态：draft-草稿，published-已发布，active-激活，inactive-停用，archived-已归档
     */
    @TableField("MODEL_STATUS")
    private String modelStatus;

    /**
     * 版本号
     */
    @TableField("MODEL_VERSION")
    private String modelVersion;

    /**
     * 父模型ID（用于版本管理）
     */
    @TableField("PARENT_MODEL_ID")
    private String parentModelId;

    /**
     * 是否当前版本
     */
    @TableField("IS_CURRENT_VERSION")
    private Boolean isCurrentVersion;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    @TableField("PUBLISH_BY")
    private String publishBy;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDateTime expiryDate;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用
     */
    @TableField("STATUS")
    private String status;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

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

    // 模型类型常量
    public static final String MODEL_TYPE_REVENUE = "revenue";
    public static final String MODEL_TYPE_COST = "cost";
    public static final String MODEL_TYPE_EXPENSE = "expense";
    public static final String MODEL_TYPE_INVESTMENT = "investment";
    public static final String MODEL_TYPE_CASH = "cash";
    public static final String MODEL_TYPE_COMPREHENSIVE = "comprehensive";
    public static final String MODEL_TYPE_CUSTOM = "custom";

    // 期间类型常量
    public static final String PERIOD_TYPE_MONTHLY = "monthly";
    public static final String PERIOD_TYPE_QUARTERLY = "quarterly";
    public static final String PERIOD_TYPE_YEARLY = "yearly";

    // 模型状态常量
    public static final String MODEL_STATUS_DRAFT = "draft";
    public static final String MODEL_STATUS_PUBLISHED = "published";
    public static final String MODEL_STATUS_ACTIVE = "active";
    public static final String MODEL_STATUS_INACTIVE = "inactive";
    public static final String MODEL_STATUS_ARCHIVED = "archived";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
