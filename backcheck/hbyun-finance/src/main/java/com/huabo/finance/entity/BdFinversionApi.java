package com.huabo.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务系统API配置
 * </p>
 *
 * @author Augment Code
 * @since 2026-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("BD_FINVERSION_API")
@Schema(name="BdFinversionApi对象", description="财务系统API配置")
public class BdFinversionApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId("FID")
    private String fid;

    @Schema(name = "API名称")
    @TableField("HANDTEXT")
    private String handtext;

    @Schema(name = "父级主键")
    @TableField("PID")
    private String pid;

    @Schema(name = "API地址")
    @TableField("API_URL")
    private String apiUrl;

    @Schema(name = "API版本")
    @TableField("API_VERSION")
    private String apiVersion;

    @Schema(name = "认证方式: OAuth2, ApiKey, Basic, None")
    @TableField("AUTH_TYPE")
    private String authType;

    @Schema(name = "接口类型: REST, SOAP, GraphQL")
    @TableField("API_TYPE")
    private String apiType;

    @Schema(name = "请求方式: GET, POST, PUT, DELETE")
    @TableField("REQUEST_METHOD")
    private String requestMethod;

    @Schema(name = "Content-Type")
    @TableField("CONTENT_TYPE")
    private String contentType;

    @Schema(name = "请求头配置(JSON格式)")
    @TableField("REQUEST_HEADERS")
    private String requestHeaders;

    @Schema(name = "请求参数配置(JSON格式)")
    @TableField("REQUEST_PARAMS")
    private String requestParams;

    @Schema(name = "响应数据映射配置(JSON格式)")
    @TableField("RESPONSE_MAPPING")
    private String responseMapping;

    @Schema(name = "接口文档地址")
    @TableField("DOC_URL")
    private String docUrl;

    @Schema(name = "供应商联系方式")
    @TableField("VENDOR_CONTACT")
    private String vendorContact;

    @Schema(name = "API密钥")
    @TableField("API_KEY")
    private String apiKey;

    @Schema(name = "API密钥")
    @TableField("API_SECRET")
    private String apiSecret;

    @Schema(name = "超时时间(秒)")
    @TableField("TIMEOUT_SECONDS")
    private BigDecimal timeoutSeconds;

    @Schema(name = "重试次数")
    @TableField("RETRY_COUNT")
    private BigDecimal retryCount;

    @Schema(name = "启用状态: 0-禁用 1-启用")
    @TableField("ENABLED")
    private BigDecimal enabled;

    @Schema(name = "备注说明")
    @TableField("REMARK")
    private String remark;

    @Schema(name = "排序")
    @TableField("SORT")
    private BigDecimal sort;

    @Schema(name = "创建人")
    @TableField("CREATOR")
    private BigDecimal creator;

    @Schema(name = "修改人")
    @TableField("MODIFIER")
    private BigDecimal modifier;

    @Schema(name = "创建时间")
    @TableField("CREATIONTIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

    @Schema(name = "修改时间")
    @TableField(value = "MODIFIEDTIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

    @Schema(name = "分布式 0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
    @TableField("DATAORIGINFLAG")
    private BigDecimal dataoriginflag;
      
    @IgnoreSwaggerParameter
    @Schema(name = "子集")
    @TableField(exist = false)
    private List<BdFinversionApi> childrenList = null;

}
