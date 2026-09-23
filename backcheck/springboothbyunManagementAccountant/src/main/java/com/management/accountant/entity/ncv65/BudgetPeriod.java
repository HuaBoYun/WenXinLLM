package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算期间实体类
 * 
 * @description 预算期间管理实体，支持预算期间的定义、配置和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_PERIOD")
public class BudgetPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 期间编码
     */
    @TableField("PERIOD_CODE")
    private String periodCode;

    /**
     * 期间名称
     */
    @TableField("PERIOD_NAME")
    private String periodName;

    /**
     * 期间类型：YEAR-年度，QUARTER-季度，MONTH-月度，WEEK-周，DAY-日
     */
    @TableField("PERIOD_TYPE")
    private String periodType;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 期间序号
     */
    @TableField("PERIOD_NUMBER")
    private Integer periodNumber;

    /**
     * 开始日期
     */
    @TableField("START_DATE")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    private LocalDateTime endDate;

    /**
     * 期间状态：DRAFT-草稿，ACTIVE-激活，CLOSED-关闭，LOCKED-锁定
     */
    @TableField("PERIOD_STATUS")
    private String periodStatus;

    /**
     * 是否当前期间
     */
    @TableField("IS_CURRENT")
    private Boolean isCurrent;

    /**
     * 是否允许预算编制
     */
    @TableField("ALLOW_BUDGET_PREPARATION")
    private Boolean allowBudgetPreparation;

    /**
     * 是否允许预算调整
     */
    @TableField("ALLOW_BUDGET_ADJUSTMENT")
    private Boolean allowBudgetAdjustment;

    /**
     * 是否允许数据录入
     */
    @TableField("ALLOW_DATA_ENTRY")
    private Boolean allowDataEntry;

    /**
     * 是否允许数据查询
     */
    @TableField("ALLOW_DATA_QUERY")
    private Boolean allowDataQuery;

    /**
     * 父期间ID
     */
    @TableField("PARENT_PERIOD_ID")
    private String parentPeriodId;

    /**
     * 期间层级
     */
    @TableField("PERIOD_LEVEL")
    private Integer periodLevel;

    /**
     * 期间路径
     */
    @TableField("PERIOD_PATH")
    private String periodPath;

    /**
     * 期间描述
     */
    @TableField("PERIOD_DESCRIPTION")
    private String periodDescription;

    /**
     * 期间配置
     */
    @TableField("PERIOD_CONFIG")
    private String periodConfig;

    /**
     * 工作日天数
     */
    @TableField("WORKING_DAYS")
    private Integer workingDays;

    /**
     * 自然日天数
     */
    @TableField("CALENDAR_DAYS")
    private Integer calendarDays;

    /**
     * 期间权重
     */
    @TableField("PERIOD_WEIGHT")
    private Double periodWeight;

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
     * 期间类型常量
     */
    public static final String PERIOD_TYPE_YEAR = "YEAR";
    public static final String PERIOD_TYPE_QUARTER = "QUARTER";
    public static final String PERIOD_TYPE_MONTH = "MONTH";
    public static final String PERIOD_TYPE_WEEK = "WEEK";
    public static final String PERIOD_TYPE_DAY = "DAY";

    /**
     * 期间状态常量
     */
    public static final String PERIOD_STATUS_DRAFT = "DRAFT";
    public static final String PERIOD_STATUS_ACTIVE = "ACTIVE";
    public static final String PERIOD_STATUS_CLOSED = "CLOSED";
    public static final String PERIOD_STATUS_LOCKED = "LOCKED";

    /**
     * 期间层级常量
     */
    public static final Integer PERIOD_LEVEL_YEAR = 1;
    public static final Integer PERIOD_LEVEL_QUARTER = 2;
    public static final Integer PERIOD_LEVEL_MONTH = 3;
    public static final Integer PERIOD_LEVEL_WEEK = 4;
    public static final Integer PERIOD_LEVEL_DAY = 5;
}
