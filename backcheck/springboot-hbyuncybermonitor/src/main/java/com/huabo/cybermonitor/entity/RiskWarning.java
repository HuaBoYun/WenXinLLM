package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_RISK_WARNING")
public class RiskWarning {
    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID)
    private String warningId;
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;
    @TableField("WARNING_TITLE")
    private String warningTitle;
    @TableField("WARNING_DESC")
    private String warningDesc;
    @TableField("WARNING_LEVEL")
    private String warningLevel;
    @TableField("WARNING_TYPE")
    private String warningType;
    @TableField("WARNING_STATUS")
    private String warningStatus;
    @TableField("WARNING_TIME")
    private LocalDateTime warningTime;
    @TableField("RISK_ID")
    private String riskId;
    @TableField("RISK_NAME")
    private String riskName;
    @TableField("INDICATOR_NAME")
    private String indicatorName;
    @TableField("CURRENT_VALUE")
    private BigDecimal currentValue;
    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;
    @TableField("TRIGGER_CONDITION")
    private String triggerCondition;
    @TableField("HANDLED_BY")
    private String handledBy;
    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;
    @TableField("HANDLE_RESULT")
    private String handleResult;
    @TableField("IS_READ")
    private Boolean isRead;
    @TableField("REMARKS")
    private String remarks;
    @TableField("CREATE_BY")
    private String createBy;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_BY")
    private String updateBy;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    @TableField("DELETED")
    @TableLogic
    private Boolean deleted;
}
