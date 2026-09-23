package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_RD_PROJECT")
public class GzctRdProject extends Model<GzctRdProject> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("PROJECT_NO")
    private String projectNo;

    @TableField("PROJECT_NAME")
    private String projectName;

    @TableField("PROJECT_TYPE")
    private String projectType;

    @TableField("LEADER")
    private String leader;

    @TableField("TEAM_SIZE")
    private Integer teamSize;

    @TableField("BUDGET")
    private BigDecimal budget;

    @TableField("USED_BUDGET")
    private BigDecimal usedBudget;

    @TableField("PROGRESS")
    private Integer progress;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("STATUS")
    private String status;

    @TableField("PRIORITY")
    private String priority;

    @TableField("DESCRIPTION")
    private String description;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
