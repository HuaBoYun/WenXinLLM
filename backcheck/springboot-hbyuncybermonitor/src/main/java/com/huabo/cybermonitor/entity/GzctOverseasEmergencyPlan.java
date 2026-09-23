package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 境外应急预案实体类
 * @TableName GZCT_OVERSEAS_EMERGENCY_PLAN
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_EMERGENCY_PLAN")
public class GzctOverseasEmergencyPlan extends Model<GzctOverseasEmergencyPlan> {

    @TableId(value = "PLAN_ID", type = IdType.ASSIGN_UUID)
    private String planId;

    @TableField("PLAN_NAME")
    private String planName;

    @TableField("PLAN_TYPE")
    private String planType;

    @TableField("APPLICABLE_SCENARIO")
    private String applicableScenario;

    @TableField("PLAN_LEVEL")
    private String planLevel;

    @TableField("RESPONSE_PROCESS")
    private String responseProcess;

    @TableField("RESPONSIBLE_DEPT")
    private String responsibleDept;

    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @TableField("STATUS")
    private String status;

    @TableField("VERSION")
    private String version;

    @TableField("LAST_DRILL_DATE")
    private LocalDate lastDrillDate;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
