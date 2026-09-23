package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_COMPLIANCE")
public class GzctOverseasCompliance extends Model<GzctOverseasCompliance> {
    @TableId(value = "COMPLIANCE_ID", type = IdType.ASSIGN_UUID) private String complianceId;
    @TableField("UNIT_ID") private String unitId;
    // 前端使用 unitName
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("UNIT_NAME") private String unitName;
    @TableField("COUNTRY") private String country;
    @TableField("COMPLIANCE_TYPE") private String complianceType;
    // isCompliant: 0否/1是，前端用 complianceStatus: COMPLIANT/NON_COMPLIANT/PENDING
    @TableField("IS_COMPLIANT") private String isCompliant;
    @TableField("COMPLIANCE_STATUS") private String complianceStatus;
    @TableField("VIOLATION_DESC") private String violationDesc;
    @TableField("PENALTY_AMOUNT") private BigDecimal penaltyAmount;
    @TableField("RECT_STATUS") private String rectStatus;
    @TableField("LAST_CHECK_DATE") private LocalDate lastCheckDate;
    @TableField("NEXT_CHECK_DATE") private LocalDate nextCheckDate;
    @TableField("ISSUE_COUNT") private Integer issueCount;
    @TableField("LEADER") private String leader;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
