package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险监控实体类
 *
 * @author 华博云开发团队
 * @since 2025-03-24
 */
@TableName("TBL_RISK_MONITORING")
public class TblRiskMonitoring implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long recordId;

    /** 监控编号 */
    private String monitoringNo;

    /** 风险类型ID */
    private Long riskTypeId;

    /** 风险类型名称 */
    private String riskTypeName;

    /** 风险状态(NORMAL/WARNING/CRITICAL/BREACH) */
    private String riskStatus;

    /** 当前风险值 */
    private BigDecimal currentRiskValue;

    /** 阈值 */
    private BigDecimal thresholdValue;

    /** 预警值 */
    private BigDecimal warningValue;

    /** 临界值 */
    private BigDecimal criticalValue;

    /** 是否触发警报 */
    private Boolean alertTriggered;

    /** 警报消息 */
    private String alertMessage;

    /** 监控日期 */
    private Date monitoringDate;

    /** 下次监控日期 */
    private Date nextMonitoringDate;

    /** 处理措施 */
    private String actionTaken;

    /** 处理人ID */
    private Long handledBy;

    /** 处理时间 */
    private Date handledAt;

    /** 备注 */
    private String remark;

    /** 组织ID */
    private Long orgId;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 创建人 */
    private String createBy;

    /** 更新人 */
    private String updateBy;

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
    public Long getHandledBy() { return handledBy; }
    public void setHandledBy(Long handledBy) { this.handledBy = handledBy; }
    public Date getHandledAt() { return handledAt; }
    public void setHandledAt(Date handledAt) { this.handledAt = handledAt; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}

