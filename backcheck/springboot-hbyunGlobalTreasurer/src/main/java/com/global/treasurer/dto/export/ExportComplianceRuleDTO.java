package com.global.treasurer.dto.export;

import com.global.treasurer.entity.TblComplianceRule;
import com.global.treasurer.util.excel.annotation.ExcelField;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 合规检查规则导出DTO
 */
public class ExportComplianceRuleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "规则编码", sort = 1, words = 15)
    private String ruleCode;

    @ExcelField(title = "规则名称", sort = 2, words = 25)
    private String ruleName;

    @ExcelField(title = "规则类型", sort = 3, words = 15)
    private String ruleType;

    @ExcelField(title = "严重程度", sort = 4, words = 10)
    private String severityLevel;

    @ExcelField(title = "检查频率", sort = 5, words = 10)
    private String checkFrequency;

    @ExcelField(title = "检查范围", sort = 6, words = 20)
    private String checkScope;

    @ExcelField(title = "阈值", sort = 7, words = 12)
    private String thresholdValue;

    @ExcelField(title = "警告阈值", sort = 8, words = 12)
    private String warningThreshold;

    @ExcelField(title = "规则条件", sort = 9, words = 30)
    private String ruleCondition;

    @ExcelField(title = "法规依据", sort = 10, words = 20)
    private String regulationReference;

    @ExcelField(title = "生效日期", sort = 11, words = 12)
    private String effectiveDate;

    @ExcelField(title = "失效日期", sort = 12, words = 12)
    private String expiryDate;

    @ExcelField(title = "状态", sort = 13, words = 8)
    private String isEnabled;

    @ExcelField(title = "创建时间", sort = 14, words = 18)
    private String createdTime;

    @ExcelField(title = "备注", sort = 15, words = 30)
    private String remark;

    public static ExportComplianceRuleDTO fromEntity(TblComplianceRule e) {
        if (e == null) return null;
        ExportComplianceRuleDTO dto = new ExportComplianceRuleDTO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.ruleCode = e.getRuleCode();
        dto.ruleName = e.getRuleName();
        // 规则类型
        String rt = e.getRuleType();
        if ("LIMIT_CHECK".equals(rt)) dto.ruleType = "限额检查";
        else if ("RATIO_CHECK".equals(rt)) dto.ruleType = "比率检查";
        else if ("THRESHOLD_CHECK".equals(rt)) dto.ruleType = "阈值检查";
        else if ("BUSINESS_RULE".equals(rt)) dto.ruleType = "业务规则";
        else if ("DATA_QUALITY".equals(rt)) dto.ruleType = "数据质量";
        else dto.ruleType = rt;
        // 严重程度
        String sl = e.getSeverityLevel();
        if ("LOW".equals(sl)) dto.severityLevel = "低";
        else if ("MEDIUM".equals(sl)) dto.severityLevel = "中";
        else if ("HIGH".equals(sl)) dto.severityLevel = "高";
        else if ("CRITICAL".equals(sl)) dto.severityLevel = "严重";
        else dto.severityLevel = sl;
        // 检查频率
        String cf = e.getCheckFrequency();
        if ("REAL_TIME".equals(cf)) dto.checkFrequency = "实时";
        else if ("HOURLY".equals(cf)) dto.checkFrequency = "每小时";
        else if ("DAILY".equals(cf)) dto.checkFrequency = "每日";
        else if ("WEEKLY".equals(cf)) dto.checkFrequency = "每周";
        else if ("MONTHLY".equals(cf)) dto.checkFrequency = "每月";
        else dto.checkFrequency = cf;
        dto.checkScope = e.getCheckScope();
        dto.thresholdValue = e.getThresholdValue() != null ? e.getThresholdValue().toPlainString() : "";
        dto.warningThreshold = e.getWarningThreshold() != null ? e.getWarningThreshold().toPlainString() : "";
        dto.ruleCondition = e.getRuleCondition();
        dto.regulationReference = e.getRegulationReference();
        dto.effectiveDate = e.getEffectiveDate() != null ? sdf.format(e.getEffectiveDate()) : "";
        dto.expiryDate = e.getExpiryDate() != null ? sdf.format(e.getExpiryDate()) : "";
        dto.isEnabled = Integer.valueOf(1).equals(e.getIsEnabled()) ? "启用" : "停用";
        dto.createdTime = e.getCreatedTime() != null ? sdtf.format(e.getCreatedTime()) : "";
        dto.remark = e.getRemark();
        return dto;
    }

    public String getRuleCode() { return ruleCode; }
    public String getRuleName() { return ruleName; }
    public String getRuleType() { return ruleType; }
    public String getSeverityLevel() { return severityLevel; }
    public String getCheckFrequency() { return checkFrequency; }
    public String getCheckScope() { return checkScope; }
    public String getThresholdValue() { return thresholdValue; }
    public String getWarningThreshold() { return warningThreshold; }
    public String getRuleCondition() { return ruleCondition; }
    public String getRegulationReference() { return regulationReference; }
    public String getEffectiveDate() { return effectiveDate; }
    public String getExpiryDate() { return expiryDate; }
    public String getIsEnabled() { return isEnabled; }
    public String getCreatedTime() { return createdTime; }
    public String getRemark() { return remark; }
}

