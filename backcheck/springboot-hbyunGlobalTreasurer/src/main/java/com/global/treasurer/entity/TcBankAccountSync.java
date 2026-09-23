package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 银企直连账户同步实体类
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除

@TableName("TC_BANK_ACCOUNT_SYNC")
public class TcBankAccountSync implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

    /**
     * 账户ID
     */
    @TableField("ACCOUNT_ID")
    private String accountId;

    /**
     * 银行代码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 同步类型
     */
    @TableField("SYNC_TYPE")
    private String syncType;

    /**
     * 同步状态(PENDING-待同步,SYNCING-同步中,SUCCESS-成功,FAILED-失败)
     */
    @TableField("SYNC_STATUS")
    private String syncStatus;

    /**
     * 同步请求时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("SYNC_REQUEST_TIME")
    private Date syncRequestTime;

    /**
     * 同步响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("SYNC_RESPONSE_TIME")
    private Date syncResponseTime;

    /**
     * 同步耗时(毫秒)
     */
    @TableField("SYNC_DURATION")
    private Integer syncDuration;

    /**
     * 请求数据
     */
    @TableField("REQUEST_DATA")
    private String requestData;

    /**
     * 响应数据
     */
    @TableField("RESPONSE_DATA")
    private String responseData;

    /**
     * 错误代码
     */
    @TableField("ERROR_CODE")
    private String errorCode;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 重试次数
     */
    @TableField("RETRY_COUNT")
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    @TableField("MAX_RETRY_COUNT")
    private Integer maxRetryCount;

    /**
     * 下次重试时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("NEXT_RETRY_TIME")
    private Date nextRetryTime;

    /**
     * 状态(1-启用,0-停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Integer versionNo;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getSyncType() { return syncType; }
    public void setSyncType(String syncType) { this.syncType = syncType; }
    public String getSyncStatus() { return syncStatus; }
    public void setSyncStatus(String syncStatus) { this.syncStatus = syncStatus; }
    public Date getSyncRequestTime() { return syncRequestTime; }
    public void setSyncRequestTime(Date syncRequestTime) { this.syncRequestTime = syncRequestTime; }
    public Date getSyncResponseTime() { return syncResponseTime; }
    public void setSyncResponseTime(Date syncResponseTime) { this.syncResponseTime = syncResponseTime; }
    public Integer getSyncDuration() { return syncDuration; }
    public void setSyncDuration(Integer syncDuration) { this.syncDuration = syncDuration; }
    public String getRequestData() { return requestData; }
    public void setRequestData(String requestData) { this.requestData = requestData; }
    public String getResponseData() { return responseData; }
    public void setResponseData(String responseData) { this.responseData = responseData; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Integer getMaxRetryCount() { return maxRetryCount; }
    public void setMaxRetryCount(Integer maxRetryCount) { this.maxRetryCount = maxRetryCount; }
    public Date getNextRetryTime() { return nextRetryTime; }
    public void setNextRetryTime(Date nextRetryTime) { this.nextRetryTime = nextRetryTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

}
