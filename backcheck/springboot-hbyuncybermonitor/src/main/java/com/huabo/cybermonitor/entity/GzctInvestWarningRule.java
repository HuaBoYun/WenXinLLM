package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TZ_WARNING_RULE")
public class GzctInvestWarningRule extends Model<GzctInvestWarningRule> {
    @TableId(value = "RULE_ID", type = IdType.ASSIGN_UUID)
    private String ruleId;
    @TableField("RULE_CODE")
    private String ruleCode;
    @TableField("RULE_NAME")
    private String ruleName;
    @TableField("TRIGGER_CONDITION")
    private String triggerCondition;
    @TableField("RISK_LEVEL")
    private String riskLevel;
    @TableField("DEADLINE_DAYS")
    private Integer deadlineDays;
    @TableField("HANDLE_ACTION")
    private String handleAction;
    @TableField("STATUS")
    private String status;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织ID */
    @TableField("ORG_ID")
    private String orgId;
}
