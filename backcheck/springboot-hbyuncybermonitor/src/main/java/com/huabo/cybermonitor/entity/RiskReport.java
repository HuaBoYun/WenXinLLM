package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_RISK_REPORT")
public class RiskReport {
    @TableId(value = "RISK_REPORT_ID", type = IdType.ASSIGN_UUID)
    private String riskReportId;
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;
    @TableField("REPORT_TITLE")
    private String reportTitle;
    @TableField("REPORT_CODE")
    private String reportCode;
    @TableField("REPORT_TYPE")
    private String reportType;
    @TableField("PERIOD")
    private String period;
    @TableField("START_DATE")
    private LocalDate startDate;
    @TableField("END_DATE")
    private LocalDate endDate;
    @TableField("REPORT_CONTENT")
    private String reportContent;
    @TableField("SUMMARY")
    private String summary;
    @TableField("CREATOR")
    private String creator;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("STATUS")
    private String status;
    @TableField("REVIEWER")
    private String reviewer;
    @TableField("REVIEW_TIME")
    private LocalDateTime reviewTime;
    @TableField("REVIEW_COMMENTS")
    private String reviewComments;
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;
    @TableField("DOWNLOAD_COUNT")
    private Integer downloadCount;
    @TableField("FILE_PATH")
    private String filePath;
    @TableField("REMARKS")
    private String remarks;
    @TableField("UPDATE_BY")
    private String updateBy;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    @TableField("DELETED")
    @TableLogic
    private Boolean deleted;
}
