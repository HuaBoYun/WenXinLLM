package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算Web服务集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_WEBSERVICE_INTEGRATION")
public class BudgetWebServiceIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Web服务集成ID (主键)
     */
    @TableId(value = "WS_ID", type = IdType.ASSIGN_UUID)
    private String wsId;

    /**
     * Web服务编码
     */
    @TableField("WS_CODE")
    private String wsCode;

    /**
     * Web服务名称
     */
    @TableField("WS_NAME")
    private String wsName;

    /**
     * Web服务类型 (SOAP/REST)
     */
    @TableField("WS_TYPE")
    private String wsType;

    /**
     * WSDL地址(SOAP)
     */
    @TableField("WSDL_URL")
    private String wsdlUrl;

    /**
     * 服务端点地址
     */
    @TableField("ENDPOINT_URL")
    private String endpointUrl;

    /**
     * 命名空间(SOAP)
     */
    @TableField("NAMESPACE")
    private String namespace;

    /**
     * 服务方法名
     */
    @TableField("METHOD_NAME")
    private String methodName;

    /**
     * 认证方式 (NONE/BASIC/WSSE/OAUTH2)
     */
    @TableField("AUTH_TYPE")
    private String authType;

    /**
     * 认证配置 (JSON格式)
     */
    @TableField("AUTH_CONFIG")
    private String authConfig;

    /**
     * SOAP头配置 (XML格式)
     */
    @TableField("SOAP_HEADER")
    private String soapHeader;

    /**
     * 请求参数配置 (JSON格式)
     */
    @TableField("REQUEST_PARAMS")
    private String requestParams;

    /**
     * 请求体模板 (XML/JSON格式)
     */
    @TableField("REQUEST_BODY_TEMPLATE")
    private String requestBodyTemplate;

    /**
     * 响应数据映射 (JSON格式)
     */
    @TableField("RESPONSE_MAPPING")
    private String responseMapping;

    /**
     * 超时时间(秒)
     */
    @TableField("TIMEOUT_SECONDS")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @TableField("RETRY_COUNT")
    private Integer retryCount;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 调用频率 (REALTIME/SCHEDULED/MANUAL)
     */
    @TableField("CALL_FREQUENCY")
    private String callFrequency;

    /**
     * 调用时间配置 (CRON表达式)
     */
    @TableField("CALL_SCHEDULE")
    private String callSchedule;

    /**
     * 最后调用时间
     */
    @TableField("LAST_CALL_TIME")
    private Date lastCallTime;

    /**
     * 调用成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 调用失败次数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 平均响应时间(毫秒)
     */
    @TableField("AVG_RESPONSE_TIME")
    private Integer avgResponseTime;

    /**
     * 集成状态 (ACTIVE/INACTIVE/ERROR/TESTING)
     */
    @TableField("INTEGRATION_STATUS")
    private String integrationStatus;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;
}

