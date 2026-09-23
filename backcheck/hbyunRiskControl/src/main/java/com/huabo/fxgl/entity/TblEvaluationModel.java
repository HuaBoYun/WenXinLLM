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
 * 评估模型实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_EVALUATION_MODEL")
@Schema(name="TblEvaluationModel对象", description="评估模型表")
public class TblEvaluationModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "评估模型ID")
    @TableId(value = "EVAL_MODEL_ID", type = IdType.ASSIGN_ID)
    private String evalModelId;

    @Schema(name = "模型编码")
    @TableField("MODEL_CODE")
    private String modelCode;

    @Schema(name = "模型名称")
    @TableField("MODEL_NAME")
    private String modelName;

    @Schema(name = "关联的数据模型ID")
    @TableField("DATA_MODEL_ID")
    private String dataModelId;

    @Schema(name = "业务场景(PROCUREMENT/FINANCE/AUDIT)")
    @TableField("BUSINESS_SCENARIO")
    private String businessScenario;

    @Schema(name = "行业类型")
    @TableField("INDUSTRY_TYPE")
    private String industryType;

    @Schema(name = "模型描述")
    @TableField("DESCRIPTION")
    private String description;

    @Schema(name = "评分算法(WEIGHTED_SUM/NEURAL_NETWORK)")
    @TableField("SCORE_ALGORITHM")
    private String scoreAlgorithm;

    @Schema(name = "总权重")
    @TableField("TOTAL_WEIGHT")
    private BigDecimal totalWeight;

    @Schema(name = "低风险阈值")
    @TableField("RISK_THRESHOLD_LOW")
    private BigDecimal riskThresholdLow;

    @Schema(name = "中风险阈值")
    @TableField("RISK_THRESHOLD_MEDIUM")
    private BigDecimal riskThresholdMedium;

    @Schema(name = "高风险阈值")
    @TableField("RISK_THRESHOLD_HIGH")
    private BigDecimal riskThresholdHigh;

    @Schema(name = "版本号")
    @TableField("VERSION")
    private String version;

    @Schema(name = "状态(DRAFT/TESTING/PUBLISHED/ARCHIVED)")
    @TableField("STATUS")
    private String status;

    @Schema(name = "是否启用(Y/N)")
    @TableField("IS_ENABLED")
    private String isEnabled;

    @Schema(name = "准确率(%)")
    @TableField("ACCURACY_RATE")
    private BigDecimal accuracyRate;

    @Schema(name = "预警数量")
    @TableField("WARNING_COUNT")
    private Integer warningCount;

    @Schema(name = "最后测试时间")
    @TableField("LAST_TEST_TIME")
    private LocalDateTime lastTestTime;

    @Schema(name = "发布时间")
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name = "更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(name = "参数配置(JSON格式)")
    @TableField("PARAMETER_CONFIG")
    private String parameterConfig;

    @Schema(name = "备注信息")
    @TableField("REMARK")
    private String remark;

    @Schema(name = "定时任务配置(JSON格式)")
    @TableField("SCHEDULE_CONFIG")
    private String scheduleConfig;

    // 状态常量
    public static final String STATUS_DRAFT = "DRAFT";
    public static final String STATUS_TESTING = "TESTING";
    public static final String STATUS_PUBLISHED = "PUBLISHED";
    public static final String STATUS_ARCHIVED = "ARCHIVED";
    public static final String STATUS_RUNNING = "RUNNING";

    // 业务场景常量
    public static final String SCENARIO_PROCUREMENT = "PROCUREMENT";
    public static final String SCENARIO_FINANCE = "FINANCE";
    public static final String SCENARIO_AUDIT = "AUDIT";

    // 启用状态常量
    public static final String ENABLED_YES = "Y";
    public static final String ENABLED_NO = "N";
}
