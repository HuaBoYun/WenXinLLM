package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDate;
import java.time.LocalDateTime;;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_RECTIFICATION")
public class GzctAccountingRectification {
    @TableId(value = "RECTIFICATION_ID", type = IdType.ASSIGN_UUID) private String rectificationId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("POLICY_TYPE") private String policyType;
    @TableField("DIFF_LEVEL") private String diffLevel;
    @TableField("DIFF_DESC") private String diffDesc;
    @TableField("RECTIFICATION_REQUIREMENT") private String rectificationRequirement;
    @TableField("DEADLINE") private LocalDate deadline;
    @TableField("STATUS") private String status;
    @TableField("ISSUER") private String issuer;
    @TableField("ISSUE_TIME") private LocalDateTime issueTime;
    @TableField("COMPLETE_TIME") private LocalDateTime completeTime;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
}
