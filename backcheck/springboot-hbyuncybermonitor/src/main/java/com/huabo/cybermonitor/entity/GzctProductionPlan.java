package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PRODUCTION_PLAN")
public class GzctProductionPlan extends Model<GzctProductionPlan> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("PLAN_NAME")
    private String planName;

    @TableField("PLAN_TYPE")
    private String planType;

    @TableField("PRODUCT_NAME")
    private String productName;

    @TableField("PRODUCTION_LINE")
    private String productionLine;

    @TableField("PLANNED_QUANTITY")
    private BigDecimal plannedQuantity;

    @TableField("ACTUAL_QUANTITY")
    private BigDecimal actualQuantity;

    @TableField("COMPLETION_RATE")
    private BigDecimal completionRate;

    @TableField("START_DATE")
    private String startDate;

    @TableField("END_DATE")
    private String endDate;

    @TableField("STATUS")
    private String status;

    @TableField("MANAGER")
    private String manager;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
