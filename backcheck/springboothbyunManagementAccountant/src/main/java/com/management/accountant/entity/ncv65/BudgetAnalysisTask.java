package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算分析任务实体类
 * 
 * @description 预算分析任务管理实体，支持多维度分析、差异分析、智能分析
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_ANALYSIS_TASK")
public class BudgetAnalysisTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 分析任务编码
     */
    @TableField("TASK_CODE")
    private String taskCode;

    /**
     * 分析任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 分析类型：multi_dimension-多维度分析，variance-差异分析，trend-趋势分析，intelligent-智能分析
     */
    @TableField("ANALYSIS_TYPE")
    private String analysisType;

    /**
     * 分析描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /**
     * 分析期间
     */
    @TableField("ANALYSIS_PERIOD")
    private String analysisPeriod;

    /**
     * 组织体系ID
     */
    @TableField("STRUCTURE_ID")
    private String structureId;

    /**
     * 分析范围（JSON格式）
     */
    @TableField("ANALYSIS_SCOPE")
    private String analysisScope;

    /**
     * 分析维度配置（JSON格式）
     */
    @TableField("DIMENSION_CONFIG")
    private String dimensionConfig;

    /**
     * 分析指标配置（JSON格式）
     */
    @TableField("INDICATOR_CONFIG")
    private String indicatorConfig;

    /**
     * 分析条件（JSON格式）
     */
    @TableField("ANALYSIS_CONDITIONS")
    private String analysisConditions;

    /**
     * 分析参数（JSON格式）
     */
    @TableField("ANALYSIS_PARAMETERS")
    private String analysisParameters;

    /**
     * 任务状态：draft-草稿，running-运行中，completed-已完成，failed-失败，cancelled-已取消
     */
    @TableField("TASK_STATUS")
    private String taskStatus;

    /**
     * 执行进度（百分比）
     */
    @TableField("PROGRESS")
    private Integer progress;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private LocalDateTime endTime;

    /**
     * 执行耗时（毫秒）
     */
    @TableField("EXECUTION_TIME")
    private Long executionTime;

    /**
     * 分析结果路径
     */
    @TableField("RESULT_PATH")
    private String resultPath;

    /**
     * 分析结果摘要（JSON格式）
     */
    @TableField("RESULT_SUMMARY")
    private String resultSummary;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 是否定时任务
     */
    @TableField("IS_SCHEDULED")
    private Boolean isScheduled;

    /**
     * 定时表达式
     */
    @TableField("CRON_EXPRESSION")
    private String cronExpression;

    /**
     * 下次执行时间
     */
    @TableField("NEXT_EXECUTION_TIME")
    private LocalDateTime nextExecutionTime;

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

    // 分析类型常量
    public static final String ANALYSIS_TYPE_MULTI_DIMENSION = "multi_dimension";
    public static final String ANALYSIS_TYPE_VARIANCE = "variance";
    public static final String ANALYSIS_TYPE_TREND = "trend";
    public static final String ANALYSIS_TYPE_INTELLIGENT = "intelligent";

    // 任务状态常量
    public static final String TASK_STATUS_DRAFT = "draft";
    public static final String TASK_STATUS_RUNNING = "running";
    public static final String TASK_STATUS_COMPLETED = "completed";
    public static final String TASK_STATUS_FAILED = "failed";
    public static final String TASK_STATUS_CANCELLED = "cancelled";

    // 状态常量
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
}
