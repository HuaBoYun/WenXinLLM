package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 负责人资质证书实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_CERTIFICATE")
public class GzctLeaderCertificate extends Model<GzctLeaderCertificate> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("LEADER_ID")
    private String leaderId;

    @TableField("LEADER_NAME")
    private String leaderName;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("CERT_NAME")
    private String certName;

    @TableField("CERT_TYPE")
    private String certType;

    @TableField("CERT_NO")
    private String certNo;

    @TableField("ISSUE_ORG")
    private String issueOrg;

    @TableField("ISSUE_DATE")
    private LocalDate issueDate;

    @TableField("EXPIRE_DATE")
    private LocalDate expireDate;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
