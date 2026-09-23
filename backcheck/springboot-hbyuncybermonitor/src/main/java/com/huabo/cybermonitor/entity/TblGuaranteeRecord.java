package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 担保记录实体 - 金融风险穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_GUARANTEE_RECORD")
public class TblGuaranteeRecord {

    @TableId(value = "GUARANTEE_ID", type = IdType.ASSIGN_UUID)
    private String guaranteeId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("GUARANTEED_ID")
    private String guaranteedId;

    @TableField("GUARANTEED_NAME")
    private String guaranteedName;

    @TableField("GUARANTEE_NAME")
    private String guaranteeName;

    @TableField("GUARANTEE_TYPE")
    private String guaranteeType;

    @TableField("GUARANTEE_AMOUNT")
    private BigDecimal guaranteeAmount;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("COUNTER_GUARANTEE")
    private String counterGuarantee;

    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @TableField("GUARANTEE_STATUS")
    private String guaranteeStatus;

    @TableField("GUARANTEE_RATIO")
    private String guaranteeRatio;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("IS_RELATED_PARTY")
    private Integer isRelatedParty;

    @TableField("REMARK")
    private String remark;

    @TableField("COMPENSATE_AMOUNT")
    private BigDecimal compensateAmount;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

