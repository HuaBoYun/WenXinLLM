package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 负责人变更记录实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_CHANGE_RECORD")
public class GzctLeaderChangeRecord extends Model<GzctLeaderChangeRecord> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("LEADER_ID")
    private String leaderId;

    @TableField("LEADER_NAME")
    private String leaderName;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("CHANGE_TYPE")
    private String changeType;

    @TableField("CHANGE_FIELD")
    private String changeField;

    @TableField("OLD_VALUE")
    private String oldValue;

    @TableField("NEW_VALUE")
    private String newValue;

    @TableField("CHANGE_REASON")
    private String changeReason;

    @TableField("CHANGE_DATE")
    private LocalDate changeDate;

    @TableField("OPERATOR")
    private String operator;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
