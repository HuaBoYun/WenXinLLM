package com.global.treasurer.dto;

/**
 * 风险监控查询DTO
 */
public class RiskMonitoringQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String monitoringNo;
    private Long riskTypeId;
    private String riskStatus;
    private String monitoringDateStart;
    private String monitoringDateEnd;
    private Long orgId;

    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    public String getMonitoringNo() { return monitoringNo; }
    public void setMonitoringNo(String monitoringNo) { this.monitoringNo = monitoringNo; }
    public Long getRiskTypeId() { return riskTypeId; }
    public void setRiskTypeId(Long riskTypeId) { this.riskTypeId = riskTypeId; }
    public String getRiskStatus() { return riskStatus; }
    public void setRiskStatus(String riskStatus) { this.riskStatus = riskStatus; }
    public String getMonitoringDateStart() { return monitoringDateStart; }
    public void setMonitoringDateStart(String monitoringDateStart) { this.monitoringDateStart = monitoringDateStart; }
    public String getMonitoringDateEnd() { return monitoringDateEnd; }
    public void setMonitoringDateEnd(String monitoringDateEnd) { this.monitoringDateEnd = monitoringDateEnd; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
}

