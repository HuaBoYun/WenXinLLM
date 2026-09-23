package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_REPORT")
public class GzctAccountingReport {

    @TableId(value = "REPORT_ID", type = IdType.ASSIGN_UUID)
    private String reportId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("REPORT_TYPE")
    private String reportType;

    @TableField("SUBMIT_STATUS")
    private String submitStatus;

    @TableField("AUDIT_OPINION")
    private String auditOpinion;

    @TableField("QUALITY_SCORE")
    private Integer qualityScore;

    @TableField("HOOK_ISSUES")
    private Integer hookIssues;

    @TableField("ITEM_NAME")
    private String itemName;

    @TableField("ITEM_CATEGORY")
    private String itemCategory;

    @TableField("CURRENT_AMOUNT")
    private BigDecimal currentAmount;

    @TableField("PREV_AMOUNT")
    private BigDecimal prevAmount;

    @TableField("CHANGE_RATE")
    private BigDecimal changeRate;

    @TableField("IS_CATEGORY")
    private String isCategory;

    @TableField("IS_KEY")
    private String isKey;

    @TableField("ANOMALY")
    private String anomaly;

    @TableField("GROWTH")
    private BigDecimal growth;

    @TableField("REPORT_TAB")
    private String reportTab;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
