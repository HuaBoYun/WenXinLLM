package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算合并实体类
 * 
 * @description 预算合并管理，支持多组织、多层级的预算数据合并和汇总
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_CONSOLIDATION")
public class BudgetConsolidation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合并编码
     */
    @TableField("CONSOLIDATION_CODE")
    private String consolidationCode;

    /**
     * 合并名称
     */
    @TableField("CONSOLIDATION_NAME")
    private String consolidationName;

    /**
     * 合并类型
     */
    @TableField("CONSOLIDATION_TYPE")
    private String consolidationType;

    /**
     * 合并范围
     */
    @TableField("CONSOLIDATION_SCOPE")
    private String consolidationScope;

    /**
     * 合并方法
     */
    @TableField("CONSOLIDATION_METHOD")
    private String consolidationMethod;

    /**
     * 合并规则
     */
    @TableField("CONSOLIDATION_RULES")
    private String consolidationRules;

    /**
     * 预算年度
     */
    @TableField("FISCAL_YEAR")
    private Integer fiscalYear;

    /**
     * 合并期间
     */
    @TableField("CONSOLIDATION_PERIOD")
    private String consolidationPeriod;

    /**
     * 父组织ID
     */
    @TableField("PARENT_ORGANIZATION_ID")
    private String parentOrganizationId;

    /**
     * 父组织名称
     */
    @TableField("PARENT_ORGANIZATION_NAME")
    private String parentOrganizationName;

    /**
     * 子组织ID列表
     */
    @TableField("CHILD_ORGANIZATION_IDS")
    private String childOrganizationIds;

    /**
     * 子组织名称列表
     */
    @TableField("CHILD_ORGANIZATION_NAMES")
    private String childOrganizationNames;

    /**
     * 合并层级
     */
    @TableField("CONSOLIDATION_LEVEL")
    private Integer consolidationLevel;

    /**
     * 合并路径
     */
    @TableField("CONSOLIDATION_PATH")
    private String consolidationPath;

    /**
     * 指标ID
     */
    @TableField("INDICATOR_ID")
    private String indicatorId;

    /**
     * 指标名称
     */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /**
     * 指标分类ID
     */
    @TableField("INDICATOR_CATEGORY_ID")
    private String indicatorCategoryId;

    /**
     * 合并前金额
     */
    @TableField("BEFORE_AMOUNT")
    private BigDecimal beforeAmount;

    /**
     * 合并后金额
     */
    @TableField("AFTER_AMOUNT")
    private BigDecimal afterAmount;

    /**
     * 调整金额
     */
    @TableField("ADJUSTMENT_AMOUNT")
    private BigDecimal adjustmentAmount;

    /**
     * 抵消金额
     */
    @TableField("ELIMINATION_AMOUNT")
    private BigDecimal eliminationAmount;

    /**
     * 合并比例
     */
    @TableField("CONSOLIDATION_RATIO")
    private BigDecimal consolidationRatio;

    /**
     * 权重系数
     */
    @TableField("WEIGHT_FACTOR")
    private BigDecimal weightFactor;

    /**
     * 合并状态
     */
    @TableField("CONSOLIDATION_STATUS")
    private String consolidationStatus;

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
     * 合并时间
     */
    @TableField("CONSOLIDATION_TIME")
    private LocalDateTime consolidationTime;

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
     * 合并人ID
     */
    @TableField("CONSOLIDATOR_ID")
    private String consolidatorId;

    /**
     * 合并人姓名
     */
    @TableField("CONSOLIDATOR_NAME")
    private String consolidatorName;

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
     * 是否自动合并
     */
    @TableField("IS_AUTO_CONSOLIDATION")
    private Boolean isAutoConsolidation;

    /**
     * 是否实时合并
     */
    @TableField("IS_REAL_TIME")
    private Boolean isRealTime;

    /**
     * 是否包含抵消分录
     */
    @TableField("IS_INCLUDE_ELIMINATION")
    private Boolean isIncludeElimination;

    /**
     * 是否启用版本控制
     */
    @TableField("IS_VERSION_CONTROL")
    private Boolean isVersionControl;

    /**
     * 合并版本号
     */
    @TableField("VERSION_NUMBER")
    private String versionNumber;

    /**
     * 基准版本ID
     */
    @TableField("BASE_VERSION_ID")
    private String baseVersionId;

    /**
     * 合并频率
     */
    @TableField("CONSOLIDATION_FREQUENCY")
    private String consolidationFrequency;

    /**
     * 下次合并时间
     */
    @TableField("NEXT_CONSOLIDATION_TIME")
    private LocalDateTime nextConsolidationTime;

    /**
     * 合并结果
     */
    @TableField("CONSOLIDATION_RESULT")
    private String consolidationResult;

    /**
     * 合并日志
     */
    @TableField("CONSOLIDATION_LOG")
    private String consolidationLog;

    /**
     * 异常信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 数据质量评分
     */
    @TableField("DATA_QUALITY_SCORE")
    private BigDecimal dataQualityScore;

    /**
     * 完整性检查结果
     */
    @TableField("INTEGRITY_CHECK_RESULT")
    private String integrityCheckResult;

    /**
     * 一致性检查结果
     */
    @TableField("CONSISTENCY_CHECK_RESULT")
    private String consistencyCheckResult;

    /**
     * 合并配置
     */
    @TableField("CONSOLIDATION_CONFIG")
    private String consolidationConfig;

    /**
     * 抵消规则配置
     */
    @TableField("ELIMINATION_RULES")
    private String eliminationRules;

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
     * 合并类型常量
     */
    public static final String CONSOLIDATION_TYPE_FULL = "full";
    public static final String CONSOLIDATION_TYPE_PARTIAL = "partial";
    public static final String CONSOLIDATION_TYPE_PROPORTIONAL = "proportional";
    public static final String CONSOLIDATION_TYPE_EQUITY = "equity";

    /**
     * 合并范围常量
     */
    public static final String CONSOLIDATION_SCOPE_ALL = "all";
    public static final String CONSOLIDATION_SCOPE_SELECTED = "selected";
    public static final String CONSOLIDATION_SCOPE_LEVEL = "level";
    public static final String CONSOLIDATION_SCOPE_CATEGORY = "category";

    /**
     * 合并方法常量
     */
    public static final String CONSOLIDATION_METHOD_SUM = "sum";
    public static final String CONSOLIDATION_METHOD_AVERAGE = "average";
    public static final String CONSOLIDATION_METHOD_WEIGHTED = "weighted";
    public static final String CONSOLIDATION_METHOD_FORMULA = "formula";

    /**
     * 合并状态常量
     */
    public static final String CONSOLIDATION_STATUS_PENDING = "pending";
    public static final String CONSOLIDATION_STATUS_PROCESSING = "processing";
    public static final String CONSOLIDATION_STATUS_COMPLETED = "completed";
    public static final String CONSOLIDATION_STATUS_FAILED = "failed";

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

    /**
     * 合并频率常量
     */
    public static final String CONSOLIDATION_FREQUENCY_REAL_TIME = "real_time";
    public static final String CONSOLIDATION_FREQUENCY_DAILY = "daily";
    public static final String CONSOLIDATION_FREQUENCY_WEEKLY = "weekly";
    public static final String CONSOLIDATION_FREQUENCY_MONTHLY = "monthly";
    public static final String CONSOLIDATION_FREQUENCY_QUARTERLY = "quarterly";
    public static final String CONSOLIDATION_FREQUENCY_YEARLY = "yearly";
    public static final String CONSOLIDATION_FREQUENCY_MANUAL = "manual";
}
