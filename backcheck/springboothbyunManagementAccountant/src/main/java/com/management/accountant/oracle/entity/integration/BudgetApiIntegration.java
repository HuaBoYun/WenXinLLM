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
 * 预算API接口集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_API_INTEGRATION")
public class BudgetApiIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * API集成ID (主键)
     */
    @TableId(value = "API_ID", type = IdType.ASSIGN_UUID)
    private String apiId;

    /**
     * API编码
     */
    @TableField("API_CODE")
    private String apiCode;

    /**
     * API名称
     */
    @TableField("API_NAME")
    private String apiName;

    /**
     * API类型 (REST/SOAP/GRAPHQL)
     */
    @TableField("API_TYPE")
    private String apiType;

    /**
     * API地址
     */
    @TableField("API_URL")
    private String apiUrl;

    /**
     * 请求方法 (GET/POST/PUT/DELETE)
     */
    @TableField("REQUEST_METHOD")
    private String requestMethod;

    /**
     * 认证方式 (NONE/BASIC/BEARER/OAUTH2/API_KEY)
     */
    @TableField("AUTH_TYPE")
    private String authType;

    /**
     * 认证配置 (JSON格式)
     */
    @TableField("AUTH_CONFIG")
    private String authConfig;

    /**
     * 请求头配置 (JSON格式)
     */
    @TableField("REQUEST_HEADERS")
    private String requestHeaders;

    /**
     * 请求参数配置 (JSON格式)
     */
    @TableField("REQUEST_PARAMS")
    private String requestParams;

    /**
     * 请求体模板 (JSON格式)
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
     * 调用频率限制(次/分钟)
     */
    @TableField("RATE_LIMIT")
    private Integer rateLimit;

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
     * API状态 (ACTIVE/INACTIVE/ERROR)
     */
    @TableField("API_STATUS")
    private String apiStatus;

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

