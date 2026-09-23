package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预算体系表
 * 对应表：tbl_eps_budget_system
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_system")
public class EpsBudgetSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 体系ID，主键
     */
    @TableId(value = "system_id", type = IdType.AUTO)
    private Long systemId;

    /**
     * 体系编码，唯一标识
     */
    @TableField("system_code")
    private String systemCode;

    /**
     * 体系名称
     */
    @TableField("system_name")
    private String systemName;

    /**
     * 体系描述
     */
    @TableField("system_description")
    private String systemDescription;

    /**
     * 体系类型：COMPREHENSIVE-综合预算/CAPITAL-资本预算/CASH-现金预算/ROLLING-滚动预算
     */
    @TableField("system_type")
    private String systemType;

    /**
     * 预算年度
     */
    @TableField("fiscal_year")
    private Integer fiscalYear;

    /**
     * 组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @TableField("organization_name")
    private String organizationName;

    /**
     * 组织层级
     */
    @TableField("organization_level")
    private Integer organizationLevel;

    /**
     * 组织路径
     */
    @TableField("organization_path")
    private String organizationPath;

    /**
     * 预算主体类型：LEGAL_ENTITY-法人实体/BUSINESS_UNIT-业务单元/COST_CENTER-成本中心/PROFIT_CENTER-利润中心
     */
    @TableField("entity_type")
    private String entityType;

    /**
     * 预算主体ID
     */
    @TableField("entity_id")
    private Long entityId;

    /**
     * 预算主体名称
     */
    @TableField("entity_name")
    private String entityName;

    /**
     * 预算主体编码
     */
    @TableField("entity_code")
    private String entityCode;

    /**
     * 预算科目体系ID
     */
    @TableField("subject_system_id")
    private Long subjectSystemId;

    /**
     * 预算科目体系名称
     */
    @TableField("subject_system_name")
    private String subjectSystemName;

    /**
     * 维度配置，JSON格式存储
     */
    @TableField("dimension_config")
    private String dimensionConfig;

    /**
     * 版本管理配置，JSON格式存储
     */
    @TableField("version_config")
    private String versionConfig;

    /**
     * 期间配置，JSON格式存储
     */
    @TableField("period_config")
    private String periodConfig;

    /**
     * 币种配置，JSON格式存储
     */
    @TableField("currency_config")
    private String currencyConfig;

    /**
     * 基础币种
     */
    @TableField("base_currency")
    private String baseCurrency;

    /**
     * 是否启用多币种：0-否/1-是
     */
    @TableField("enable_multi_currency")
    private Integer enableMultiCurrency;

    /**
     * 汇率类型：SPOT-即期汇率/FORWARD-远期汇率/AVERAGE-平均汇率
     */
    @TableField("exchange_rate_type")
    private String exchangeRateType;

    /**
     * 预算控制模式：STRICT-严格控制/WARNING-预警控制/NONE-无控制
     */
    @TableField("control_mode")
    private String controlMode;

    /**
     * 预警阈值（百分比）
     */
    @TableField("warning_threshold")
    private BigDecimal warningThreshold;

    /**
     * 控制阈值（百分比）
     */
    @TableField("control_threshold")
    private BigDecimal controlThreshold;

    /**
     * 是否启用实时控制：0-否/1-是
     */
    @TableField("enable_real_time_control")
    private Integer enableRealTimeControl;

    /**
     * 审批流程ID
     */
    @TableField("approval_workflow_id")
    private String approvalWorkflowId;

    /**
     * 是否需要审批：0-否/1-是
     */
    @TableField("require_approval")
    private Integer requireApproval;

    /**
     * 审批级别：DEPARTMENT-部门级/COMPANY-公司级/GROUP-集团级
     */
    @TableField("approval_level")
    private String approvalLevel;

    /**
     * 状态：DRAFT-草稿/ACTIVE-激活/INACTIVE-停用/ARCHIVED-归档
     */
    @TableField("status")
    private String status;

    /**
     * 是否默认体系：0-否/1-是
     */
    @TableField("is_default")
    private Integer isDefault;

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
