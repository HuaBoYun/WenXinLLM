package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROJECT_MANAGEMENT")
public class GzctProjectManagement extends Model<GzctProjectManagement> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("PROJECT_CODE")
    private String projectCode;

    @TableField("PROJECT_NAME")
    private String projectName;

    @TableField("PROJECT_TYPE")
    private String projectType;

    @TableField("PROJECT_MANAGER")
    private String projectManager;

    @TableField("BUDGET")
    private BigDecimal budget;

    @TableField("ACTUAL_COST")
    private BigDecimal actualCost;

    @TableField("PROGRESS")
    private BigDecimal progress;

    @TableField("START_DATE")
    private String startDate;

    @TableField("END_DATE")
    private String endDate;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
