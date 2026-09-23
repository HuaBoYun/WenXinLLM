package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TZ_PROGRESS")
public class GzctInvestProgress extends Model<GzctInvestProgress> {
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;
    @TableField("PROJECT_ID")
    private String projectId;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("PLAN_PROGRESS")
    private BigDecimal planProgress;
    @TableField("ACTUAL_PROGRESS")
    private BigDecimal actualProgress;
    @TableField("PLAN_DATE")
    private LocalDate planDate;
    @TableField("DELAY_DAYS")
    private Integer delayDays;
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
