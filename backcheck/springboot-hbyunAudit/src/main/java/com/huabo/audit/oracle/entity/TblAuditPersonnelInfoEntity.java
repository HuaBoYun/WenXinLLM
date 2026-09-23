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
 * 审计人员情况报表
 */
@Data
@Entity
@TableName("TBL_AUDIT_PERSONNEL_INFO")
public class TblAuditPersonnelInfoEntity {
    @Id
    @TableId(value = "id", type= IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal id;

    @Schema(name="单位名称")
    @TableField(value = "UNIT_NAME")
    private String unitName;

    @Schema(name="姓名")
    @TableField(value = "NAME")
    private String name;

    @Schema(name="性别")
    @TableField(value = "GENDER")
    private String gender;

    @Schema(name="出生日期")
    @TableField(value = "BIRTHDATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

    @Schema(name="籍贯")
    @TableField(value = "NATIVE_PLACE")
    private String nativePlace;

    @Schema(name="民族")
    @TableField(value = "ETHNICITY")
    private String ethnicity;

    @Schema(name="政治面貌")
    @TableField(value = "POLITICAL_STATUS")
    private String politicalStatus;

    @Schema(name="主要从事的职责")
    @TableField(value = "PRIMARY_RESPONSIBILITIES")
    private String primaryResponsibilities;

    @Schema(name="职务")
    @TableField(value = "POSITION")
    private String position;

    @Schema(name="专业技术职称")
    @TableField(value = "PROFESSIONAL_TITLE")
    private String professionalTitle;

    @Schema(name="执业资格")
    @TableField(value = "PROFESSIONAL_QUALIFICATION")
    private String professionalQualification;

    @Schema(name="是否为国际注册信息系统审计师CIA")
    @TableField(value = "IS_CIA")
    private String isCia;

    @Schema(name="参加工作时间")
    @TableField(value = "WORK_START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date workStartDate;

    @Schema(name="参加工作时间开始查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date workStartDateStart;

    @Schema(name="参加工作时间结束查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date workStartDateEnd;

    @Schema(name="进入本单位时间")
    @TableField(value = "JOIN_UNIT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinUnitDate;

    @Schema(name="进入本单位时间开始查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinUnitDateStart;

    @Schema(name="进入本单位时间结束查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinUnitDateEnd;

    @Schema(name="进入系统时间")
    @TableField(value = "JOIN_SYSTEM_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinSystemDate;

    @Schema(name="进入系统时间开始查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinSystemDateStart;

    @Schema(name="进入系统时间结束查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinSystemDateEnd;

    @Schema(name="从事审计工作年限")
    @TableField(value = "YEARS_OF_AUDIT_EXPERIENCE")
    private Integer yearsOfAuditExperience;

    @Schema(name="曾从事的其他工作")
    @TableField(value = "PREVIOUS_JOBS")
    private String previousJobs;

    @Schema(name="是否提拔交流人员")
    @TableField(value = "IS_PROMOTED_TRANSFER")
    private String isPromotedTransfer;

    @Schema(name="是否平职交流人员")
    @TableField(value = "IS_HORIZONTAL_TRANSFER")
    private String isHorizontalTransfer;

    @Schema(name="学历")
    @TableField(value = "EDUCATION")
    private String education;

    @Schema(name="最高学历专业")
    @TableField(value = "HIGHEST_EDUCATION_MAJOR")
    private String highestEducationMajor;

    @Schema(name="其他学历专业")
    @TableField(value = "OTHER_EDUCATION_MAJORS")
    private String otherEducationMajors;

    @Schema(name="毕业时间")
    @TableField(value = "GRADUATION_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date graduationDate;

    @Schema(name="毕业时间开始查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date graduationDateStart;

    @Schema(name="毕业时间结束查询条件")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date graduationDateEnd;

    @Schema(name="毕业院校")
    @TableField(value = "UNIVERSITY")
    private String university;

    @Schema(name="办公电话_区号")
    @TableField(value = "OFFICE_PHONE_AREA_CODE")
    private String officePhoneAreaCode;

    @Schema(name="办公电话_电话号")
    @TableField(value = "OFFICE_PHONE_NUMBER")
    private String officePhoneNumber;

    @Schema(name="手机号码")
    @TableField(value = "MOBILE_PHONE")
    private String mobilePhone;

    @Schema(name="电子邮箱")
    @TableField(value = "EMAIL")
    private String email;

    @Schema(name="培训学时")
    @TableField(value = "TRAINING_HOURS")
    private Integer trainingHours;

    @Schema(name="对应支付的经费（万元）")
    @TableField(value = "TRAINING_EXPENSES")
    private BigDecimal trainingExpenses;

    @Schema(name="受到表彰情况")
    @TableField(value = "RECOGNITIONS")
    private String recognitions;

    @Schema(name="受到惩处情况")
    @TableField(value = "PUNISHMENTS")
    private String punishments;

    @Schema(name="是否特邀审计员")
    @TableField(value = "IS_SPECIAL_INVITED_AUDITOR")
    private String isSpecialInvitedAuditor;

    @Schema(name="参加本单位审计项目的次数")
    @TableField(value = "NUMBER_OF_UNIT_AUDIT_PROJECTS")
    private Integer numberOfUnitAuditProjects;

    @Schema(name="参加上级审计部门审计项目的次数")
    @TableField(value = "NUMBER_OF_SUPERIOR_AUDIT_PROJECTS")
    private Integer numberOfSuperiorAuditProjects;

    @Schema(name="备注")
    @TableField(value = "REMARKS")
    private String remarks;

    @TableField(value= "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
