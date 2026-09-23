package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 风险预警实体类
 * 基于数据库表 TBL_RISK_WARNING 的实际结构
 * 
 * @author 华博云开发团队
 * @since 2025-10-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_RISK_WARNING")
@Schema(name="TblRiskWarning对象", description="风险预警表")
public class TblRiskWarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "预警ID")
    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_ID)
    private String warningId;

    @Schema(name = "预警编码")
    @TableField("WARNING_CODE")
    private String warningCode;

    @Schema(name = "模型ID")
    @TableField("MODEL_ID")
    private String modelId;

    @Schema(name = "规则ID")
    @TableField("RULE_ID")
    private String ruleId;

    @Schema(name = "评估模型ID")
    @TableField("EVAL_MODEL_ID")
    private String evalModelId;

    @Schema(name = "企业ID")
    @TableField("COMPANY_ID")
    private String companyId;

    @Schema(name = "企业名称")
    @TableField("COMPANY_NAME")
    private String companyName;

    @Schema(name = "预警类型")
    @TableField("WARNING_TYPE")
    private String warningType;

    @Schema(name = "预警级别(HIGH/MEDIUM/LOW)")
    @TableField("WARNING_LEVEL")
    private String warningLevel;

    @Schema(name = "预警状态(PENDING/PROCESSING/PROCESSED/IGNORED)")
    @TableField("WARNING_STATUS")
    private String warningStatus;

    @Schema(name = "预警值")
    @TableField("WARNING_VALUE")
    private BigDecimal warningValue;

    @Schema(name = "阈值")
    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;

    @Schema(name = "预警描述")
    @TableField("WARNING_DESCRIPTION")
    private String warningDescription;

    @Schema(name = "预警详情")
    @TableField("WARNING_DETAIL")
    private String warningDetail;

    @Schema(name = "相关数据")
    @TableField("RELATED_DATA")
    private String relatedData;

    @Schema(name = "预警时间")
    @TableField("WARNING_TIME")
    private LocalDateTime warningTime;

    @Schema(name = "处理人")
    @TableField("PROCESS_USER")
    private String processUser;

    @Schema(name = "处理时间")
    @TableField("PROCESS_TIME")
    private LocalDateTime processTime;

    @Schema(name = "处理动作")
    @TableField("PROCESS_ACTION")
    private String processAction;

    @Schema(name = "处理说明")
    @TableField("PROCESS_NOTE")
    private String processNote;

    @Schema(name = "是否误报(Y/N)")
    @TableField("IS_FALSE_POSITIVE")
    private String isFalsePositive;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    // 预警级别常量
    public static final String WARNING_LEVEL_HIGH = "HIGH";
    public static final String WARNING_LEVEL_MEDIUM = "MEDIUM";
    public static final String WARNING_LEVEL_LOW = "LOW";

    // 预警状态常量
    public static final String WARNING_STATUS_PENDING = "PENDING";
    public static final String WARNING_STATUS_PROCESSING = "PROCESSING";
    public static final String WARNING_STATUS_PROCESSED = "PROCESSED";
    public static final String WARNING_STATUS_IGNORED = "IGNORED";

    // 处理动作常量
    public static final String PROCESS_ACTION_CONFIRM = "CONFIRM";
    public static final String PROCESS_ACTION_IGNORE = "IGNORE";
    public static final String PROCESS_ACTION_ESCALATE = "ESCALATE";

    // 是否误报常量
    public static final String FALSE_POSITIVE_YES = "Y";
    public static final String FALSE_POSITIVE_NO = "N";
}
