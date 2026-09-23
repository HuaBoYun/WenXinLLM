package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_HR_DEPARTMENT")
public class GzctEnterpriseHrDepartment extends Model<GzctEnterpriseHrDepartment> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("DEPT_NAME")
    private String deptName;

    @TableField("DEPT_CODE")
    private String deptCode;

    @TableField("PARENT_ID")
    private String parentId;

    @TableField("DEPT_LEVEL")
    private Integer deptLevel;

    @TableField("MANAGER_NAME")
    private String managerName;

    @TableField("EMPLOYEE_COUNT")
    private Integer employeeCount;

    @TableField("STATUS")
    private String status;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
