package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FINANCIAL_REPORT")
public class GzctFinancialReport extends Model<GzctFinancialReport> {

    @TableId(value = "REPORT_ID", type = IdType.ASSIGN_UUID)
    private String reportId;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("REPORT_NAME")
    private String reportName;

    @TableField("REPORT_TYPE")
    private String reportType;

    @TableField("PERIOD")
    private String period;

    @TableField("STATUS")
    private String status;

    @TableField("CREATOR")
    private String creator;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("PROGRESS")
    private BigDecimal progress;

    @TableField("REPORT_CONTENT")
    private String reportContent;

    @TableField("AUDIT_STATUS")
    private String auditStatus;

    @TableField("AUDITOR")
    private String auditor;

    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    @TableField("AUDIT_OPINION")
    private String auditOpinion;

    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("DEL_FLAG")
    private String delFlag;
}
