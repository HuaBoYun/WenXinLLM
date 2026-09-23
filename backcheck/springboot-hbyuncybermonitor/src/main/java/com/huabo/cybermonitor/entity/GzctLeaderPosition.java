package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 负责人任职情况实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_POSITION")
public class GzctLeaderPosition extends Model<GzctLeaderPosition> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("LEADER_ID")
    private String leaderId;

    @TableField("LEADER_NAME")
    private String leaderName;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("POSITION_NAME")
    private String positionName;

    @TableField("POSITION_LEVEL")
    private String positionLevel;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("APPOINT_DATE")
    private LocalDate appointDate;

    @TableField("LEAVE_DATE")
    private LocalDate leaveDate;

    @TableField("APPOINT_REASON")
    private String appointReason;

    @TableField("RESPONSIBILITY")
    private String responsibility;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
