package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据质量管理实体类
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_eps_data_quality")
@ApiModel(value = "EpsDataQuality对象", description = "数据质量管理")
public class EpsDataQuality {

    @ApiModelProperty(value = "数据质量ID，主键")
    @TableId(value = "quality_id", type = IdType.ASSIGN_ID)
    private String qualityId;

    @ApiModelProperty(value = "质量编码，唯一标识")
    @TableField("quality_code")
    private String qualityCode;

    @ApiModelProperty(value = "质量名称")
    @TableField("quality_name")
    private String qualityName;

    @ApiModelProperty(value = "质量类型：COMPLETENESS-完整性/ACCURACY-准确性/CONSISTENCY-一致性/VALIDITY-有效性/UNIQUENESS-唯一性/TIMELINESS-及时性")
    @TableField("quality_type")
    private String qualityType;

    @ApiModelProperty(value = "质量分类：DATA-数据质量/PROCESS-流程质量/SYSTEM-系统质量")
    @TableField("quality_category")
    private String qualityCategory;

    @ApiModelProperty(value = "质量模块：BUDGET-预算/REPORT-报表/ANALYSIS-分析")
    @TableField("quality_module")
    private String qualityModule;

    @ApiModelProperty(value = "数据源ID")
    @TableField("data_source_id")
    private String dataSourceId;

    @ApiModelProperty(value = "数据源编码")
    @TableField("data_source_code")
    private String dataSourceCode;

    @ApiModelProperty(value = "数据源名称")
    @TableField("data_source_name")
    private String dataSourceName;

    @ApiModelProperty(value = "数据源类型：DATABASE-数据库/FILE-文件/API-接口/MANUAL-手工")
    @TableField("data_source_type")
    private String dataSourceType;

    @ApiModelProperty(value = "数据表名")
    @TableField("table_name")
    private String tableName;

    @ApiModelProperty(value = "数据字段名")
    @TableField("field_name")
    private String fieldName;

    @ApiModelProperty(value = "质量规则ID")
    @TableField("rule_id")
    private String ruleId;

    @ApiModelProperty(value = "质量规则编码")
    @TableField("rule_code")
    private String ruleCode;

    @ApiModelProperty(value = "质量规则名称")
    @TableField("rule_name")
    private String ruleName;

    @ApiModelProperty(value = "质量规则类型：VALIDATION-验证规则/CLEANSING-清洗规则/TRANSFORMATION-转换规则")
    @TableField("rule_type")
    private String ruleType;

    @ApiModelProperty(value = "质量规则表达式")
    @TableField("rule_expression")
    private String ruleExpression;

    @ApiModelProperty(value = "质量规则描述")
    @TableField("rule_description")
    private String ruleDescription;

    @ApiModelProperty(value = "检查时间")
    @TableField("check_time")
    private LocalDateTime checkTime;

    @ApiModelProperty(value = "检查类型：MANUAL-手工检查/AUTO-自动检查/SCHEDULED-定时检查")
    @TableField("check_type")
    private String checkType;

    @ApiModelProperty(value = "检查状态：PENDING-待检查/CHECKING-检查中/COMPLETED-已完成/FAILED-检查失败")
    @TableField("check_status")
    private String checkStatus;

    @ApiModelProperty(value = "检查结果：PASS-通过/FAIL-失败/WARNING-警告")
    @TableField("check_result")
    private String checkResult;

    @ApiModelProperty(value = "检查总数")
    @TableField("total_count")
    private Integer totalCount;

    @ApiModelProperty(value = "通过数量")
    @TableField("pass_count")
    private Integer passCount;

    @ApiModelProperty(value = "失败数量")
    @TableField("fail_count")
    private Integer failCount;

    @ApiModelProperty(value = "警告数量")
    @TableField("warning_count")
    private Integer warningCount;

    @ApiModelProperty(value = "质量得分")
    @TableField("quality_score")
    private BigDecimal qualityScore;

    @ApiModelProperty(value = "质量等级：EXCELLENT-优秀/GOOD-良好/FAIR-一般/POOR-较差")
    @TableField("quality_level")
    private String qualityLevel;

    @ApiModelProperty(value = "问题数量")
    @TableField("issue_count")
    private Integer issueCount;

    @ApiModelProperty(value = "问题描述")
    @TableField("issue_description")
    private String issueDescription;

    @ApiModelProperty(value = "问题类型：MISSING-缺失/DUPLICATE-重复/INVALID-无效/INCONSISTENT-不一致")
    @TableField("issue_type")
    private String issueType;

    @ApiModelProperty(value = "问题严重程度：HIGH-高/MEDIUM-中/LOW-低")
    @TableField("issue_severity")
    private String issueSeverity;

    @ApiModelProperty(value = "修复状态：PENDING-待修复/FIXING-修复中/FIXED-已修复/IGNORED-已忽略")
    @TableField("fix_status")
    private String fixStatus;

    @ApiModelProperty(value = "修复方案")
    @TableField("fix_solution")
    private String fixSolution;

    @ApiModelProperty(value = "修复时间")
    @TableField("fix_time")
    private LocalDateTime fixTime;

    @ApiModelProperty(value = "修复人员ID")
    @TableField("fix_user_id")
    private String fixUserId;

    @ApiModelProperty(value = "修复人员姓名")
    @TableField("fix_user_name")
    private String fixUserName;

    @ApiModelProperty(value = "检查开始时间")
    @TableField("check_start_time")
    private LocalDateTime checkStartTime;

    @ApiModelProperty(value = "检查结束时间")
    @TableField("check_end_time")
    private LocalDateTime checkEndTime;

    @ApiModelProperty(value = "检查耗时（秒）")
    @TableField("check_duration")
    private Integer checkDuration;

    @ApiModelProperty(value = "检查人员ID")
    @TableField("check_user_id")
    private String checkUserId;

    @ApiModelProperty(value = "检查人员姓名")
    @TableField("check_user_name")
    private String checkUserName;

    @ApiModelProperty(value = "检查批次号")
    @TableField("check_batch_no")
    private String checkBatchNo;

    @ApiModelProperty(value = "检查配置JSON")
    @TableField("check_config")
    private String checkConfig;

    @ApiModelProperty(value = "检查参数JSON")
    @TableField("check_params")
    private String checkParams;

    @ApiModelProperty(value = "检查日志")
    @TableField("check_log")
    private String checkLog;

    @ApiModelProperty(value = "检查报告路径")
    @TableField("report_path")
    private String reportPath;

    @ApiModelProperty(value = "检查报告格式：PDF/EXCEL/HTML/JSON")
    @TableField("report_format")
    private String reportFormat;

    @ApiModelProperty(value = "是否自动修复")
    @TableField("auto_fix")
    private Boolean autoFix;

    @ApiModelProperty(value = "是否发送通知")
    @TableField("send_notification")
    private Boolean sendNotification;

    @ApiModelProperty(value = "通知方式：EMAIL-邮件/SMS-短信/SYSTEM-系统通知")
    @TableField("notification_type")
    private String notificationType;

    @ApiModelProperty(value = "通知接收人")
    @TableField("notification_recipients")
    private String notificationRecipients;

    @ApiModelProperty(value = "业务类型")
    @TableField("business_type")
    private String businessType;

    @ApiModelProperty(value = "业务模块")
    @TableField("business_module")
    private String businessModule;

    @ApiModelProperty(value = "业务场景")
    @TableField("business_scenario")
    private String businessScenario;

    @ApiModelProperty(value = "业务数据JSON")
    @TableField("business_data")
    private String businessData;

    @ApiModelProperty(value = "业务ID")
    @TableField("business_id")
    private String businessId;

    @ApiModelProperty(value = "业务编码")
    @TableField("business_code")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    @TableField("business_name")
    private String businessName;

    @ApiModelProperty(value = "标签")
    @TableField("tags")
    private String tags;

    @ApiModelProperty(value = "分组名称")
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty(value = "批次号")
    @TableField("batch_number")
    private String batchNumber;

    @ApiModelProperty(value = "质量指标JSON")
    @TableField("quality_metrics")
    private String qualityMetrics;

    @ApiModelProperty(value = "基准值")
    @TableField("baseline_value")
    private BigDecimal baselineValue;

    @ApiModelProperty(value = "目标值")
    @TableField("target_value")
    private BigDecimal targetValue;

    @ApiModelProperty(value = "阈值")
    @TableField("threshold_value")
    private BigDecimal thresholdValue;

    @ApiModelProperty(value = "趋势：IMPROVING-改善/STABLE-稳定/DECLINING-下降")
    @TableField("trend")
    private String trend;

    @ApiModelProperty(value = "排序")
    @TableField("sort_order")
    private Integer sortOrder;

    @ApiModelProperty(value = "状态：ACTIVE-活跃/INACTIVE-非活跃")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "扩展属性JSON")
    @TableField("extended_attributes")
    private String extendedAttributes;

    @ApiModelProperty(value = "租户ID")
    @TableField("tenant_id")
    private String tenantId;

    @ApiModelProperty(value = "创建人ID")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @ApiModelProperty(value = "创建人姓名")
    @TableField(value = "created_by_name", fill = FieldFill.INSERT)
    private String createdByName;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    @ApiModelProperty(value = "更新人姓名")
    @TableField(value = "updated_by_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedByName;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "版本号")
    @Version
    @TableField("version")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除标志")
    @TableLogic
    @TableField("deleted")
    private Boolean deleted;
}
