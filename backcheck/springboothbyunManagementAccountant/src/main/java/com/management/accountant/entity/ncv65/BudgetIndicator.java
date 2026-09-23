package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算指标实体类
 * 
 * @description 预算指标管理实体，支持8类指标管理
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_INDICATOR")
public class BudgetIndicator implements Serializable {
    /** 补充字段（来源: 调用点签名反推） */
    private boolean isCalculated;



    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 指标编码
     */
    @TableField("INDICATOR_CODE")
    private String indicatorCode;

    /**
     * 指标名称
     */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /**
     * 指标类型：revenue-收入，cost-成本，expense-费用，investment-投资，cash_flow-现金流，operational-经营，strategic-战略，custom-自定义
     */
    @TableField("INDICATOR_TYPE")
    private String indicatorType;

    /**
     * 指标分类
     */
    @TableField("INDICATOR_CATEGORY")
    private String indicatorCategory;

    /**
     * 指标描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 父指标ID
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 指标层级
     */
    @TableField("INDICATOR_LEVEL")
    private Integer indicatorLevel;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 计量单位
     */
    @TableField("UNIT")
    private String unit;

    /**
     * 数据类型：number-数值，percentage-百分比，currency-货币，text-文本
     */
    @TableField("DATA_TYPE")
    private String dataType;

    /**
     * 精度（小数位数）
     */
    @TableField("PRECISION_SCALE")
    private Integer precisionScale;

    /**
     * 是否必填
     */
    @TableField("IS_REQUIRED")
    private Boolean isRequired;

    /**
     * 是否可编辑
     */
    @TableField("IS_EDITABLE")
    private Boolean isEditable;

    /**
     * 是否系统指标
     */
    @TableField("IS_SYSTEM")
    private Boolean isSystem;

    /**
     * 是否汇总指标
     */
    @TableField("IS_SUMMARY")
    private Boolean isSummary;

    /**
     * 汇总方式：sum-求和，avg-平均，max-最大值，min-最小值，count-计数
     */
    @TableField("SUMMARY_METHOD")
    private String summaryMethod;

    /**
     * 计算公式
     */
    @TableField("FORMULA")
    private String formula;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private BigDecimal defaultValue;

    /**
     * 最小值
     */
    @TableField("MIN_VALUE")
    private BigDecimal minValue;

    /**
     * 最大值
     */
    @TableField("MAX_VALUE")
    private BigDecimal maxValue;

    /**
     * 验证规则（JSON格式）
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

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

    // 指标类型常量
    public static final String INDICATOR_TYPE_REVENUE = "revenue";
    public static final String INDICATOR_TYPE_COST = "cost";
    public static final String INDICATOR_TYPE_EXPENSE = "expense";
    public static final String INDICATOR_TYPE_INVESTMENT = "investment";
    public static final String INDICATOR_TYPE_CASH_FLOW = "cash_flow";
    public static final String INDICATOR_TYPE_OPERATIONAL = "operational";
    public static final String INDICATOR_TYPE_STRATEGIC = "strategic";
    public static final String INDICATOR_TYPE_CUSTOM = "custom";

    // 数据类型常量
    public static final String DATA_TYPE_NUMBER = "number";
    public static final String DATA_TYPE_PERCENTAGE = "percentage";
    public static final String DATA_TYPE_CURRENCY = "currency";
    public static final String DATA_TYPE_TEXT = "text";

    // 汇总方式常量
    public static final String SUMMARY_METHOD_SUM = "sum";
    public static final String SUMMARY_METHOD_AVG = "avg";
    public static final String SUMMARY_METHOD_MAX = "max";
    public static final String SUMMARY_METHOD_MIN = "min";
    public static final String SUMMARY_METHOD_COUNT = "count";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
