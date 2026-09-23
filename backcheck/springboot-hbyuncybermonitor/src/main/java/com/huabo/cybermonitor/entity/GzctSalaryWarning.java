package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SALARY_WARNING")
public class GzctSalaryWarning extends Model<GzctSalaryWarning> {

    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID)
    private String warningId;

    @TableField("WARNING_CODE")
    private String warningCode;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("WARNING_TYPE")
    private String warningType;

    @TableField("WARNING_CONTENT")
    private String warningContent;

    @TableField("LEVEL")
    private String level;

    @TableField("STATUS")
    private String status;

    @TableField("RULE_CODE")
    private String ruleCode;

    @TableField("TRIGGER_CONDITION")
    private String triggerCondition;

    @TableField("TRIGGER_TIME")
    private LocalDateTime triggerTime;

    @TableField("TRIGGER_VALUE")
    private BigDecimal triggerValue;

    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;

    @TableField("HANDLE_RESULT")
    private String handleResult;

    @TableField("HANDLE_USER")
    private String handleUser;

    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
