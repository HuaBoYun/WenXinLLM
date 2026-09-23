package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_COST")
public class GzctEnterpriseCost extends Model<GzctEnterpriseCost> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("COST_TYPE")
    private String costType;

    @TableField("COST_AMOUNT")
    private BigDecimal costAmount;

    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("COST_NAME")
    private String costName;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("DEL_FLAG")
    private String delFlag;
}
