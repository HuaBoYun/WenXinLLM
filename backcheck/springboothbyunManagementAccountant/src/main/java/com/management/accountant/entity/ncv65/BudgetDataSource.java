package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 数据源实体类
 * 
 * @description 数据源管理实体，支持预算数据源的配置和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_DATA_SOURCE")
public class BudgetDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 数据源编码
     */
    @TableField("DATA_SOURCE_CODE")
    private String dataSourceCode;

    /**
     * 数据源名称
     */
    @TableField("DATA_SOURCE_NAME")
    private String dataSourceName;

    /**
     * 数据源类型：DATABASE-数据库，FILE-文件，API-接口，MANUAL-手工录入
     */
    @TableField("DATA_SOURCE_TYPE")
    private String dataSourceType;

    /**
     * 连接字符串
     */
    @TableField("CONNECTION_STRING")
    private String connectionString;

    /**
     * 数据库类型：MYSQL-MySQL，ORACLE-Oracle，SQLSERVER-SQL Server
     */
    @TableField("DATABASE_TYPE")
    private String databaseType;

    /**
     * 主机地址
     */
    @TableField("HOST")
    private String host;

    /**
     * 端口号
     */
    @TableField("PORT")
    private Integer port;

    /**
     * 数据库名称
     */
    @TableField("DATABASE_NAME")
    private String databaseName;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 密码（加密存储）
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 数据源状态：ACTIVE-激活，INACTIVE-停用，ERROR-错误
     */
    @TableField("DATA_SOURCE_STATUS")
    private String dataSourceStatus;

    /**
     * 最后连接时间
     */
    @TableField("LAST_CONNECTION_TIME")
    private LocalDateTime lastConnectionTime;

    /**
     * 连接测试结果
     */
    @TableField("CONNECTION_TEST_RESULT")
    private String connectionTestResult;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // ==================== 常量定义 ====================

    /**
     * 数据源类型常量
     */
    public static final String DATA_SOURCE_TYPE_DATABASE = "DATABASE";
    public static final String DATA_SOURCE_TYPE_FILE = "FILE";
    public static final String DATA_SOURCE_TYPE_API = "API";
    public static final String DATA_SOURCE_TYPE_MANUAL = "MANUAL";

    /**
     * 数据库类型常量
     */
    public static final String DATABASE_TYPE_MYSQL = "MYSQL";
    public static final String DATABASE_TYPE_ORACLE = "ORACLE";
    public static final String DATABASE_TYPE_SQLSERVER = "SQLSERVER";

    /**
     * 数据源状态常量
     */
    public static final String DATA_SOURCE_STATUS_ACTIVE = "ACTIVE";
    public static final String DATA_SOURCE_STATUS_INACTIVE = "INACTIVE";
    public static final String DATA_SOURCE_STATUS_ERROR = "ERROR";
}
