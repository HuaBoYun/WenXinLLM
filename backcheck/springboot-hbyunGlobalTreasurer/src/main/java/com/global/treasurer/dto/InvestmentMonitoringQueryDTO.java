package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * 投资监控查询DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@ApiModel(value = "InvestmentMonitoringQueryDTO", description = "投资监控查询对象")
@Data
public class InvestmentMonitoringQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("投资ID")
    private Long investmentId;

    @ApiModelProperty("监控类型")
    private String monitoringType;

    @ApiModelProperty("预警级别")
    private String alertLevel;

    @ApiModelProperty("监控状态")
    private String monitoringStatus;

    @ApiModelProperty("开始日期")
    private Date startDate;

    @ApiModelProperty("结束日期")
    private Date endDate;

    // Getter和Setter
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    public Long getInvestmentId() { return investmentId; }
    public void setInvestmentId(Long investmentId) { this.investmentId = investmentId; }
    public String getMonitoringType() { return monitoringType; }
    public void setMonitoringType(String monitoringType) { this.monitoringType = monitoringType; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getMonitoringStatus() { return monitoringStatus; }
    public void setMonitoringStatus(String monitoringStatus) { this.monitoringStatus = monitoringStatus; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
