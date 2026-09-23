package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_HR_EMPLOYEE")
public class GzctEnterpriseHrEmployee extends Model<GzctEnterpriseHrEmployee> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("EMPLOYEE_NAME")
    private String employeeName;

    @TableField("EMPLOYEE_NO")
    private String employeeNo;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("POSITION")
    private String position;

    @TableField("EDUCATION")
    private String education;

    @TableField("ENTRY_DATE")
    private LocalDate entryDate;

    @TableField("CONTRACT_END_DATE")
    private LocalDate contractEndDate;

    @TableField("SALARY_LEVEL")
    private String salaryLevel;

    @TableField("STATUS")
    private String status;

    @TableField("GENDER")
    private String gender;

    @TableField("PHONE")
    private String phone;

    @TableField("EMAIL")
    private String email;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
