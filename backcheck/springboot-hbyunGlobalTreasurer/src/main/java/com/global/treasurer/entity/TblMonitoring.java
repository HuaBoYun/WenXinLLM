package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_MONITORING")
public class TblMonitoring implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long monitoringId;
    
    private String monitorType;
    private String monitorName;
    private String alertLevel;
    private String alertMessage;
    private Integer transactionCount;
    private Integer successCount;
    private Integer failedCount;
    private Double successRate;
    private Integer avgProcessingTime;
    private Integer maxProcessingTime;
    private Integer minProcessingTime;
    private java.math.BigDecimal totalAmount;
    private Double healthScore;
    private Double systemLoad;
    private Double memoryUsage;
    private Double cpuUsage;
    private Double diskUsage;
    private Integer networkLatency;
    private Integer isAlertSent;
    private Date alertSentTime;
    private Date monitorTime;
    private String monitorDescription;
    private String remark;
    private Integer deleteFlag;
    @TableField("CREATE_BY")
    private Long createdBy;
    @TableField("CREATE_BY_NAME")
    private String createdByName;
    @TableField("CREATE_TIME")
    private Date createdTime;
    @TableField("UPDATE_BY")
    private Long updatedBy;
    @TableField("UPDATE_BY_NAME")
    private String updatedByName;
    @TableField("UPDATE_TIME")
    private Date updatedTime;
    private Long orgId;
    private String orgName;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getMonitoringId() { return monitoringId; }
    public void setMonitoringId(Long monitoringId) { this.monitoringId = monitoringId; }
    public String getMonitorType() { return monitorType; }
    public void setMonitorType(String monitorType) { this.monitorType = monitorType; }
    public String getMonitorName() { return monitorName; }
    public void setMonitorName(String monitorName) { this.monitorName = monitorName; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public Integer getTransactionCount() { return transactionCount; }
    public void setTransactionCount(Integer transactionCount) { this.transactionCount = transactionCount; }
    public Integer getSuccessCount() { return successCount; }
    public void setSuccessCount(Integer successCount) { this.successCount = successCount; }
    public Integer getFailedCount() { return failedCount; }
    public void setFailedCount(Integer failedCount) { this.failedCount = failedCount; }
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    public Integer getAvgProcessingTime() { return avgProcessingTime; }
    public void setAvgProcessingTime(Integer avgProcessingTime) { this.avgProcessingTime = avgProcessingTime; }
    public Integer getMaxProcessingTime() { return maxProcessingTime; }
    public void setMaxProcessingTime(Integer maxProcessingTime) { this.maxProcessingTime = maxProcessingTime; }
    public Integer getMinProcessingTime() { return minProcessingTime; }
    public void setMinProcessingTime(Integer minProcessingTime) { this.minProcessingTime = minProcessingTime; }
    public java.math.BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Double getHealthScore() { return healthScore; }
    public void setHealthScore(Double healthScore) { this.healthScore = healthScore; }
    public Double getSystemLoad() { return systemLoad; }
    public void setSystemLoad(Double systemLoad) { this.systemLoad = systemLoad; }
    public Double getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(Double memoryUsage) { this.memoryUsage = memoryUsage; }
    public Double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; }
    public Double getDiskUsage() { return diskUsage; }
    public void setDiskUsage(Double diskUsage) { this.diskUsage = diskUsage; }
    public Integer getNetworkLatency() { return networkLatency; }
    public void setNetworkLatency(Integer networkLatency) { this.networkLatency = networkLatency; }
    public Integer getIsAlertSent() { return isAlertSent; }
    public void setIsAlertSent(Integer isAlertSent) { this.isAlertSent = isAlertSent; }
    public Date getAlertSentTime() { return alertSentTime; }
    public void setAlertSentTime(Date alertSentTime) { this.alertSentTime = alertSentTime; }
    public Date getMonitorTime() { return monitorTime; }
    public void setMonitorTime(Date monitorTime) { this.monitorTime = monitorTime; }
    public String getMonitorDescription() { return monitorDescription; }
    public void setMonitorDescription(String monitorDescription) { this.monitorDescription = monitorDescription; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

}
