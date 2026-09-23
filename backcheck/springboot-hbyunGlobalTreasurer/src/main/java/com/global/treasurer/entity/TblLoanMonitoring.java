package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 贷款监控实体类
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@TableName("TBL_LOAN_MONITORING")
public class TblLoanMonitoring implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "MONITORING_ID", type = IdType.ASSIGN_ID)
    private Long monitoringId;

    @TableField("LOAN_ID")
    private String loanId;

    @TableField("ALERT_TYPE")
    private String alertType;

    @TableField("ALERT_LEVEL")
    private String alertLevel;

    @TableField("ALERT_TITLE")
    private String alertTitle;

    @TableField("ALERT_MESSAGE")
    private String alertMessage;

    @TableField("ALERT_DATE")
    private Date alertDate;

    @TableField("DUE_DATE")
    private Date dueDate;

    @TableField("OVERDUE_DAYS")
    private Integer overdueDays;

    @TableField("OVERDUE_AMOUNT")
    private BigDecimal overdueAmount;

    @TableField("ALERT_STATUS")
    private String alertStatus;

    @TableField("HANDLER_ID")
    private Long handlerId;

    @TableField("HANDLER_NAME")
    private String handlerName;

    @TableField("HANDLE_DATE")
    private Date handleDate;

    @TableField("HANDLE_OPINION")
    private String handleOpinion;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private String updatedBy;

    @TableField("UPDATED_TIME")
    private Date updatedTime;

    @TableField("REMARK")
    private String remark;

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
    public Date getAlertDate() { return alertDate; }
    public void setAlertDate(Date alertDate) { this.alertDate = alertDate; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
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
    public Date getHandleDate() { return handleDate; }
    public void setHandleDate(Date handleDate) { this.handleDate = handleDate; }
    public String getHandleOpinion() { return handleOpinion; }
    public void setHandleOpinion(String handleOpinion) { this.handleOpinion = handleOpinion; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

