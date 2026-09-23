package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_DEVELOPMENT")
public class GzctLeaderDevelopment extends Model<GzctLeaderDevelopment> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("LEADER_ID")
    private String leaderId;

    @TableField("LEADER_NAME")
    private String leaderName;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("DEVELOPMENT_TYPE")
    private String developmentType;

    @TableField("DEVELOPMENT_NAME")
    private String developmentName;

    @TableField("INSTITUTION")
    private String institution;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("ACHIEVEMENT")
    private String achievement;

    @TableField("STATUS")
    private String status;

    @TableField("DURATION")
    private Integer duration;

    @TableField("PROGRESS")
    private Integer progress;

    @TableField("SCORE")
    private Integer score;

    @TableField("DESCRIPTION")
    private String description;

    // 前端表格展示字段: company (企业别名)
    @TableField(exist = false)
    private String company;

    // 前端表格展示字段: programName (培训项目别名)
    @TableField(exist = false)
    private String programName;

    // 前端表格展示字段: trainingType (培训类型别名)
    @TableField(exist = false)
    private String trainingType;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
