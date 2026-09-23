package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 负责人履历管理实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_RESUME")
public class GzctLeaderResume extends Model<GzctLeaderResume> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("LEADER_ID")
    private String leaderId;

    @TableField("LEADER_NAME")
    private String leaderName;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("RESUME_TYPE")
    private String resumeType;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("ORGANIZATION")
    private String organization;

    @TableField("POSITION_HELD")
    private String positionHeld;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
