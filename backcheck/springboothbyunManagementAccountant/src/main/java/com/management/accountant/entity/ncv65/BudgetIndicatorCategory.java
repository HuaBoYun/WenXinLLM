package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算指标分类实体类
 * 
 * @description 预算指标分类管理实体，支持指标的分类管理和层级结构
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_INDICATOR_CATEGORY")
public class BudgetIndicatorCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 分类编码
     */
    @TableField("CATEGORY_CODE")
    private String categoryCode;

    /**
     * 分类名称
     */
    @TableField("CATEGORY_NAME")
    private String categoryName;

    /**
     * 分类简称
     */
    @TableField("CATEGORY_SHORT_NAME")
    private String categoryShortName;

    /**
     * 父分类ID
     */
    @TableField("PARENT_CATEGORY_ID")
    private String parentCategoryId;

    /**
     * 分类层级
     */
    @TableField("CATEGORY_LEVEL")
    private Integer categoryLevel;

    /**
     * 分类路径（用于快速查询层级关系）
     */
    @TableField("CATEGORY_PATH")
    private String categoryPath;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 分类类型：income-收入类，expense-支出类，asset-资产类，liability-负债类，equity-权益类
     */
    @TableField("CATEGORY_TYPE")
    private String categoryType;

    /**
     * 业务类型：operational-经营性，investment-投资性，financing-筹资性
     */
    @TableField("BUSINESS_TYPE")
    private String businessType;

    /**
     * 分类描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 分类属性（JSON格式）
     */
    @TableField("CATEGORY_ATTRIBUTES")
    private String categoryAttributes;

    /**
     * 计算规则（JSON格式）
     */
    @TableField("CALCULATION_RULES")
    private String calculationRules;

    /**
     * 汇总规则（JSON格式）
     */
    @TableField("AGGREGATION_RULES")
    private String aggregationRules;

    /**
     * 控制规则（JSON格式）
     */
    @TableField("CONTROL_RULES")
    private String controlRules;

    /**
     * 审批规则（JSON格式）
     */
    @TableField("APPROVAL_RULES")
    private String approvalRules;

    /**
     * 是否叶子节点
     */
    @TableField("IS_LEAF")
    private Boolean isLeaf;

    /**
     * 是否允许录入数据
     */
    @TableField("IS_DATA_ENTRY_ALLOWED")
    private Boolean isDataEntryAllowed;

    /**
     * 是否系统预置
     */
    @TableField("IS_SYSTEM")
    private Boolean isSystem;

    /**
     * 是否可编辑
     */
    @TableField("IS_EDITABLE")
    private Boolean isEditable;

    /**
     * 是否可删除
     */
    @TableField("IS_DELETABLE")
    private Boolean isDeletable;

    /**
     * 图标
     */
    @TableField("ICON")
    private String icon;

    /**
     * 颜色
     */
    @TableField("COLOR")
    private String color;

    /**
     * 样式配置（JSON格式）
     */
    @TableField("STYLE_CONFIG")
    private String styleConfig;

    /**
     * 权限配置（JSON格式）
     */
    @TableField("PERMISSION_CONFIG")
    private String permissionConfig;

    /**
     * 数据来源：manual-手工录入，import-导入，sync-同步
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 外部系统编码
     */
    @TableField("EXTERNAL_CODE")
    private String externalCode;

    /**
     * 外部系统ID
     */
    @TableField("EXTERNAL_ID")
    private String externalId;

    /**
     * 映射规则（JSON格式）
     */
    @TableField("MAPPING_RULES")
    private String mappingRules;

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
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用，expired-已过期
     */
    @TableField("STATUS")
    private String status;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

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

    // 常量定义
    public static final String CATEGORY_TYPE_INCOME = "income";
    public static final String CATEGORY_TYPE_EXPENSE = "expense";
    public static final String CATEGORY_TYPE_ASSET = "asset";
    public static final String CATEGORY_TYPE_LIABILITY = "liability";
    public static final String CATEGORY_TYPE_EQUITY = "equity";

    public static final String BUSINESS_TYPE_OPERATIONAL = "operational";
    public static final String BUSINESS_TYPE_INVESTMENT = "investment";
    public static final String BUSINESS_TYPE_FINANCING = "financing";

    public static final String DATA_SOURCE_MANUAL = "manual";
    public static final String DATA_SOURCE_IMPORT = "import";
    public static final String DATA_SOURCE_SYNC = "sync";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_EXPIRED = "expired";
}
