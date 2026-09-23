package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 股东穿透分析实体类
 * @author system
 * @date 2025-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SHAREHOLDER_ANALYSIS")
@Schema(name = "股东穿透分析", description = "股东穿透分析实体")
public class ShareholderAnalysis {

    @TableId(value = "ANALYSIS_ID", type = IdType.ASSIGN_UUID)
    @Schema(description = "分析ID")
    private String analysisId;

    @TableField("ENTERPRISE_ID")
    @Schema(description = "企业ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    @Schema(description = "企业名称")
    private String enterpriseName;

    @TableField("SHAREHOLDER_NAME")
    @Schema(description = "股东名称")
    private String shareholderName;

    @TableField("SHAREHOLDER_ID")
    @Schema(description = "股东ID")
    private String shareholderId;

    @TableField("SHAREHOLDER_TYPE")
    @Schema(description = "股东类型")
    private String shareholderType;

    @TableField("SHAREHOLDING_RATIO")
    @Schema(description = "持股比例")
    private BigDecimal shareholdingRatio;

    @TableField("SHAREHOLDING_AMOUNT")
    @Schema(description = "持股金额")
    private BigDecimal shareholdingAmount;

    @TableField("PENETRATION_LEVEL")
    @Schema(description = "穿透层级")
    private Integer penetrationLevel;

    @TableField("ULTIMATE_CONTROLLER")
    @Schema(description = "最终控制人")
    private String ultimateController;

    @TableField("RISK_LEVEL")
    @Schema(description = "风险等级")
    private String riskLevel;

    @TableField("IS_RELATED_PARTY")
    @Schema(description = "是否关联方")
    private String isRelatedParty;

    @TableField("IS_CONCERTED_ACTION")
    @Schema(description = "是否一致行动人")
    private String isConcertedAction;

    @TableField("ANALYSIS_STATUS")
    @Schema(description = "分析状态")
    private String analysisStatus;

    @TableField("ANALYSIS_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "分析时间")
    private Date analysisTime;

    @TableField("REMARK")
    @Schema(description = "备注")
    private String remark;

    @TableField("CREATE_BY")
    @Schema(description = "创建人")
    private String createBy;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;
}
