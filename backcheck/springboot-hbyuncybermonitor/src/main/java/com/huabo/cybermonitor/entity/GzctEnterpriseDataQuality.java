package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_DATA_QUALITY")
public class GzctEnterpriseDataQuality extends Model<GzctEnterpriseDataQuality> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("CHECK_TYPE")
    private String checkType;

    @TableField("CHECK_ITEM")
    private String checkItem;

    @TableField("CHECK_RESULT")
    private String checkResult;

    @TableField("ERROR_DESC")
    private String errorDesc;

    @TableField("DATA_SOURCE")
    private String dataSource;

    @TableField("CHECK_TIME")
    private LocalDateTime checkTime;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
