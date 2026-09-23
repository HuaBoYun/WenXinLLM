package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源配置表
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_DATA_SOURCE")
@Schema(name="TblDataSource对象", description="数据源配置表")
public class TblDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "数据源ID")
    @TableId(value = "SOURCE_ID", type = IdType.ASSIGN_ID)
    private String sourceId;

    @Schema(name = "数据源名称")
    @TableField("SOURCE_NAME")
    private String sourceName;

    @Schema(name = "数据源类型(DM/ORACLE/MYSQL)")
    @TableField("SOURCE_TYPE")
    private String sourceType;

    @Schema(name = "主机IP")
    @TableField("HOST_IP")
    private String hostIp;

    @Schema(name = "端口")
    @TableField("PORT")
    private Integer port;

    @Schema(name = "数据库名")
    @TableField("DATABASE_NAME")
    private String databaseName;

    @Schema(name = "用户名")
    @TableField("USERNAME")
    private String username;

    @Schema(name = "密码(加密)")
    @TableField("PASSWORD")
    private String password;

    @Schema(name = "连接URL")
    @TableField("CONNECTION_URL")
    private String connectionUrl;

    @Schema(name = "状态(ACTIVE/INACTIVE)")
    @TableField("STATUS")
    private String status;

    @Schema(name = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @Schema(name = "最后同步时间")
    @TableField("LAST_SYNC_TIME")
    private LocalDateTime lastSyncTime;

    @Schema(name = "表数量")
    @TableField("TABLE_COUNT")
    private Integer tableCount;

    @Schema(name = "连接测试结果(SUCCESS/FAILED)")
    @TableField("CONNECTION_TEST_RESULT")
    private String connectionTestResult;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
