package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_PROCESSING_RECORD")
public class TblProcessingRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long recordId;

    private Long pendingId;
    private String businessNo;
    private String batchNo;
    private String processType;
    private String processingStatus;
    private Long bankInterfaceId;
    private String bankTransactionNo;
    private Integer transactionCount;
    private Integer successCount;
    private Integer failedCount;
    private Integer avgProcessingTime;
    private Integer processingDuration;
    private Date startTime;
    private Date endTime;
    private String errorCode;
    private String errorMessage;
    private Integer maxRetryCount;
    private Integer retryCount;
    private Date nextRetryTime;
    private String bankRequest;
    private String bankResponse;
    private String recordDescription;
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


    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Long getPendingId() { return pendingId; }
    public void setPendingId(Long pendingId) { this.pendingId = pendingId; }
    public String getBusinessNo() { return businessNo; }
    public void setBusinessNo(String businessNo) { this.businessNo = businessNo; }
    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }
    public String getProcessType() { return processType; }
    public void setProcessType(String processType) { this.processType = processType; }
    public String getProcessingStatus() { return processingStatus; }
    public void setProcessingStatus(String processingStatus) { this.processingStatus = processingStatus; }
    public Long getBankInterfaceId() { return bankInterfaceId; }
    public void setBankInterfaceId(Long bankInterfaceId) { this.bankInterfaceId = bankInterfaceId; }
    public String getBankTransactionNo() { return bankTransactionNo; }
    public void setBankTransactionNo(String bankTransactionNo) { this.bankTransactionNo = bankTransactionNo; }
    public Integer getTransactionCount() { return transactionCount; }
    public void setTransactionCount(Integer transactionCount) { this.transactionCount = transactionCount; }
    public Integer getSuccessCount() { return successCount; }
    public void setSuccessCount(Integer successCount) { this.successCount = successCount; }
    public Integer getFailedCount() { return failedCount; }
    public void setFailedCount(Integer failedCount) { this.failedCount = failedCount; }
    public Integer getAvgProcessingTime() { return avgProcessingTime; }
    public void setAvgProcessingTime(Integer avgProcessingTime) { this.avgProcessingTime = avgProcessingTime; }
    public Integer getProcessingDuration() { return processingDuration; }
    public void setProcessingDuration(Integer processingDuration) { this.processingDuration = processingDuration; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Integer getMaxRetryCount() { return maxRetryCount; }
    public void setMaxRetryCount(Integer maxRetryCount) { this.maxRetryCount = maxRetryCount; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Date getNextRetryTime() { return nextRetryTime; }
    public void setNextRetryTime(Date nextRetryTime) { this.nextRetryTime = nextRetryTime; }
    public String getBankRequest() { return bankRequest; }
    public void setBankRequest(String bankRequest) { this.bankRequest = bankRequest; }
    public String getBankResponse() { return bankResponse; }
    public void setBankResponse(String bankResponse) { this.bankResponse = bankResponse; }
    public String getRecordDescription() { return recordDescription; }
    public void setRecordDescription(String recordDescription) { this.recordDescription = recordDescription; }
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
