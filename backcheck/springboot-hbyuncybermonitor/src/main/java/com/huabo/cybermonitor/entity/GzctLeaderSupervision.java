package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_SUPERVISION")
public class GzctLeaderSupervision extends Model<GzctLeaderSupervision> {

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

    @TableField("SUPERVISION_TYPE")
    private String supervisionType;

    @TableField("SUPERVISION_CONTENT")
    private String supervisionContent;

    @TableField("FINDING_DESC")
    private String findingDesc;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("RECTIFICATION_STATUS")
    private String rectificationStatus;

    @TableField("SUPERVISOR")
    private String supervisor;

    @TableField("SUPERVISION_DATE")
    private LocalDate supervisionDate;

    // 前端表格展示字段: company (企业别名)
    @TableField(exist = false)
    private String company;

    // 前端表格展示字段: supervisionDate_str (监管日期字符串格式)
    @TableField(exist = false)
    private String supervisionDateStr;

    // 前端表格展示字段: complianceStatus (合规状态)
    @TableField(exist = false)
    private String complianceStatus;

    // 前端表格展示字段: issueCount (问题数量)
    @TableField(exist = false)
    private Integer issueCount;

    // 前端表格展示字段: status (状态)
    @TableField(exist = false)
    private String status;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
