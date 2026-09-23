package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预算科目表
 * 对应表：tbl_eps_budget_subject
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_subject")
public class EpsBudgetSubject implements Serializable {
    /** 补充字段（来源: 调用点签名反推） */
    private boolean isEnabled;



    private static final long serialVersionUID = 1L;

    /**
     * 科目ID，主键
     */
    @TableId(value = "subject_id", type = IdType.AUTO)
    private Long subjectId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 科目编码，唯一标识
     */
    @TableField("subject_code")
    private String subjectCode;

    /**
     * 科目名称
     */
    @TableField("subject_name")
    private String subjectName;

    /**
     * 科目描述
     */
    @TableField("subject_description")
    private String subjectDescription;

    /**
     * 科目类型：REVENUE-收入/COST-成本/EXPENSE-费用/ASSET-资产/LIABILITY-负债/EQUITY-权益
     */
    @TableField("subject_type")
    private String subjectType;

    /**
     * 科目分类：OPERATING-经营性/INVESTING-投资性/FINANCING-筹资性
     */
    @TableField("subject_category")
    private String subjectCategory;

    /**
     * 父科目ID
     */
    @TableField("parent_subject_id")
    private Long parentSubjectId;

    /**
     * 科目层级
     */
    @TableField("subject_level")
    private Integer subjectLevel;

    /**
     * 科目路径
     */
    @TableField("subject_path")
    private String subjectPath;

    /**
     * 是否叶子节点：0-否/1-是
     */
    @TableField("is_leaf")
    private Integer isLeaf;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 计量单位
     */
    @TableField("unit_of_measure")
    private String unitOfMeasure;

    /**
     * 数据类型：AMOUNT-金额/QUANTITY-数量/RATE-比率
     */
    @TableField("data_type")
    private String dataType;

    /**
     * 精度（小数位数）
     */
    @TableField("decimal_places")
    private Integer decimalPlaces;

    /**
     * 是否允许负值：0-否/1-是
     */
    @TableField("allow_negative")
    private Integer allowNegative;

    /**
     * 默认值
     */
    @TableField("default_value")
    private BigDecimal defaultValue;

    /**
     * 计算公式
     */
    @TableField("calculation_formula")
    private String calculationFormula;

    /**
     * 是否系统科目：0-否/1-是
     */
    @TableField("is_system")
    private Integer isSystem;

    /**
     * 是否可编辑：0-否/1-是
     */
    @TableField("is_editable")
    private Integer isEditable;

    /**
     * 是否必填：0-否/1-是
     */
    @TableField("is_required")
    private Integer isRequired;

    /**
     * 控制规则，JSON格式存储
     */
    @TableField("control_rules")
    private String controlRules;

    /**
     * 验证规则，JSON格式存储
     */
    @TableField("validation_rules")
    private String validationRules;

    /**
     * 关联会计科目
     */
    @TableField("accounting_subject")
    private String accountingSubject;

    /**
     * 关联统计科目
     */
    @TableField("statistical_subject")
    private String statisticalSubject;

    /**
     * 数据来源：MANUAL-手工录入/SYSTEM-系统计算/IMPORT-导入
     */
    @TableField("data_source")
    private String dataSource;

    /**
     * 数据来源配置，JSON格式存储
     */
    @TableField("data_source_config")
    private String dataSourceConfig;

    /**
     * 审批要求：NONE-无需审批/OPTIONAL-可选审批/REQUIRED-必须审批
     */
    @TableField("approval_requirement")
    private String approvalRequirement;

    /**
     * 审批阈值
     */
    @TableField("approval_threshold")
    private BigDecimal approvalThreshold;

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
