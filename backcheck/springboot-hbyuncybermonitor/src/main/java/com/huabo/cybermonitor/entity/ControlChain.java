package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 控制链分析实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONTROL_CHAIN")
public class ControlChain {

    @TableId(value = "CHAIN_ID", type = IdType.ASSIGN_UUID)
    private String chainId;

    private String enterpriseId;

    private String enterpriseName;

    private String controllerName;

    private String controllerId;

    private String controlType;

    private BigDecimal controlStrength;

    private Integer chainLength;

    private Integer nodeCount;

    private String stabilityLevel;

    private Integer hasLoop;

    private String riskLevel;

    private String chainPath;

    private LocalDateTime lastAnalysisTime;

    private String status;

    private String remark;

    private String createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
