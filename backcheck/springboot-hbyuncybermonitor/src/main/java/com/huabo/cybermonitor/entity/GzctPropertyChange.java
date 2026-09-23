package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROPERTY_CHANGE")
public class GzctPropertyChange {
    @TableId(value = "CHANGE_ID", type = IdType.ASSIGN_UUID) private String changeId;
    @TableField("PROPERTY_ID") private String propertyId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("CHANGE_TYPE") private String changeType;
    @TableField("CHANGE_REASON") private String changeReason;
    @TableField("BEFORE_RATIO") private BigDecimal beforeRatio;
    @TableField("AFTER_RATIO") private BigDecimal afterRatio;
    @TableField("BEFORE_AMOUNT") private BigDecimal beforeAmount;
    @TableField("AFTER_AMOUNT") private BigDecimal afterAmount;
    @TableField("CHANGE_AMOUNT") private BigDecimal changeAmount;
    @TableField("CHANGE_DATE") private LocalDate changeDate;
    @TableField("EFFECTIVE_DATE") private LocalDate effectiveDate;
    @TableField("APPROVAL_STATUS") private String approvalStatus;
    @TableField("APPROVAL_AUTHORITY") private String approvalAuthority;
    @TableField("APPROVAL_NO") private String approvalNo;
    @TableField("IS_MAJOR_CHANGE") private String isMajorChange;
    @TableField("IS_WARNING") private Integer isWarning;
    @TableField("IMPACT_ANALYSIS") private String impactAnalysis;
    @TableField("REMARK") private String remark;
    @TableField("CREATE_BY") private String createBy;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
