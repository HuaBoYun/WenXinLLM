package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会计政策登记实体 - 会计穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ACCOUNTING_POLICY")
public class TblAccountingPolicy {

    @TableId(value = "POLICY_ID", type = IdType.ASSIGN_UUID)
    private String policyId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("POLICY_TYPE")
    private String policyType;

    @TableField("POLICY_NAME")
    private String policyName;

    @TableField("POLICY_DETAIL")
    private String policyDetail;

    @TableField("GROUP_STANDARD")
    private String groupStandard;

    @TableField("IS_CONSISTENT")
    private String isConsistent;

    @TableField("DEVIATION_DESC")
    private String deviationDesc;

    @TableField("EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @TableField("AUDIT_OPINION_TYPE")
    private String auditOpinionType;

    @TableField("AUDIT_EMPHASIS")
    private String auditEmphasis;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("STATUS")
    private String status;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

