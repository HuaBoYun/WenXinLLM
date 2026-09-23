package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算科目实体类
 * 
 * @description 预算科目管理实体，支持预算科目的定义、分类和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_ACCOUNT")
public class BudgetAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 科目编码
     */
    @TableField("ACCOUNT_CODE")
    private String accountCode;

    /**
     * 科目名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 科目简称
     */
    @TableField("ACCOUNT_SHORT_NAME")
    private String accountShortName;

    /**
     * 科目类型：REVENUE-收入，EXPENSE-支出，ASSET-资产，LIABILITY-负债，EQUITY-权益
     */
    @TableField("ACCOUNT_TYPE")
    private String accountType;

    /**
     * 科目分类：OPERATING-经营性，INVESTING-投资性，FINANCING-筹资性
     */
    @TableField("ACCOUNT_CATEGORY")
    private String accountCategory;

    /**
     * 科目性质：DEBIT-借方，CREDIT-贷方
     */
    @TableField("ACCOUNT_NATURE")
    private String accountNature;

    /**
     * 科目级别
     */
    @TableField("ACCOUNT_LEVEL")
    private Integer accountLevel;

    /**
     * 父科目ID
     */
    @TableField("PARENT_ACCOUNT_ID")
    private String parentAccountId;

    /**
     * 科目路径
     */
    @TableField("ACCOUNT_PATH")
    private String accountPath;

    /**
     * 是否叶子节点
     */
    @TableField("IS_LEAF")
    private Boolean isLeaf;

    /**
     * 是否允许录入数据
     */
    @TableField("ALLOW_DATA_ENTRY")
    private Boolean allowDataEntry;

    /**
     * 是否必填科目
     */
    @TableField("IS_REQUIRED")
    private Boolean isRequired;

    /**
     * 是否系统科目
     */
    @TableField("IS_SYSTEM")
    private Boolean isSystem;

    /**
     * 科目状态：ACTIVE-激活，INACTIVE-停用，LOCKED-锁定
     */
    @TableField("ACCOUNT_STATUS")
    private String accountStatus;

    /**
     * 计量单位
     */
    @TableField("UNIT_OF_MEASURE")
    private String unitOfMeasure;

    /**
     * 币种
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 汇率类型
     */
    @TableField("EXCHANGE_RATE_TYPE")
    private String exchangeRateType;

    /**
     * 默认汇率
     */
    @TableField("DEFAULT_EXCHANGE_RATE")
    private BigDecimal defaultExchangeRate;

    /**
     * 预算控制方式：NONE-无控制，WARNING-预警，STRICT-严格控制
     */
    @TableField("BUDGET_CONTROL_TYPE")
    private String budgetControlType;

    /**
     * 预警阈值（百分比）
     */
    @TableField("WARNING_THRESHOLD")
    private BigDecimal warningThreshold;

    /**
     * 控制阈值（百分比）
     */
    @TableField("CONTROL_THRESHOLD")
    private BigDecimal controlThreshold;

    /**
     * 科目描述
     */
    @TableField("ACCOUNT_DESCRIPTION")
    private String accountDescription;

    /**
     * 科目用途
     */
    @TableField("ACCOUNT_PURPOSE")
    private String accountPurpose;

    /**
     * 核算规则
     */
    @TableField("ACCOUNTING_RULES")
    private String accountingRules;

    /**
     * 计算公式
     */
    @TableField("CALCULATION_FORMULA")
    private String calculationFormula;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 数据来源配置
     */
    @TableField("DATA_SOURCE_CONFIG")
    private String dataSourceConfig;

    /**
     * 关联会计科目
     */
    @TableField("RELATED_ACCOUNTING_SUBJECT")
    private String relatedAccountingSubject;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

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
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
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

    // ==================== 常量定义 ====================

    /**
     * 科目类型常量
     */
    public static final String ACCOUNT_TYPE_REVENUE = "REVENUE";
    public static final String ACCOUNT_TYPE_EXPENSE = "EXPENSE";
    public static final String ACCOUNT_TYPE_ASSET = "ASSET";
    public static final String ACCOUNT_TYPE_LIABILITY = "LIABILITY";
    public static final String ACCOUNT_TYPE_EQUITY = "EQUITY";

    /**
     * 科目分类常量
     */
    public static final String ACCOUNT_CATEGORY_OPERATING = "OPERATING";
    public static final String ACCOUNT_CATEGORY_INVESTING = "INVESTING";
    public static final String ACCOUNT_CATEGORY_FINANCING = "FINANCING";

    /**
     * 科目性质常量
     */
    public static final String ACCOUNT_NATURE_DEBIT = "DEBIT";
    public static final String ACCOUNT_NATURE_CREDIT = "CREDIT";

    /**
     * 科目状态常量
     */
    public static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
    public static final String ACCOUNT_STATUS_INACTIVE = "INACTIVE";
    public static final String ACCOUNT_STATUS_LOCKED = "LOCKED";

    /**
     * 预算控制方式常量
     */
    public static final String BUDGET_CONTROL_TYPE_NONE = "NONE";
    public static final String BUDGET_CONTROL_TYPE_WARNING = "WARNING";
    public static final String BUDGET_CONTROL_TYPE_STRICT = "STRICT";

    /**
     * 汇率类型常量
     */
    public static final String EXCHANGE_RATE_TYPE_FIXED = "FIXED";
    public static final String EXCHANGE_RATE_TYPE_FLOATING = "FLOATING";
    public static final String EXCHANGE_RATE_TYPE_AVERAGE = "AVERAGE";
}
