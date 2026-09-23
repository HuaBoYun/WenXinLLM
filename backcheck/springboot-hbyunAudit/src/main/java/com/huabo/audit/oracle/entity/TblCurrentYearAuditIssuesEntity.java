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
 * 本年度内部审计发现问题及整改情况
 */
@Data
@Entity
@TableName("TBL_CURRENT_YEAR_AUDIT_ISSUES")
public class TblCurrentYearAuditIssuesEntity {
    @Id
    @TableId(value = "id", type = IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal id;

    @Schema(name="审计实施单位名称")
    @TableField(value = "AUDIT_IMPLEMENTING_UNIT")
    private String auditImplementingUnit;

    @Schema(name="审计项目名称")
    @TableField(value = "AUDIT_PROJECT_NAME")
    private String auditProjectName;

    @Schema(name="审计项目类型")
    @TableField(value = "AUDIT_PROJECT_TYPE")
    private String auditProjectType;

    @Schema(name="整改责任单位")
    @TableField(value = "ISSUE_RESPONSIBLE_UNIT")
    private String issueResponsibleUnit;

    @Schema(name="审计发现问题简述")
    @TableField(value = "ISSUE_DESCRIPTION")
    private String issueDescription;

    @Schema(name="审计发现问题类型（国资委、集团公司口径）")
    @TableField(value = "ISSUE_TYPE_SASAC_GROUP")
    private String issueTypeSasacGroup;

    @Schema(name="审计发现问题类型（审计器口径）")
    @TableField(value = "ISSUE_TYPE_AUDIT_TOOL")
    private String issueTypeAuditTool;

    @Schema(name="金额类问题类型（审计器口径）")
    @TableField(value = "MONETARY_ISSUE_TYPE_AUDIT_TOOL")
    private String monetaryIssueTypeAuditTool;

    @Schema(name="金额类问题涉及具体金额（审计署口径）（万元）")
    @TableField(value = "MONETARY_ISSUE_AMOUNT")
    private BigDecimal monetaryIssueAmount;

    @Schema(name="非金额类问题类型（审计署口径）")
    @TableField(value = "NON_MONETARY_ISSUE_TYPE_AUDIT_OFFICE")
    private String nonMonetaryIssueTypeAuditOffice;

    @Schema(name="提出审计建议简述")
    @TableField(value = "AUDIT_RECOMMENDATIONS")
    private String auditRecommendations;

    @Schema(name="整改完成标准从定分类")
    @TableField(value = "RECTIFICATION_COMPLETION_STANDARD")
    private String rectificationCompletionStandard;

    @Schema(name="细化的整改措施")
    @TableField(value = "DETAILED_RECTIFICATION_MEASURES")
    private String detailedRectificationMeasures;

    @Schema(name="审计整改期限")
    @TableField(value = "RECTIFICATION_DEADLINE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rectificationDeadline;

    @Schema(name="审计整改期限开始查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rectificationDeadlineStart;

    @Schema(name="审计整改期限结束查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rectificationDeadlineEnd;

    @Schema(name="整改第一责任人")
    @TableField(value = "PRIMARY_RECTIFICATION_RESPONSIBLE_PERSON")
    private String primaryRectificationResponsiblePerson;

    @Schema(name="协助整改工作的领导")
    @TableField(value = "ASSISTING_RECTIFICATION_LEADER")
    private String assistingRectificationLeader;

    @Schema(name="牵头整改部门责任人及联系电话")
    @TableField(value = "LEAD_RECTIFICATION_DEPARTMENT_CONTACT")
    private String leadRectificationDepartmentContact;

    @Schema(name="配合整改部门责任人及联系电话")
    @TableField(value = "COORDINATING_RECTIFICATION_DEPARTMENT_CONTACT")
    private String coordinatingRectificationDepartmentContact;

    @Schema(name="审计部门责任人及联系电话")
    @TableField(value = "AUDIT_DEPARTMENT_CONTACT")
    private String auditDepartmentContact;

    @Schema(name="截至目前整改措施落实情况描述")
    @TableField(value = "RECTIFICATION_PROGRESS_DESCRIPTION")
    private String rectificationProgressDescription;

    @Schema(name="涉及金额整改措施-合计（万元）")
    @TableField(value = "TOTAL_MONETARY_RECTIFICATION_AMOUNT")
    private BigDecimal totalMonetaryRectificationAmount;

    @Schema(name="涉及金额整改措施-调整会计账目（万元）")
    @TableField(value = "ACCOUNTING_ADJUSTMENT_AMOUNT")
    private BigDecimal accountingAdjustmentAmount;

    @Schema(name="涉及金额整改措施-收回资金（万元）")
    @TableField(value = "RECOVERED_FUNDS_AMOUNT")
    private BigDecimal recoveredFundsAmount;

    @Schema(name="涉及金额整改措施-挽回损失（万元）")
    @TableField(value = "LOSS_RECOVERY_AMOUNT")
    private BigDecimal lossRecoveryAmount;

    @Schema(name="涉及金额整改措施-归还原资金渠道（万元）")
    @TableField(value = "ORIGINAL_FUND_CHANNEL_RETURN_AMOUNT")
    private BigDecimal originalFundChannelReturnAmount;

    @Schema(name="涉及金额整改措施-补缴税费（万元）")
    @TableField(value = "TAX_SUPPLEMENT_AMOUNT")
    private BigDecimal taxSupplementAmount;

    @Schema(name="涉及金额整改措施-其他（万元）")
    @TableField(value = "OTHER_MONETARY_RECTIFICATION_AMOUNT")
    private BigDecimal otherMonetaryRectificationAmount;

    @Schema(name="涉及金额整改措施-其他的具体方式")
    @TableField(value = "OTHER_MONETARY_RECTIFICATION_METHOD")
    private String otherMonetaryRectificationMethod;

    @Schema(name="不涉及金额的整改措施-合计（个）")
    @TableField(value = "TOTAL_NON_MONETARY_RECTIFICATION_MEASURES")
    private Integer totalNonMonetaryRectificationMeasures;

    @Schema(name="不涉及金额的整改措施-新建指定制度（个）")
    @TableField(value = "NEW_SYSTEMS_ESTABLISHED")
    private Integer newSystemsEstablished;

    @Schema(name="不涉及金额的整改措施-修订完善制度（个）")
    @TableField(value = "EXISTING_SYSTEMS_IMPROVED")
    private Integer existingSystemsImproved;

    @Schema(name="不涉及金额的整改措施-优化完善业务流程（个）")
    @TableField(value = "BUSINESS_PROCESSES_OPTIMIZED")
    private Integer businessProcessesOptimized;

    @Schema(name="不涉及金额的整改措施-其他措施（个）")
    @TableField(value = "OTHER_NON_MONETARY_RECTIFICATION_MEASURES")
    private Integer otherNonMonetaryRectificationMeasures;

    @Schema(name="不涉及金额的整改措施-新建、修订制度名称、优化完善业务流程及其他措施具体情况")
    @TableField(value = "NON_MONETARY_RECTIFICATION_DETAILS")
    private String nonMonetaryRectificationDetails;

    @Schema(name="追责问题情况-组织处理（人次）")
    @TableField(value = "PERSONNEL_ACTIONS")
    private Integer personnelActions;

    @Schema(name="追责问题情况-扣减薪酬（万元）")
    @TableField(value = "SALARY_DEDUCTION_AMOUNT")
    private BigDecimal salaryDeductionAmount;

    @Schema(name="追责问题情况-党纪处分（人次）")
    @TableField(value = "PARTY_DISCIPLINARY_ACTIONS")
    private Integer partyDisciplinaryActions;

    @Schema(name="追责问题情况-政务处分（人次）")
    @TableField(value = "ADMINISTRATIVE_SANCTIONS")
    private Integer administrativeSanctions;

    @Schema(name="追责问题情况-向司法机关移送问题线索（个）")
    @TableField(value = "JUDICIAL_REFERRALS")
    private Integer judicialReferrals;

    @Schema(name="追责问题情况-涉案人员（人次）")
    @TableField(value = "PERSONS_INVOLVED")
    private Integer personsInvolved;

    @Schema(name="追责问题情况-具体情况描述")
    @TableField(value = "ACCOUNTABILITY_DETAILS")
    private String accountabilityDetails;

    @Schema(name="是否提交整改销号申请")
    @TableField(value = "HAS_SUBMITTED_RECTIFICATION_APPLICATION")
    private String hasSubmittedRectificationApplication;

    @Schema(name="是否已完成整改销号")
    @TableField(value = "IS_RECTIFICATION_COMPLETED")
    private String isRectificationCompleted;

    @Schema(name="到期未完成整改原因及下一步措施")
    @TableField(value = "UNCOMPLETED_RECTIFICATION_REASON")
    private String uncompletedRectificationReason;

    @TableField(value= "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
