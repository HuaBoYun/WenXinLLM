package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@TableName("TBL_INVESTMENT_MONITORING")
@ApiModel(value = "TblInvestmentMonitoring", description = "投资监控")
public class TblInvestmentMonitoring implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "MONITORING_ID", type = IdType.ASSIGN_ID)
    @ApiModelProperty("监控ID")
    private Long monitoringId;

    @TableField("INVESTMENT_ID")
    @ApiModelProperty("投资ID")
    private Long investmentId;

    @TableField("MONITORING_DATE")
    @ApiModelProperty("监控日期")
    private Date monitoringDate;

    @TableField("MONITORING_TYPE")
    @ApiModelProperty("监控类型")
    private String monitoringType;

    @TableField("PREVIOUS_VALUE")
    @ApiModelProperty("前期价值")
    private java.math.BigDecimal previousValue;

    @TableField("CURRENT_VALUE")
    @ApiModelProperty("当前价值")
    private java.math.BigDecimal currentValue;

    @TableField("VALUE_CHANGE")
    @ApiModelProperty("价值变化")
    private java.math.BigDecimal valueChange;

    @TableField("VALUE_CHANGE_RATE")
    @ApiModelProperty("价值变化率")
    private java.math.BigDecimal valueChangeRate;

    @TableField("PERFORMANCE_SCORE")
    @ApiModelProperty("绩效评分")
    private Integer performanceScore;

    @TableField("RISK_SCORE")
    @ApiModelProperty("风险评分")
    private Integer riskScore;

    @TableField("ALERT_LEVEL")
    @ApiModelProperty("预警级别")
    private String alertLevel;

    @TableField("ALERT_MESSAGE")
    @ApiModelProperty("预警信息")
    private String alertMessage;

    @TableField("ALERT_TYPE")
    @ApiModelProperty("预警类型")
    private String alertType;

    @TableField("ALERT_STATUS")
    @ApiModelProperty("预警状态")
    private String alertStatus;

    @TableField("MONITORING_STATUS")
    @ApiModelProperty("监控状态")
    private String monitoringStatus;

    @TableField("MONITORING_NOTES")
    @ApiModelProperty("监控备注")
    private String monitoringNotes;

    @TableField("INVESTMENT_TYPE")
    @ApiModelProperty("投资类型")
    private String investmentType;

    @TableField(value = "INVESTMENT_TARGET", exist = false)
    @ApiModelProperty("投资目标")
    private String investmentTarget;

    @TableField(value = "MONITORING_INDICATOR", exist = false)
    @ApiModelProperty("监控指标")
    private String monitoringIndicator;

    @TableField(value = "ALERT_THRESHOLD", exist = false)
    @ApiModelProperty("告警阈值")
    private java.math.BigDecimal alertThreshold;

    @TableField(value = "RISK_LEVEL", exist = false)
    @ApiModelProperty("风险等级")
    private String riskLevel;

    @TableField(value = "ALERT_TIME", exist = false)
    @ApiModelProperty("预警时间")
    private Timestamp alertTime;

    @TableField(value = "HANDLE_STATUS", exist = false)
    @ApiModelProperty("处理状态")
    private String handleStatus;

    @TableField(value = "HANDLE_RESULT", exist = false)
    @ApiModelProperty("处理结果")
    private String handleResult;

    @TableField(value = "HANDLE_TIME", exist = false)
    @ApiModelProperty("处理时间")
    private Timestamp handleTime;

    @TableField(value = "HANDLER_ID", exist = false)
    @ApiModelProperty("处理人ID")
    private Long handlerId;

    @TableField(value = "HANDLER_NAME", exist = false)
    @ApiModelProperty("处理人姓名")
    private String handlerName;

    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private Timestamp updateTime;

    @TableField("CREATED_BY")
    @ApiModelProperty("创建人")
    private Long createUser;

    @TableField("UPDATED_BY")
    @ApiModelProperty("更新人")
    private Long updateUser;

    @TableField("CREATED_BY_NAME")
    @ApiModelProperty("创建人姓名")
    private String createdByName;

    @TableField("UPDATED_BY_NAME")
    @ApiModelProperty("更新人姓名")
    private String updatedByName;

    @TableField("COMPANY_ID")
    @ApiModelProperty("公司ID")
    private Long companyId;

    @TableField("COMPANY_NAME")
    @ApiModelProperty("公司名称")
    private String companyName;

    @TableField("DELETE_FLAG")
    @ApiModelProperty("删除标志")
    private Integer deleteFlag;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    // Getter和Setter
    public Long getMonitoringId() { return monitoringId; }
    public void setMonitoringId(Long monitoringId) { this.monitoringId = monitoringId; }
    public Long getInvestmentId() { return investmentId; }
    public void setInvestmentId(Long investmentId) { this.investmentId = investmentId; }
    public Date getMonitoringDate() { return monitoringDate; }
    public void setMonitoringDate(Date monitoringDate) { this.monitoringDate = monitoringDate; }
    public String getMonitoringType() { return monitoringType; }
    public void setMonitoringType(String monitoringType) { this.monitoringType = monitoringType; }
    public java.math.BigDecimal getPreviousValue() { return previousValue; }
    public void setPreviousValue(java.math.BigDecimal previousValue) { this.previousValue = previousValue; }
    public java.math.BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(java.math.BigDecimal currentValue) { this.currentValue = currentValue; }
    public java.math.BigDecimal getValueChange() { return valueChange; }
    public void setValueChange(java.math.BigDecimal valueChange) { this.valueChange = valueChange; }
    public java.math.BigDecimal getValueChangeRate() { return valueChangeRate; }
    public void setValueChangeRate(java.math.BigDecimal valueChangeRate) { this.valueChangeRate = valueChangeRate; }
    public Integer getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(Integer performanceScore) { this.performanceScore = performanceScore; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }
    public String getMonitoringStatus() { return monitoringStatus; }
    public void setMonitoringStatus(String monitoringStatus) { this.monitoringStatus = monitoringStatus; }
    public String getMonitoringNotes() { return monitoringNotes; }
    public void setMonitoringNotes(String monitoringNotes) { this.monitoringNotes = monitoringNotes; }
    public String getInvestmentType() { return investmentType; }
    public void setInvestmentType(String investmentType) { this.investmentType = investmentType; }
    public String getInvestmentTarget() { return investmentTarget; }
    public void setInvestmentTarget(String investmentTarget) { this.investmentTarget = investmentTarget; }
    public String getMonitoringIndicator() { return monitoringIndicator; }
    public void setMonitoringIndicator(String monitoringIndicator) { this.monitoringIndicator = monitoringIndicator; }
    public java.math.BigDecimal getAlertThreshold() { return alertThreshold; }
    public void setAlertThreshold(java.math.BigDecimal alertThreshold) { this.alertThreshold = alertThreshold; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }
    public Timestamp getUpdateTime() { return updateTime; }
    public void setUpdateTime(Timestamp updateTime) { this.updateTime = updateTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
}
