package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源测试参数
 */
@Data
@ApiModel("数据源测试参数")
public class DataSourceTestParam {

    @ApiModelProperty("配置ID")
    private Long configId;

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

    @ApiModelProperty("测试SQL（数据库类型使用）")
    private String testSql;

    @ApiModelProperty("预览行数（默认10行）")
    private Integer previewRows = 10;
}