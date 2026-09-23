package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_INFO")
public class GzctLeaderInfo extends Model<GzctLeaderInfo> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("LEADER_NAME")
    private String leaderName;

    @TableField("GENDER")
    private String gender;

    @TableField("BIRTH_DATE")
    private LocalDate birthDate;

    @TableField("EDUCATION")
    private String education;

    @TableField("MAJOR")
    private String major;

    @TableField("POSITION")
    private String position;

    @TableField("APPOINT_DATE")
    private LocalDate appointDate;

    @TableField("TERM_START")
    private LocalDate termStart;

    @TableField("TERM_END")
    private LocalDate termEnd;

    @TableField("POLITICAL_STATUS")
    private String politicalStatus;

    @TableField("PHONE")
    private String phone;

    @TableField("EMAIL")
    private String email;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
