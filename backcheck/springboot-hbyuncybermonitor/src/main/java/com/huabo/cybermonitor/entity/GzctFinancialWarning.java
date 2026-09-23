package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FINANCIAL_WARNING")
public class GzctFinancialWarning extends Model<GzctFinancialWarning> {

    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID)
    private String warningId;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("WARNING_NAME")
    private String warningName;

    @TableField("WARNING_TYPE")
    private String warningType;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("CURRENT_VALUE")
    private BigDecimal currentValue;

    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;

    @TableField("DEVIATION")
    private BigDecimal deviation;

    @TableField("TRIGGER_TIME")
    private LocalDateTime triggerTime;

    @TableField("STATUS")
    private String status;

    @TableField("HANDLER")
    private String handler;

    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;

    @TableField("HANDLE_REMARK")
    private String handleRemark;

    @TableField("UNIT")
    private String unit;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("DEL_FLAG")
    private String delFlag;
}
