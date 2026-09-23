package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 审计情况统计表
 */
@Data
@Entity
@TableName("TBL_AUDIT_STATISTICS")
public class TblAuditStatisticsEntity {
    @Id
    @TableId(value = "id", type= IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal id;

    @Schema(name="单位名称")
    @TableField(value= "UNIT_NAME")
    private String unitName;

    @Schema(name="是否设立董事会")
    @TableField(value= "HAS_BOARD_OF_DIRECTORS")
    private String hasBoardOfDirectors;

    @Schema(name="是否设立审计委员会")
    @TableField(value= "HAS_AUDIT_COMMITTEE")
    private String hasAuditCommittee;

    @Schema(name="是否设立总审计师")
    @TableField(value= "HAS_CHIEF_AUDITOR")
    private String hasChiefDesigner;

    @Schema(name="总审计师职位层级")
    @TableField(value= "CHIEF_AUDITOR_POSITION_LEVEL")
    private String chiefDesignerPositionLevel;

    @Schema(name="总审计师任职方式")
    @TableField(value= "CHIEF_AUDITOR_EMPLOYMENT_MODE")
    private String chiefDesignerEmploymentMode;

    @Schema(name="是否设置内部审计机构")
    @TableField(value= "HAS_INTERNAL_AUDIT_DEPARTMENT")
    private String hasInternalAuditDepartment;

    @Schema(name="内部审计工作的领导机构")
    @TableField(value= "INTERNAL_AUDIT_LEADERSHIP_ORGANIZATION")
    private String internalAuditLeadershipOrganization;

    @Schema(name="内部审计机构层级")
    @TableField(value= "INTERNAL_AUDIT_DEPARTMENT_LEVEL")
    private String internalAuditDepartmentLevel;

    @Schema(name="本年度内部审计制度制修订个数")
    @TableField(value= "INTERNAL_AUDIT_SYSTEM_REVISIONS")
    private Integer internalAuditSystemRevisions;

    @Schema(name="内部审计部门是否实现差异化考核")
    @TableField(value= "HAS_DIFFERENTIATED_PERFORMANCE_EVALUATION")
    private String hasDifferentiatedPerformanceEvaluation;

    @Schema(name="部门负责人绩效考核结果")
    @TableField(value= "DEPARTMENT_HEAD_PERFORMANCE_RESULT")
    private String departmentHeadPerformanceResult;

    @Schema(name="部门考核结果")
    @TableField(value= "DEPARTMENT_PERFORMANCE_RESULT")
    private String departmentPerformanceResult;

    @Schema(name="编制数")
    @TableField(value= "APPROVED_STAFFING")
    private Integer approvedStaffing;

    @Schema(name="实有人员数")
    @TableField(value= "ACTUAL_STAFFING")
    private Integer actualStaffing;

    @Schema(name="从事审计职责_本机构是否有此职能")
    @TableField(value= "HAS_AUDIT_FUNCTION")
    private String hasAuditFunction;

    @Schema(name="从事审计职责_编制数")
    @TableField(value= "AUDIT_FUNCTION_APPROVED_STAFFING")
    private Integer auditFunctionApprovedStaffing;

    @Schema(name="从事审计职责_人员数")
    @TableField(value= "AUDIT_FUNCTION_ACTUAL_STAFFING")
    private Integer auditFunctionActualStaffing;

    @Schema(name="从事风险管理工作_本机构是否有此职能")
    @TableField(value= "HAS_RISK_MANAGEMENT_FUNCTION")
    private String hasRiskManagementFunction;

    @Schema(name="从事风险管理工作_编制数")
    @TableField(value= "RISK_MANAGEMENT_APPROVED_STAFFING")
    private Integer riskManagementApprovedStaffing;

    @Schema(name="从事风险管理工作_人员数")
    @TableField(value= "RISK_MANAGEMENT_ACTUAL_STAFFING")
    private Integer riskManagementActualStaffing;

    @Schema(name="从事内部控制评价工作_本机构是否有此职能")
    @TableField(value= "HAS_INTERNAL_CONTROL_EVALUATION_FUNCTION")
    private String hasInternalControlEvaluationFunction;

    @Schema(name="从事内部控制评价工作_编制数")
    @TableField(value= "INTERNAL_CONTROL_EVALUATION_APPROVED_STAFFING")
    private Integer internalControlEvaluationApprovedStaffing;

    @Schema(name="从事内部控制评价工作_人员数")
    @TableField(value= "INTERNAL_CONTROL_EVALUATION_ACTUAL_STAFFING")
    private Integer internalControlEvaluationActualStaffing;

    @Schema(name="其他工作_工作名称")
    @TableField(value= "OTHER_WORK_NAMES")
    private String otherWorkNames;

    @Schema(name="其他工作_编制数")
    @TableField(value= "OTHER_WORK_APPROVED_STAFFING")
    private Integer otherWorkApprovedStaffing;

    @Schema(name="其他工作_人员数")
    @TableField(value= "OTHER_WORK_ACTUAL_STAFFING")
    private Integer otherWorkActualStaffing;

    @Schema(name="通讯地址")
    @TableField(value= "CONTACT_ADDRESS")
    private String contactAddress;

    @Schema(name="邮编")
    @TableField(value= "POSTAL_CODE")
    private String postalCode;

    @Schema(name="传真号码_区号")
    @TableField(value= "FAX_AREA_CODE")
    private String faxAreaCode;

    @Schema(name="传真号码_电话号")
    @TableField(value= "FAX_PHONE_NUMBER")
    private String faxPhoneNumber;

    @Schema(name="审计分管领导信息_姓名")
    @TableField(value= "AUDIT_SUPERVISOR_NAME")
    private String auditSupervisorName;

    @Schema(name="审计分管领导信息_职务")
    @TableField(value= "AUDIT_SUPERVISOR_POSITION")
    private String auditSupervisorPosition;

    @Schema(name="审计分管领导信息_性别")
    @TableField(value= "AUDIT_SUPERVISOR_GENDER")
    private String auditSupervisorGender;

    @Schema(name="审计分管领导信息_出生日期")
    @TableField(value= "AUDIT_SUPERVISOR_BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auditSupervisorBirthdate;

    @Schema(name="审计分管领导信息_籍贯")
    @TableField(value= "AUDIT_SUPERVISOR_NATIVE_PLACE")
    private String auditSupervisorNativePlace;

    @Schema(name="审计分管领导信息_民族")
    @TableField(value= "AUDIT_SUPERVISOR_ETHNICITY")
    private String auditSupervisorEthnicity;

    @Schema(name="审计分管领导信息_办公电话_区号")
    @TableField(value= "AUDIT_SUPERVISOR_OFFICE_PHONE_AREA_CODE")
    private String auditSupervisorOfficePhoneAreaCode;

    @Schema(name="审计分管领导信息_办公电话_电话")
    @TableField(value= "AUDIT_SUPERVISOR_OFFICE_PHONE_NUMBER")
    private String auditSupervisorOfficePhoneNumber;

    @Schema(name="审计分管领导信息_电子邮箱")
    @TableField(value= "AUDIT_SUPERVISOR_EMAIL")
    private String auditSupervisorEmail;

    @Schema(name="审计协管领导信息_姓名")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_NAME")
    private String auditAssistantSupervisorName;

    @Schema(name="审计协管领导信息_职务")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_POSITION")
    private String auditAssistantSupervisorPosition;

    @Schema(name="审计协管领导信息_性别")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_GENDER")
    private String auditAssistantSupervisorGender;

    @Schema(name="审计协管领导信息_出生日期")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auditAssistantSupervisorBirthdate;

    @Schema(name="审计协管领导信息_籍贯")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_NATIVE_PLACE")
    private String auditAssistantSupervisorNativePlace;

    @Schema(name="审计协管领导信息_民族")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_ETHNICITY")
    private String auditAssistantSupervisorEthnicity;

    @Schema(name="审计协管领导信息_办公电话_区号")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_OFFICE_PHONE_AREA_CODE")
    private String auditAssistantSupervisorOfficePhoneAreaCode;

    @Schema(name="审计协管领导信息_办公电话_电话")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_OFFICE_PHONE_NUMBER")
    private String auditAssistantSupervisorOfficePhoneNumber;

    @Schema(name="审计协管领导信息_电子邮箱")
    @TableField(value= "AUDIT_ASSISTANT_SUPERVISOR_EMAIL")
    private String auditAssistantSupervisorEmail;

    @Schema(name="审计机构名称")
    @TableField(value= "AUDIT_DEPARTMENT_NAME")
    private String auditDepartmentName;

    @Schema(name="审计机构是否独立")
    @TableField(value= "AUDIT_DEPARTMENT_IS_INDEPENDENT")
    private String auditDepartmentIsIndependent;

    @Schema(name="非独立审计机构与哪些职能部门合并设置")
    @TableField(value= "NON_INDEPENDENT_AUDIT_DEPARTMENT_MERGED")
    private String nonIndependentAuditDepartmentMerged;

    @TableField(value= "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}