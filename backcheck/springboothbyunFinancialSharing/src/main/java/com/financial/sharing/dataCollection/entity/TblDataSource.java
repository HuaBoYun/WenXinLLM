package com.financial.sharing.dataCollection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据源配置实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_FS_DATA_SOURCE")
public class TblDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "SOURCE_ID", type = IdType.ASSIGN_UUID)
    private String sourceId;

    @TableField("SOURCE_CODE")
    private String sourceCode;

    @TableField("SOURCE_NAME")
    private String sourceName;

    @TableField("SOURCE_TYPE")
    private String sourceType;

    @TableField("CONNECTION_TYPE")
    private String connectionType;

    @TableField("HOST")
    private String host;

    @TableField("PORT")
    private Integer port;

    @TableField("DATABASE_NAME")
    private String databaseName;

    @TableField("USERNAME")
    private String username;

    @TableField("PASSWORD")
    private String password;

    @TableField("API_URL")
    private String apiUrl;

    @TableField("API_METHOD")
    private String apiMethod;

    @TableField("API_HEADERS")
    private String apiHeaders;

    @TableField("AUTH_TYPE")
    private String authType;

    @TableField("AUTH_TOKEN")
    private String authToken;

    @TableField("FILE_PATH")
    private String filePath;

    @TableField("FILE_TYPE")
    private String fileType;

    @TableField("CONNECTION_POOL_SIZE")
    private Integer connectionPoolSize;

    @TableField("TIMEOUT")
    private Integer timeout;

    @TableField("IS_ENABLED")
    private String isEnabled;

    @TableField("ORG_ID")
    private String orgId;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("UPDATE_TIME")
    private Date updateTime;
}

