package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_HR_RECRUITMENT")
public class GzctEnterpriseHrRecruitment extends Model<GzctEnterpriseHrRecruitment> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("POSITION_NO")
    private String positionNo;

    @TableField("POSITION_NAME")
    private String positionName;

    @TableField("POSITION_TYPE")
    private String positionType;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("REQUIRED_COUNT")
    private Integer requiredCount;

    @TableField("APPLICANT_COUNT")
    private Integer applicantCount;

    @TableField("INTERVIEW_COUNT")
    private Integer interviewCount;

    @TableField("HIRED_COUNT")
    private Integer hiredCount;

    @TableField("SALARY_RANGE")
    private String salaryRange;

    @TableField("URGENCY")
    private String urgency;

    @TableField("PUBLISH_DATE")
    private LocalDate publishDate;

    @TableField("DEADLINE")
    private LocalDate deadline;

    @TableField("JOB_DESCRIPTION")
    private String jobDescription;

    @TableField("REQUIREMENTS")
    private String requirements;

    @TableField("STATUS")
    private String status;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
