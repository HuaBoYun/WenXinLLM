package com.global.treasurer.dto.export;

import com.global.treasurer.entity.TblRegulatoryReport;
import com.global.treasurer.util.excel.annotation.ExcelField;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 监管报告导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-03-27
 */
public class ExportRegulatoryReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "报告编号", sort = 1, words = 15)
    private String reportNo;

    @ExcelField(title = "报告名称", sort = 2, words = 25)
    private String reportName;

    @ExcelField(title = "监管机构", sort = 3, words = 15)
    private String authorityName;

    @ExcelField(title = "报告期间", sort = 4, words = 12)
    private String reportPeriod;

    @ExcelField(title = "截止日期", sort = 5, words = 12)
    private String dueDate;

    @ExcelField(title = "提交日期", sort = 6, words = 12)
    private String submitDate;

    @ExcelField(title = "报告状态", sort = 7, words = 10)
    private String reportStatus;

    @ExcelField(title = "提交方式", sort = 8, words = 10)
    private String submissionMethod;

    @ExcelField(title = "确认编号", sort = 9, words = 18)
    private String acknowledgmentNo;

    @ExcelField(title = "创建时间", sort = 10, words = 18)
    private String createdTime;

    @ExcelField(title = "备注", sort = 11, words = 30)
    private String remark;

    public static ExportRegulatoryReportDTO fromEntity(TblRegulatoryReport entity) {
        if (entity == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExportRegulatoryReportDTO dto = new ExportRegulatoryReportDTO();
        dto.reportNo = entity.getReportNo();
        dto.reportName = entity.getReportName();
        dto.authorityName = entity.getAuthorityName() != null ? entity.getAuthorityName() : entity.getAuthorityId();
        dto.reportPeriod = entity.getReportPeriod();
        dto.dueDate = entity.getDueDate() != null ? sdf.format(entity.getDueDate()) : "";
        dto.submitDate = entity.getSubmitDate() != null ? sdf.format(entity.getSubmitDate()) : "";
        // 状态转中文
        String status = entity.getReportStatus();
        if ("DRAFT".equals(status)) dto.reportStatus = "草稿";
        else if ("GENERATED".equals(status)) dto.reportStatus = "已生成";
        else if ("VALIDATED".equals(status)) dto.reportStatus = "已验证";
        else if ("SUBMITTED".equals(status)) dto.reportStatus = "已提交";
        else if ("ACCEPTED".equals(status)) dto.reportStatus = "已接受";
        else if ("REJECTED".equals(status)) dto.reportStatus = "已拒绝";
        else dto.reportStatus = status;
        dto.submissionMethod = entity.getSubmissionMethod();
        dto.acknowledgmentNo = entity.getAcknowledgmentNo();
        dto.createdTime = entity.getCreatedTime() != null ? sdtf.format(entity.getCreatedTime()) : "";
        dto.remark = entity.getRemark();
        return dto;
    }

    public String getReportNo() { return reportNo; }
    public String getReportName() { return reportName; }
    public String getAuthorityName() { return authorityName; }
    public String getReportPeriod() { return reportPeriod; }
    public String getDueDate() { return dueDate; }
    public String getSubmitDate() { return submitDate; }
    public String getReportStatus() { return reportStatus; }
    public String getSubmissionMethod() { return submissionMethod; }
    public String getAcknowledgmentNo() { return acknowledgmentNo; }
    public String getCreatedTime() { return createdTime; }
    public String getRemark() { return remark; }
}

