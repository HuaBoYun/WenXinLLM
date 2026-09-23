package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源配置参数
 */
@Data
@ApiModel("数据源配置参数")
public class DataSourceConfigParam {

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("配置代码")
    private String configCode;

    @ApiModelProperty("数据源类型")
    private String dataSourceType;

    @ApiModelProperty("连接URL")
    private String connectionUrl;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("驱动类")
    private String driverClass;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("SQL查询")
    private String sqlQuery;

    @ApiModelProperty("API地址")
    private String apiUrl;

    @ApiModelProperty("API方法")
    private String apiMethod;

    @ApiModelProperty("API请求头")
    private String apiHeaders;

    @ApiModelProperty("API请求参数")
    private String apiParams;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("文件编码")
    private String fileEncoding;

    @ApiModelProperty("分隔符")
    private String delimiter;

    @ApiModelProperty("引号字符")
    private String quoteChar;

    @ApiModelProperty("定宽配置")
    private String fixedWidthConfig;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("是否启用")
    private Boolean isEnabled;

    @ApiModelProperty("租户ID")
    private Long tenantId;
}