package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_DATA_ENTRY")
public class GzctEnterpriseDataEntry extends Model<GzctEnterpriseDataEntry> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("DATA_TYPE")
    private String dataType;

    @TableField("DATA_CATEGORY")
    private String dataCategory;

    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("STATUS")
    private String status;

    @TableField("SUBMITTER")
    private String submitter;

    @TableField("SUBMIT_TIME")
    private LocalDateTime submitTime;

    @TableField("AUDITOR")
    private String auditor;

    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
