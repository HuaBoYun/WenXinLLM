package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资金流向穿透实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FUND_FLOW_PENETRATION")
public class TblFundFlowPenetration {

    @TableId(value = "FLOW_ID", type = IdType.ASSIGN_UUID)
    private String flowId;

    @TableField("FLOW_NO")
    private String flowNo;

    @TableField("FROM_COMPANY")
    private String fromCompany;

    @TableField("TO_COMPANY")
    private String toCompany;

    @TableField("AMOUNT")
    private BigDecimal amount;

    @TableField("PURPOSE")
    private String purpose;

    @TableField("FLOW_TYPE")
    private String flowType;

    @TableField("FUND_NATURE")
    private String fundNature;

    @TableField("OCCUR_TIME")
    private String occurTime;

    @TableField("CONTRACT_NO")
    private String contractNo;

    @TableField("RISK_DESC")
    private String riskDesc;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}
