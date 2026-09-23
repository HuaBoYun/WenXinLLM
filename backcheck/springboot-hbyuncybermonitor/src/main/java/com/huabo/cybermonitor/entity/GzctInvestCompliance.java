package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TZ_COMPLIANCE")
public class GzctInvestCompliance extends Model<GzctInvestCompliance> {
    @TableId(value = "COMPLIANCE_ID", type = IdType.ASSIGN_UUID)
    private String complianceId;
    @TableField("PROJECT_ID")
    private String projectId;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("DECISION_LEVEL")
    private String decisionLevel;
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;
    @TableField("IS_COMPLIANT")
    private String isCompliant;
    @TableField("ISSUE_DESC")
    private String issueDesc;
    @TableField("RECT_STATUS")
    private String rectStatus;
    @TableField("DISPATCH_STATUS")
    private String dispatchStatus;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织ID */
    @TableField("ORG_ID")
    private String orgId;
}
