package com.global.treasurer.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险监控DTO
 */
public class RiskMonitoringDTO {
    private Long recordId;
    private String monitoringNo;
    private Long riskTypeId;
    private String riskTypeName;
    private String riskStatus;
    private BigDecimal currentRiskValue;
    private BigDecimal thresholdValue;
    private BigDecimal warningValue;
    private BigDecimal criticalValue;
    private Boolean alertTriggered;
    private String alertMessage;
    private Date monitoringDate;
    private Date nextMonitoringDate;
    private String actionTaken;
    private String remark;
    private Long orgId;

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public String getMonitoringNo() { return monitoringNo; }
    public void setMonitoringNo(String monitoringNo) { this.monitoringNo = monitoringNo; }
    public Long getRiskTypeId() { return riskTypeId; }
    public void setRiskTypeId(Long riskTypeId) { this.riskTypeId = riskTypeId; }
    public String getRiskTypeName() { return riskTypeName; }
    public void setRiskTypeName(String riskTypeName) { this.riskTypeName = riskTypeName; }
    public String getRiskStatus() { return riskStatus; }
    public void setRiskStatus(String riskStatus) { this.riskStatus = riskStatus; }
    public BigDecimal getCurrentRiskValue() { return currentRiskValue; }
    public void setCurrentRiskValue(BigDecimal currentRiskValue) { this.currentRiskValue = currentRiskValue; }
    public BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    public BigDecimal getWarningValue() { return warningValue; }
    public void setWarningValue(BigDecimal warningValue) { this.warningValue = warningValue; }
    public BigDecimal getCriticalValue() { return criticalValue; }
    public void setCriticalValue(BigDecimal criticalValue) { this.criticalValue = criticalValue; }
    public Boolean getAlertTriggered() { return alertTriggered; }
    public void setAlertTriggered(Boolean alertTriggered) { this.alertTriggered = alertTriggered; }
    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public Date getMonitoringDate() { return monitoringDate; }
    public void setMonitoringDate(Date monitoringDate) { this.monitoringDate = monitoringDate; }
    public Date getNextMonitoringDate() { return nextMonitoringDate; }
    public void setNextMonitoringDate(Date nextMonitoringDate) { this.nextMonitoringDate = nextMonitoringDate; }
    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
}

