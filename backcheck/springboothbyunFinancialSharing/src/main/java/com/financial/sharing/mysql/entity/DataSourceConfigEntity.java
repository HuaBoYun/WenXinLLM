package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据源配置实体
 */
@Data
@TableName("TBL_DATA_SOURCE_CONFIG")
public class DataSourceConfigEntity {

    @TableId(type = IdType.AUTO)
    @TableField("CONFIG_ID")
    private Long configId;

    @TableField("CONFIG_NAME")
    private String configName;

    @TableField("CONFIG_CODE")
    private String configCode;

    @TableField("DATA_SOURCE_TYPE")
    private String dataSourceType;

    @TableField("CONNECTION_URL")
    private String connectionUrl;

    @TableField("USERNAME")
    private String username;

    @TableField("PASSWORD")
    private String password;

    @TableField("DRIVER_CLASS")
    private String driverClass;

    @TableField("TABLE_NAME")
    private String tableName;

    @TableField("SQL_QUERY")
    private String sqlQuery;

    @TableField("API_URL")
    private String apiUrl;

    @TableField("API_METHOD")
    private String apiMethod;

    @TableField("API_HEADERS")
    private String apiHeaders;

    @TableField("API_PARAMS")
    private String apiParams;

    @TableField("FILE_PATH")
    private String filePath;

    @TableField("FILE_ENCODING")
    private String fileEncoding;

    @TableField("DELIMITER")
    private String delimiter;

    @TableField("QUOTE_CHAR")
    private String quoteChar;

    @TableField("FIXED_WIDTH_CONFIG")
    private String fixedWidthConfig;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    @TableField("IS_TESTED")
    private Boolean isTested;

    @TableField("TEST_TIME")
    private LocalDateTime testTime;

    @TableField("TEST_RESULT")
    private String testResult;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("CREATE_USER")
    private Long createUser;

    @TableField("UPDATE_USER")
    private Long updateUser;
}