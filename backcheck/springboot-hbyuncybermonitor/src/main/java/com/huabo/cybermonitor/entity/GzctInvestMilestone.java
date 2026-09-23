package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TZ_MILESTONE")
public class GzctInvestMilestone extends Model<GzctInvestMilestone> {
    @TableId(value = "MILESTONE_ID", type = IdType.ASSIGN_UUID)
    private String milestoneId;
    @TableField("PROJECT_ID")
    private String projectId;
    @TableField("MILESTONE_NAME")
    private String milestoneName;
    @TableField("PLAN_DATE")
    private LocalDate planDate;
    @TableField("ACTUAL_DATE")
    private LocalDate actualDate;
    @TableField("STATUS")
    private String status;
    @TableField("REMARK")
    private String remark;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织ID */
    @TableField("ORG_ID")
    private String orgId;
}
