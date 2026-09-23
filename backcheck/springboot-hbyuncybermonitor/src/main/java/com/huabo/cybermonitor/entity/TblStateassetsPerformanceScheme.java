package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 考核方案表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_SCHEME")
public class TblStateassetsPerformanceScheme {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("SCHEME_NAME")
    private String schemeName;

    @TableField("SCHEME_YEAR")
    private Integer schemeYear;

    @TableField("SCHEME_TYPE")
    private String schemeType;

    @TableField("ORG_ID")
    private String orgId;

    @TableField("ORG_SCOPE")
    private String orgScope;

    @TableField("STATUS")
    private String status;

    @TableField("VERSION")
    private Integer version;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
