package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 企业数据备份记录实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_DATA_BACKUP")
public class GzctEnterpriseDataBackup extends Model<GzctEnterpriseDataBackup> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("BACKUP_NAME")
    private String backupName;

    @TableField("BACKUP_TYPE")
    private String backupType;

    @TableField("DATA_SOURCE")
    private String dataSource;

    @TableField("STATUS")
    private String status;

    @TableField("BACKUP_SIZE")
    private BigDecimal backupSize;

    @TableField("RECORD_COUNT")
    private Integer recordCount;

    @TableField("BACKUP_TIME")
    private LocalDateTime backupTime;

    @TableField("EXPIRE_TIME")
    private LocalDateTime expireTime;

    @TableField("OPERATOR")
    private String operator;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
