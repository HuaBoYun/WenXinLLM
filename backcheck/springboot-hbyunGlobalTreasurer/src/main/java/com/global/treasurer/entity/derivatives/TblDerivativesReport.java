package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("TBL_DERIVATIVES_REPORT")
public class TblDerivativesReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "REPORT_ID", type = IdType.AUTO)
    private Long reportId;

    @TableField("REPORT_NAME")
    private String reportName;

    @TableField("REPORT_TYPE")
    private String reportType;

    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    @TableField("START_DATE")
    private String startDate;

    @TableField("END_DATE")
    private String endDate;

    @TableField("OUTPUT_FORMAT")
    private String outputFormat;

    @TableField("GENERATED_BY")
    private String generatedBy;

    @TableField("GENERATE_TIME")
    private String generateTime;

    @TableField("FILE_SIZE")
    private Long fileSize;

    @TableField("FILE_PATH")
    private String filePath;

    @TableField("STATUS")
    private String status;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("DEL_FLAG")
    private String delFlag;

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getReportPeriod() { return reportPeriod; }
    public void setReportPeriod(String reportPeriod) { this.reportPeriod = reportPeriod; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getOutputFormat() { return outputFormat; }
    public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public String getGenerateTime() { return generateTime; }
    public void setGenerateTime(String generateTime) { this.generateTime = generateTime; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
}

