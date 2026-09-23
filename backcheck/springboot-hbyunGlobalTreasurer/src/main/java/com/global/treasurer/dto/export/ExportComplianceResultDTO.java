package com.global.treasurer.dto.export;

import com.global.treasurer.entity.TblComplianceResult;
import com.global.treasurer.util.excel.annotation.ExcelField;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 合规检查结果导出DTO
 */
public class ExportComplianceResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "规则ID", sort = 1, words = 20)
    private String ruleId;

    @ExcelField(title = "报告ID", sort = 2, words = 20)
    private String reportId;

    @ExcelField(title = "检查时间", sort = 3, words = 18)
    private String checkTime;

    @ExcelField(title = "检查结果", sort = 4, words = 10)
    private String isPassed;

    @ExcelField(title = "检查状态", sort = 5, words = 10)
    private String checkStatus;

    @ExcelField(title = "严重级别", sort = 6, words = 10)
    private String severityLevel;

    @ExcelField(title = "违规详情", sort = 7, words = 40)
    private String violationDetails;

    @ExcelField(title = "采取措施", sort = 8, words = 30)
    private String actionTaken;

    @ExcelField(title = "是否升级", sort = 9, words = 10)
    private String isEscalated;

    @ExcelField(title = "解决时间", sort = 10, words = 18)
    private String resolvedTime;

    @ExcelField(title = "创建时间", sort = 11, words = 18)
    private String createdTime;

    @ExcelField(title = "备注", sort = 12, words = 30)
    private String remark;

    public static ExportComplianceResultDTO fromEntity(TblComplianceResult e) {
        if (e == null) return null;
        ExportComplianceResultDTO dto = new ExportComplianceResultDTO();
        SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.ruleId = e.getRuleId();
        dto.reportId = e.getReportId();
        dto.checkTime = e.getCheckTime() != null ? sdtf.format(e.getCheckTime()) : "";
        dto.isPassed = Integer.valueOf(1).equals(e.getIsPassed()) ? "通过" : "未通过";
        // 检查状态
        String cs = e.getCheckStatus();
        if ("PENDING".equals(cs)) dto.checkStatus = "待检查";
        else if ("CHECKING".equals(cs)) dto.checkStatus = "检查中";
        else if ("COMPLETED".equals(cs)) dto.checkStatus = "已完成";
        else if ("FAILED".equals(cs)) dto.checkStatus = "检查失败";
        else if ("CANCELLED".equals(cs)) dto.checkStatus = "已取消";
        else if ("PROCESSED".equals(cs)) dto.checkStatus = "已处理";
        else if ("RESOLVED".equals(cs)) dto.checkStatus = "已解决";
        else if ("ESCALATED".equals(cs)) dto.checkStatus = "已升级";
        else dto.checkStatus = cs;
        // 严重级别
        String sl = e.getSeverityLevel();
        if ("LOW".equals(sl)) dto.severityLevel = "低";
        else if ("MEDIUM".equals(sl)) dto.severityLevel = "中";
        else if ("HIGH".equals(sl)) dto.severityLevel = "高";
        else if ("CRITICAL".equals(sl)) dto.severityLevel = "严重";
        else dto.severityLevel = sl;
        dto.violationDetails = e.getViolationDetails();
        dto.actionTaken = e.getActionTaken();
        dto.isEscalated = Integer.valueOf(1).equals(e.getIsEscalated()) ? "已升级" : "未升级";
        dto.resolvedTime = e.getResolvedTime() != null ? sdtf.format(e.getResolvedTime()) : "";
        dto.createdTime = e.getCreatedTime() != null ? sdtf.format(e.getCreatedTime()) : "";
        dto.remark = e.getRemark();
        return dto;
    }

    public String getRuleId() { return ruleId; }
    public String getReportId() { return reportId; }
    public String getCheckTime() { return checkTime; }
    public String getIsPassed() { return isPassed; }
    public String getCheckStatus() { return checkStatus; }
    public String getSeverityLevel() { return severityLevel; }
    public String getViolationDetails() { return violationDetails; }
    public String getActionTaken() { return actionTaken; }
    public String getIsEscalated() { return isEscalated; }
    public String getResolvedTime() { return resolvedTime; }
    public String getCreatedTime() { return createdTime; }
    public String getRemark() { return remark; }
}

