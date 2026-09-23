package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除
import java.io.Serializable;
import java.util.Date;

/**
 * 银行连接日志实体类
 * @author AI Assistant
 * @date 2025-01-26
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TC_BANK_CONNECT_LOG")
@ApiModel(value = "TcBankConnectLog", description = "银行连接日志")
public class TcBankConnectLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LOG_ID", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("日志ID")
    private String logId;

    @TableField("REQUEST_ID")
    @ApiModelProperty("请求ID")
    private String requestId;

    @TableField("INTERFACE_TYPE")
    @ApiModelProperty("接口类型：BALANCE_QUERY, TRANSACTION_DETAIL, PAYMENT, TRANSFER, RECONCILIATION")
    private String interfaceType;

    @TableField("REQUEST_TIME")
    @ApiModelProperty("请求时间")
    private Date requestTime;

    @TableField("REQUEST_URL")
    @ApiModelProperty("请求URL")
    private String requestUrl;

    @TableField("REQUEST_METHOD")
    @ApiModelProperty("请求方法：GET, POST")
    private String requestMethod;

    @TableField("REQUEST_HEADERS")
    @ApiModelProperty("请求头(JSON格式)")
    private String requestHeaders;

    @TableField("REQUEST_BODY")
    @ApiModelProperty("请求体(JSON格式)")
    private String requestBody;

    @TableField("RESPONSE_TIME")
    @ApiModelProperty("响应时间")
    private Date responseTime;

    @TableField("RESPONSE_STATUS")
    @ApiModelProperty("响应状态码")
    private Integer responseStatus;

    @TableField("RESPONSE_HEADERS")
    @ApiModelProperty("响应头(JSON格式)")
    private String responseHeaders;

    @TableField("RESPONSE_BODY")
    @ApiModelProperty("响应体(JSON格式)")
    private String responseBody;

    @TableField("IS_SUCCESSFUL")
    @ApiModelProperty("是否成功：1-成功, 0-失败")
    private Integer isSuccessful;

    @TableField("ERROR_MESSAGE")
    @ApiModelProperty("错误信息")
    private String errorMessage;

    @TableField("ELAPSED_TIME")
    @ApiModelProperty("耗时(毫秒)")
    private Integer elapsedTime;

    @TableField("CONNECTION_CONFIG_ID")
    @ApiModelProperty("连接配置ID")
    private Long connectionConfigId;

    @TableField("BANK_NAME")
    @ApiModelProperty("银行名称")
    private String bankName;

    @TableField("ACCOUNT_ID")
    @ApiModelProperty("账户ID")
    private Long accountId;

    @TableField("OPERATION_TYPE")
    @ApiModelProperty("操作类型")
    private String operationType;

    @TableField("CLIENT_IP")
    @ApiModelProperty("客户端IP")
    private String clientIp;

    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @ApiModelProperty("更新时间")
    private Date updateTime;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getInterfaceType() { return interfaceType; }
    public void setInterfaceType(String interfaceType) { this.interfaceType = interfaceType; }
    public Date getRequestTime() { return requestTime; }
    public void setRequestTime(Date requestTime) { this.requestTime = requestTime; }
    public String getRequestUrl() { return requestUrl; }
    public void setRequestUrl(String requestUrl) { this.requestUrl = requestUrl; }
    public String getRequestMethod() { return requestMethod; }
    public void setRequestMethod(String requestMethod) { this.requestMethod = requestMethod; }
    public String getRequestHeaders() { return requestHeaders; }
    public void setRequestHeaders(String requestHeaders) { this.requestHeaders = requestHeaders; }
    public String getRequestBody() { return requestBody; }
    public void setRequestBody(String requestBody) { this.requestBody = requestBody; }
    public Date getResponseTime() { return responseTime; }
    public void setResponseTime(Date responseTime) { this.responseTime = responseTime; }
    public Integer getResponseStatus() { return responseStatus; }
    public void setResponseStatus(Integer responseStatus) { this.responseStatus = responseStatus; }
    public String getResponseHeaders() { return responseHeaders; }
    public void setResponseHeaders(String responseHeaders) { this.responseHeaders = responseHeaders; }
    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
    public Integer getIsSuccessful() { return isSuccessful; }
    public void setIsSuccessful(Integer isSuccessful) { this.isSuccessful = isSuccessful; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Integer getElapsedTime() { return elapsedTime; }
    public void setElapsedTime(Integer elapsedTime) { this.elapsedTime = elapsedTime; }
    public Long getConnectionConfigId() { return connectionConfigId; }
    public void setConnectionConfigId(Long connectionConfigId) { this.connectionConfigId = connectionConfigId; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
