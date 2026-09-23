package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务报表编制流程实体类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FINANCIAL_STATEMENT_PROCESS")
@Schema(name="FinancialStatementProcess", description="财务报表编制流程实体")
public class FinancialStatementProcess {

    @Schema(name = "流程ID")
    @TableId(value = "PROCESS_ID", type = IdType.ASSIGN_UUID)
    private String processId;

    @Schema(name = "企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name = "报表类型")
    @TableField("STATEMENT_TYPE")
    private String statementType;

    @Schema(name = "报表名称")
    @TableField("STATEMENT_NAME")
    private String statementName;

    @Schema(name = "报告期间")
    @TableField("REPORTING_PERIOD")
    private String reportingPeriod;

    @Schema(name = "报告年度")
    @TableField("REPORTING_YEAR")
    private Integer reportingYear;

    @Schema(name = "报告月份")
    @TableField("REPORTING_MONTH")
    private Integer reportingMonth;

    @Schema(name = "编制计划ID")
    @TableField("COMPILATION_PLAN_ID")
    private String compilationPlanId;

    @Schema(name = "编制状态")
    @TableField("COMPILATION_STATUS")
    private String compilationStatus;

    @Schema(name = "编制进度")
    @TableField("COMPILATION_PROGRESS")
    private BigDecimal compilationProgress;

    @Schema(name = "计划开始时间")
    @TableField("PLANNED_START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedStartTime;

    @Schema(name = "计划结束时间")
    @TableField("PLANNED_END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedEndTime;

    @Schema(name = "实际开始时间")
    @TableField("ACTUAL_START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualStartTime;

    @Schema(name = "实际结束时间")
    @TableField("ACTUAL_END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndTime;

    @Schema(name = "编制负责人")
    @TableField("COMPILATION_MANAGER")
    private String compilationManager;

    @Schema(name = "编制部门")
    @TableField("COMPILATION_DEPARTMENT")
    private String compilationDepartment;

    @Schema(name = "参与人员")
    @TableField("PARTICIPANTS")
    private String participants;

    @Schema(name = "数据来源")
    @TableField("DATA_SOURCES")
    private String dataSources;

    @Schema(name = "数据采集状态")
    @TableField("DATA_COLLECTION_STATUS")
    private String dataCollectionStatus;

    @Schema(name = "数据采集进度")
    @TableField("DATA_COLLECTION_PROGRESS")
    private BigDecimal dataCollectionProgress;

    @Schema(name = "数据质量评分")
    @TableField("DATA_QUALITY_SCORE")
    private BigDecimal dataQualityScore;

    @Schema(name = "审核状态")
    @TableField("AUDIT_STATUS")
    private String auditStatus;

    @Schema(name = "审核进度")
    @TableField("AUDIT_PROGRESS")
    private BigDecimal auditProgress;

    @Schema(name = "初审人")
    @TableField("FIRST_AUDITOR")
    private String firstAuditor;

    @Schema(name = "初审时间")
    @TableField("FIRST_AUDIT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstAuditTime;

    @Schema(name = "初审意见")
    @TableField("FIRST_AUDIT_OPINION")
    private String firstAuditOpinion;

    @Schema(name = "复审人")
    @TableField("SECOND_AUDITOR")
    private String secondAuditor;

    @Schema(name = "复审时间")
    @TableField("SECOND_AUDIT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime secondAuditTime;

    @Schema(name = "复审意见")
    @TableField("SECOND_AUDIT_OPINION")
    private String secondAuditOpinion;

    @Schema(name = "终审人")
    @TableField("FINAL_AUDITOR")
    private String finalAuditor;

    @Schema(name = "终审时间")
    @TableField("FINAL_AUDIT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finalAuditTime;

    @Schema(name = "终审意见")
    @TableField("FINAL_AUDIT_OPINION")
    private String finalAuditOpinion;

    @Schema(name = "签字确认人")
    @TableField("SIGNATORY")
    private String signatory;

    @Schema(name = "签字确认时间")
    @TableField("SIGNATURE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signatureTime;

    @Schema(name = "质量控制状态")
    @TableField("QUALITY_CONTROL_STATUS")
    private String qualityControlStatus;

    @Schema(name = "质量评估结果")
    @TableField("QUALITY_ASSESSMENT_RESULT")
    private String qualityAssessmentResult;

    @Schema(name = "准确性评分")
    @TableField("ACCURACY_SCORE")
    private BigDecimal accuracyScore;

    @Schema(name = "完整性评分")
    @TableField("COMPLETENESS_SCORE")
    private BigDecimal completenessScore;

    @Schema(name = "及时性评分")
    @TableField("TIMELINESS_SCORE")
    private BigDecimal timelinessScore;

    @Schema(name = "合规性评分")
    @TableField("COMPLIANCE_SCORE")
    private BigDecimal complianceScore;

    @Schema(name = "综合质量评分")
    @TableField("OVERALL_QUALITY_SCORE")
    private BigDecimal overallQualityScore;

    @Schema(name = "发现问题")
    @TableField("IDENTIFIED_ISSUES")
    private String identifiedIssues;

    @Schema(name = "问题数量")
    @TableField("ISSUE_COUNT")
    private Integer issueCount;

    @Schema(name = "严重问题数量")
    @TableField("CRITICAL_ISSUE_COUNT")
    private Integer criticalIssueCount;

    @Schema(name = "问题处理状态")
    @TableField("ISSUE_RESOLUTION_STATUS")
    private String issueResolutionStatus;

    @Schema(name = "整改措施")
    @TableField("CORRECTIVE_MEASURES")
    private String correctiveMeasures;

    @Schema(name = "整改完成时间")
    @TableField("CORRECTION_COMPLETION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime correctionCompletionTime;

    @Schema(name = "效果验证")
    @TableField("EFFECTIVENESS_VERIFICATION")
    private String effectivenessVerification;

    @Schema(name = "流程改进建议")
    @TableField("PROCESS_IMPROVEMENT_SUGGESTIONS")
    private String processImprovementSuggestions;

    @Schema(name = "模板版本")
    @TableField("TEMPLATE_VERSION")
    private String templateVersion;

    @Schema(name = "格式标准")
    @TableField("FORMAT_STANDARDS")
    private String formatStandards;

    @Schema(name = "科目设置")
    @TableField("ACCOUNT_SETUP")
    private String accountSetup;

    @Schema(name = "公式配置")
    @TableField("FORMULA_CONFIGURATION")
    private String formulaConfiguration;

    @Schema(name = "版本控制")
    @TableField("VERSION_CONTROL")
    private String versionControl;

    @Schema(name = "监督检查记录")
    @TableField("SUPERVISION_RECORDS")
    private String supervisionRecords;

    @Schema(name = "合规检查结果")
    @TableField("COMPLIANCE_CHECK_RESULT")
    private String complianceCheckResult;

    @Schema(name = "创新点")
    @TableField("INNOVATION_POINTS")
    private String innovationPoints;

    @Schema(name = "经验总结")
    @TableField("LESSONS_LEARNED")
    private String lessonsLearned;

    @Schema(name = "最佳实践")
    @TableField("BEST_PRACTICES")
    private String bestPractices;

    @Schema(name = "培训需求")
    @TableField("TRAINING_REQUIREMENTS")
    private String trainingRequirements;

    @Schema(name = "能力提升建议")
    @TableField("CAPABILITY_IMPROVEMENT_SUGGESTIONS")
    private String capabilityImprovementSuggestions;

    @Schema(name = "相关附件")
    @TableField("RELATED_ATTACHMENTS")
    private String relatedAttachments;

    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "扩展字段1")
    @TableField("EXT_FIELD1")
    private String extField1;

    @Schema(name = "扩展字段2")
    @TableField("EXT_FIELD2")
    private String extField2;

    @Schema(name = "扩展字段3")
    @TableField("EXT_FIELD3")
    private String extField3;

    @Schema(name = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_BY")
    private String updateBy;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(name = "删除标志")
    @TableField("DEL_FLAG")
    private String delFlag;
}
