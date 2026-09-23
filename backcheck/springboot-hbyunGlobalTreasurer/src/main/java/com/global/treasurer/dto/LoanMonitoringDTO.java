package com.global.treasurer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款监控DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
public class LoanMonitoringDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long monitoringId;
    private String loanId;
    private String alertType;
    private String alertLevel;
    private String alertTitle;
    private String alertMessage;
    private String alertDate;
    private String dueDate;
    private Integer overdueDays;
    private BigDecimal overdueAmount;
    private String alertStatus;
    private Long handlerId;
    private String handlerName;
    private String handleDate;
    private String handleOpinion;
    private Long companyId;
    private String companyName;
    private String remark;

    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    // Getters and Setters
    public Long getMonitoringId() { return monitoringId; }
    public void setMonitoringId(Long monitoringId) { this.monitoringId = monitoringId; }
    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getAlertTitle() { return alertTitle; }
    public void setAlertTitle(String alertTitle) { this.alertTitle = alertTitle; }
    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public String getAlertDate() { return alertDate; }
    public void setAlertDate(String alertDate) { this.alertDate = alertDate; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }
    public BigDecimal getOverdueAmount() { return overdueAmount; }
    public void setOverdueAmount(BigDecimal overdueAmount) { this.overdueAmount = overdueAmount; }
    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }
    public Long getHandlerId() { return handlerId; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }
    public String getHandlerName() { return handlerName; }
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    public String getHandleDate() { return handleDate; }
    public void setHandleDate(String handleDate) { this.handleDate = handleDate; }
    public String getHandleOpinion() { return handleOpinion; }
    public void setHandleOpinion(String handleOpinion) { this.handleOpinion = handleOpinion; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}

