package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 导出日志实体
 */
@Data
@TableName("TBL_EXPORT_LOG")
public class ExportLogEntity {

    @TableId(type = IdType.AUTO)
    @TableField("LOG_ID")
    private Long logId;

    @TableField("USER_ID")
    private Long userId;

    @TableField("USERNAME")
    private String username;

    @TableField("EXPORT_TYPE")
    private String exportType;

    @TableField("EXPORT_MODULE")
    private String exportModule;

    @TableField("RECORD_COUNT")
    private Integer recordCount;

    @TableField("FILE_NAME")
    private String fileName;

    @TableField("FILE_SIZE")
    private Long fileSize;

    @TableField("FILE_FORMAT")
    private String fileFormat;

    @TableField("EXPORT_TIME")
    private LocalDateTime exportTime;

    @TableField("EXPORT_DURATION")
    private Long exportDuration; // 导出耗时（毫秒）

    @TableField("STATUS")
    private String status; // SUCCESS, FAILED, TIMEOUT

    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @TableField("IP")
    private String ip;

    @TableField("USER_AGENT")
    private String userAgent;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
}