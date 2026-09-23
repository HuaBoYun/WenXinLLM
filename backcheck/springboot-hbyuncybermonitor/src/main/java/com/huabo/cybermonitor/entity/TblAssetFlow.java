package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产流动实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ASSET_FLOW")
public class TblAssetFlow {

    @TableId(value = "FLOW_ID", type = IdType.ASSIGN_UUID)
    private String flowId;

    @TableField("SOURCE_COMPANY_ID")
    private String sourceCompanyId;

    @TableField("SOURCE_COMPANY_NAME")
    private String sourceCompanyName;

    @TableField("TARGET_COMPANY_ID")
    private String targetCompanyId;

    @TableField("TARGET_COMPANY_NAME")
    private String targetCompanyName;

    @TableField("ASSET_TYPE")
    private String assetType;

    @TableField("ASSET_NAME")
    private String assetName;

    @TableField("FLOW_AMOUNT")
    private BigDecimal flowAmount;

    @TableField("FLOW_TYPE")
    private String flowType;

    @TableField("FLOW_REASON")
    private String flowReason;

    @TableField("FLOW_DATE")
    private LocalDate flowDate;

    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @TableField("IS_RELATED_PARTY")
    private String isRelatedParty;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
