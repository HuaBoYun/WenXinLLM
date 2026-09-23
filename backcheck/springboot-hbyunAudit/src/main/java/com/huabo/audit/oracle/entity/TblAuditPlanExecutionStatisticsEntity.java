package com.huabo.audit.oracle.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 审计计划执行及要点完成情况表
 */
@Data
@Entity
@TableName("TBL_AUDIT_PLAN_EXECUTION_STATISTICS")
public class TblAuditPlanExecutionStatisticsEntity {
    @Id
    @TableId(value = "id", type= IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal id;

    @Schema(name="单位名称")
    @TableField(value= "UNIT_NAME")
    private String unitName;

    @Schema(name="审计项目名称")
    @TableField(value= "AUDIT_PROJECT_NAME")
    private String auditProjectName;

    @Schema(name="审计项目类型")
    @TableField(value= "AUDIT_PROJECT_TYPE")
    private String auditProjectType;

    @Schema(name="被审计单位全称")
    @TableField(value= "AUDITED_ENTITY_NAME")
    private String auditedEntityName;

    @Schema(name="被审计单位级次")
    @TableField(value= "AUDITED_ENTITY_LEVEL")
    private String auditedEntityLevel;

    @Schema(name="审计内容")
    @TableField(value= "AUDIT_CONTENT")
    private String auditContent;

    @Schema(name="审计工作量（人日）")
    @TableField(value= "AUDIT_WORKLOAD")
    private Integer auditWorkload;

    @Schema(name="审计资金量（万元）")
    @TableField(value= "AUDIT_FUND_AMOUNT")
    private BigDecimal auditFundAmount;

    @Schema(name="其中境外审计资金量（万元）")
    @TableField(value= "OVERSEAS_AUDIT_FUND_AMOUNT")
    private BigDecimal overseasAuditFundAmount;

    @Schema(name="审计组织方式")
    @TableField(value= "AUDIT_ORGANIZATION_MODE")
    private String auditOrganizationMode;

    @Schema(name="组长")
    @TableField(value= "TEAM_LEADER")
    private String teamLeader;

    @Schema(name="主审")
    @TableField(value= "PRINCIPAL_AUDITOR")
    private String principalAuditor;

    @Schema(name="截至目前审计状态")
    @TableField(value= "CURRENT_AUDIT_STATUS")
    private String currentAuditStatus;

    @Schema(name="计划完成现场审计时间")
    @TableField(value= "PLANNED_ON_SITE_AUDIT_COMPLETION_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date plannedOnSiteAuditCompletionDate;

    @Schema(name="计划完成现场审计时间开始查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date plannedOnSiteAuditCompletionDateStart;

    @Schema(name="计划完成现场审计时间结束查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date plannedOnSiteAuditCompletionDateEnd;

    @Schema(name="是否已完成现场审计")
    @TableField(value= "IS_ON_SITE_AUDIT_COMPLETED")
    private String isOnSiteAuditCompleted;

    @Schema(name="计划审计报告出具时间")
    @TableField(value= "PLANNED_AUDIT_REPORT_ISSUE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date plannedAuditReportIssueDate;

    @Schema(name="计划审计报告出具时间开始查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date plannedAuditReportIssueDateStart;

    @Schema(name="计划审计报告出具时间结束查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date plannedAuditReportIssueDateEnd;

    @Schema(name="是否已出具审计报告")
    @TableField(value= "IS_AUDIT_REPORT_ISSUED")
    private String isAuditReportIssued;

    @Schema(name="备注")
    @TableField(value= "REMARKS")
    private String remarks;

    @TableField(value= "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
