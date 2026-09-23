package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算维度实体类
 * 
 * @description 预算维度管理实体，支持6-20个维度配置
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_DIMENSION")
public class BudgetDimension implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 维度编码
     */
    @TableField("DIMENSION_CODE")
    private String dimensionCode;

    /**
     * 维度名称
     */
    @TableField("DIMENSION_NAME")
    private String dimensionName;

    /**
     * 维度类型：organization-组织，product-产品，project-项目，customer-客户，supplier-供应商，region-地区，time-时间，custom-自定义
     */
    @TableField("DIMENSION_TYPE")
    private String dimensionType;

    /**
     * 维度描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 父维度ID
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 维度层级
     */
    @TableField("DIMENSION_LEVEL")
    private Integer dimensionLevel;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 是否必填
     */
    @TableField("IS_REQUIRED")
    private Boolean isRequired;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 是否系统维度
     */
    @TableField("IS_SYSTEM")
    private Boolean isSystem;

    /**
     * 数据来源：manual-手工录入，import-导入，sync-同步
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 数据来源配置（JSON格式）
     */
    @TableField("DATA_SOURCE_CONFIG")
    private String dataSourceConfig;

    /**
     * 验证规则（JSON格式）
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

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

    // 维度类型常量
    public static final String DIMENSION_TYPE_ORGANIZATION = "organization";
    public static final String DIMENSION_TYPE_PRODUCT = "product";
    public static final String DIMENSION_TYPE_PROJECT = "project";
    public static final String DIMENSION_TYPE_CUSTOMER = "customer";
    public static final String DIMENSION_TYPE_SUPPLIER = "supplier";
    public static final String DIMENSION_TYPE_REGION = "region";
    public static final String DIMENSION_TYPE_TIME = "time";
    public static final String DIMENSION_TYPE_CUSTOM = "custom";

    // 数据来源常量
    public static final String DATA_SOURCE_MANUAL = "manual";
    public static final String DATA_SOURCE_IMPORT = "import";
    public static final String DATA_SOURCE_SYNC = "sync";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
