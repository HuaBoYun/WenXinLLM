package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 内控管理实体类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("INTERNAL_CONTROL")
@Schema(name="InternalControl", description="内控管理实体")
public class InternalControl {

    @Schema(name = "内控ID")
    @TableId(value = "CONTROL_ID", type = IdType.ASSIGN_UUID)
    private String controlId;

    @Schema(name = "企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name = "内控类型")
    @TableField("CONTROL_TYPE")
    private String controlType;

    @Schema(name = "内控名称")
    @TableField("CONTROL_NAME")
    private String controlName;

    @Schema(name = "内控描述")
    @TableField("CONTROL_DESCRIPTION")
    private String controlDescription;

    @Schema(name = "控制目标")
    @TableField("CONTROL_OBJECTIVE")
    private String controlObjective;

    @Schema(name = "控制活动")
    @TableField("CONTROL_ACTIVITIES")
    private String controlActivities;

    @Schema(name = "控制程序")
    @TableField("CONTROL_PROCEDURES")
    private String controlProcedures;

    @Schema(name = "控制频率")
    @TableField("CONTROL_FREQUENCY")
    private String controlFrequency;

    @Schema(name = "控制方式")
    @TableField("CONTROL_METHOD")
    private String controlMethod;

    @Schema(name = "控制层级")
    @TableField("CONTROL_LEVEL")
    private String controlLevel;

    @Schema(name = "适用范围")
    @TableField("APPLICABLE_SCOPE")
    private String applicableScope;

    @Schema(name = "责任部门")
    @TableField("RESPONSIBLE_DEPARTMENT")
    private String responsibleDepartment;

    @Schema(name = "责任人")
    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    @Schema(name = "监督部门")
    @TableField("SUPERVISION_DEPARTMENT")
    private String supervisionDepartment;

    @Schema(name = "监督人")
    @TableField("SUPERVISION_PERSON")
    private String supervisionPerson;

    @Schema(name = "制度状态")
    @TableField("SYSTEM_STATUS")
    private String systemStatus;

    @Schema(name = "制定时间")
    @TableField("FORMULATION_TIME")
    private LocalDateTime formulationTime;

    @Schema(name = "生效时间")
    @TableField("EFFECTIVE_TIME")
    private LocalDateTime effectiveTime;

    @Schema(name = "修订时间")
    @TableField("REVISION_TIME")
    private LocalDateTime revisionTime;

    @Schema(name = "废止时间")
    @TableField("ABOLITION_TIME")
    private LocalDateTime abolitionTime;

    @Schema(name = "执行状态")
    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    @Schema(name = "执行频率")
    @TableField("EXECUTION_FREQUENCY")
    private String executionFrequency;

    @Schema(name = "最后执行时间")
    @TableField("LAST_EXECUTION_TIME")
    private LocalDateTime lastExecutionTime;

    @Schema(name = "下次执行时间")
    @TableField("NEXT_EXECUTION_TIME")
    private LocalDateTime nextExecutionTime;

    @Schema(name = "执行记录")
    @TableField("EXECUTION_RECORDS")
    private String executionRecords;

    @Schema(name = "执行效果")
    @TableField("EXECUTION_EFFECTIVENESS")
    private String executionEffectiveness;

    @Schema(name = "有效性评分")
    @TableField("EFFECTIVENESS_SCORE")
    private BigDecimal effectivenessScore;

    @Schema(name = "合规性评分")
    @TableField("COMPLIANCE_SCORE")
    private BigDecimal complianceScore;

    @Schema(name = "完整性评分")
    @TableField("COMPLETENESS_SCORE")
    private BigDecimal completenessScore;

    @Schema(name = "及时性评分")
    @TableField("TIMELINESS_SCORE")
    private BigDecimal timelinessScore;

    @Schema(name = "综合评分")
    @TableField("OVERALL_SCORE")
    private BigDecimal overallScore;

    @Schema(name = "评估时间")
    @TableField("ASSESSMENT_TIME")
    private LocalDateTime assessmentTime;

    @Schema(name = "评估人")
    @TableField("ASSESSOR")
    private String assessor;

    @Schema(name = "评估结果")
    @TableField("ASSESSMENT_RESULT")
    private String assessmentResult;

    @Schema(name = "发现问题")
    @TableField("IDENTIFIED_ISSUES")
    private String identifiedIssues;

    @Schema(name = "改进建议")
    @TableField("IMPROVEMENT_SUGGESTIONS")
    private String improvementSuggestions;

    @Schema(name = "改进计划")
    @TableField("IMPROVEMENT_PLAN")
    private String improvementPlan;

    @Schema(name = "改进状态")
    @TableField("IMPROVEMENT_STATUS")
    private String improvementStatus;

    @Schema(name = "改进完成时间")
    @TableField("IMPROVEMENT_COMPLETION_TIME")
    private LocalDateTime improvementCompletionTime;

    @Schema(name = "改进效果")
    @TableField("IMPROVEMENT_EFFECTIVENESS")
    private String improvementEffectiveness;

    @Schema(name = "风险等级")
    @TableField("RISK_LEVEL")
    private String riskLevel;

    @Schema(name = "风险描述")
    @TableField("RISK_DESCRIPTION")
    private String riskDescription;

    @Schema(name = "缓解措施")
    @TableField("MITIGATION_MEASURES")
    private String mitigationMeasures;

    @Schema(name = "监控指标")
    @TableField("MONITORING_INDICATORS")
    private String monitoringIndicators;

    @Schema(name = "预警阈值")
    @TableField("WARNING_THRESHOLD")
    private String warningThreshold;

    @Schema(name = "相关制度")
    @TableField("RELATED_SYSTEMS")
    private String relatedSystems;

    @Schema(name = "参考标准")
    @TableField("REFERENCE_STANDARDS")
    private String referenceStandards;

    @Schema(name = "培训要求")
    @TableField("TRAINING_REQUIREMENTS")
    private String trainingRequirements;

    @Schema(name = "培训记录")
    @TableField("TRAINING_RECORDS")
    private String trainingRecords;

    @Schema(name = "文档附件")
    @TableField("DOCUMENT_ATTACHMENTS")
    private String documentAttachments;

    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_BY")
    private String updateBy;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @Schema(name = "删除标志")
    @TableField("DEL_FLAG")
    private String delFlag;
}
