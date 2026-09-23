package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 资金流向派单记录实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FUND_FLOW_DISPATCH")
public class TblFundFlowDispatch {

    @TableId(value = "DISPATCH_ID", type = IdType.ASSIGN_UUID)
    private String dispatchId;

    @TableField("FLOW_ID")
    private String flowId;

    @TableField("FLOW_NO")
    private String flowNo;

    @TableField("ANOMALY_TYPE")
    private String anomalyType;

    @TableField("OWNER")
    private String owner;

    @TableField("DEADLINE")
    private String deadline;

    @TableField("REQUIREMENT")
    private String requirement;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
