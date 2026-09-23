package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 穿透分析日志表 - 业绩考核穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_STATEASSETS_PERFORMANCE_DRILL_LOG")
public class TblStateassetsPerformanceDrillLog {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("DRILL_TYPE")
    private String drillType;

    @TableField("START_NODE_TYPE")
    private String startNodeType;

    @TableField("START_NODE_ID")
    private String startNodeId;

    @TableField("START_NODE_NAME")
    private String startNodeName;

    @TableField("END_NODE_TYPE")
    private String endNodeType;

    @TableField("END_NODE_ID")
    private String endNodeId;

    @TableField("END_NODE_NAME")
    private String endNodeName;

    @TableField("DRILL_PATH")
    private String drillPath;

    @TableField("DRILL_RESULT")
    private String drillResult;

    @TableField("USER_ID")
    private String userId;

    @TableField("USER_NAME")
    private String userName;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
}
