package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 交叉持股实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CROSS_HOLDING")
public class TblCrossHolding {

    @TableId(value = "HOLDING_ID", type = IdType.ASSIGN_UUID)
    private String holdingId;

    @TableField("COMPANY_A_ID")
    private String companyAId;

    @TableField("COMPANY_A_NAME")
    private String companyAName;

    @TableField("COMPANY_B_ID")
    private String companyBId;

    @TableField("COMPANY_B_NAME")
    private String companyBName;

    @JsonProperty("aHoldBRatio")
    @TableField("A_HOLD_B_RATIO")
    private BigDecimal aHoldBRatio;

    @JsonProperty("bHoldARatio")
    @TableField("B_HOLD_A_RATIO")
    private BigDecimal bHoldARatio;

    @TableField("CROSS_TYPE")
    private String crossType;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("CAPITAL_INFLATION")
    private BigDecimal capitalInflation;

    @TableField("IS_CIRCULAR")
    private String isCircular;

    @TableField("CHAIN_LENGTH")
    private Integer chainLength;

    @TableField("DISCOVERY_DATE")
    private LocalDate discoveryDate;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
