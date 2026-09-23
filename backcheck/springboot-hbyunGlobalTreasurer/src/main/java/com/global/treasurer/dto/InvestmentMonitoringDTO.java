package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * 投资监控DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@ApiModel(value = "InvestmentMonitoringDTO", description = "投资监控传输对象")
@Data
public class InvestmentMonitoringDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("监控ID")
    private Long monitoringId;

    @ApiModelProperty("投资ID")
    private Long investmentId;

    @ApiModelProperty("监控日期")
    private Date monitoringDate;

    @ApiModelProperty("监控类型")
    private String monitoringType;

    @ApiModelProperty("前期价值")
    private BigDecimal previousValue;

    @ApiModelProperty("当前价值")
    private BigDecimal currentValue;

    @ApiModelProperty("价值变化")
    private BigDecimal valueChange;

    @ApiModelProperty("价值变化率")
    private BigDecimal valueChangeRate;

    @ApiModelProperty("绩效评分")
    private Integer performanceScore;

    @ApiModelProperty("风险评分")
    private Integer riskScore;

    @ApiModelProperty("预警级别")
    private String alertLevel;

    @ApiModelProperty("预警信息")
    private String alertMessage;

    @ApiModelProperty("监控状态")
    private String monitoringStatus;

    @ApiModelProperty("监控备注")
    private String monitoringNotes;

    @ApiModelProperty("告警类型")
    private String alertType;

    @ApiModelProperty("告警状态")
    private String alertStatus;

    @ApiModelProperty("投资类型")
    private String investmentType;

    // Getter和Setter
    public Long getMonitoringId() { return monitoringId; }
    public void setMonitoringId(Long monitoringId) { this.monitoringId = monitoringId; }
    public Long getInvestmentId() { return investmentId; }
    public void setInvestmentId(Long investmentId) { this.investmentId = investmentId; }
    public Date getMonitoringDate() { return monitoringDate; }
    public void setMonitoringDate(Date monitoringDate) { this.monitoringDate = monitoringDate; }
    public String getMonitoringType() { return monitoringType; }
    public void setMonitoringType(String monitoringType) { this.monitoringType = monitoringType; }
    public BigDecimal getPreviousValue() { return previousValue; }
    public void setPreviousValue(BigDecimal previousValue) { this.previousValue = previousValue; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public BigDecimal getValueChange() { return valueChange; }
    public void setValueChange(BigDecimal valueChange) { this.valueChange = valueChange; }
    public BigDecimal getValueChangeRate() { return valueChangeRate; }
    public void setValueChangeRate(BigDecimal valueChangeRate) { this.valueChangeRate = valueChangeRate; }
    public Integer getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(Integer performanceScore) { this.performanceScore = performanceScore; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public String getMonitoringStatus() { return monitoringStatus; }
    public void setMonitoringStatus(String monitoringStatus) { this.monitoringStatus = monitoringStatus; }
    public String getMonitoringNotes() { return monitoringNotes; }
    public void setMonitoringNotes(String monitoringNotes) { this.monitoringNotes = monitoringNotes; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }
    public String getInvestmentType() { return investmentType; }
    public void setInvestmentType(String investmentType) { this.investmentType = investmentType; }
}
