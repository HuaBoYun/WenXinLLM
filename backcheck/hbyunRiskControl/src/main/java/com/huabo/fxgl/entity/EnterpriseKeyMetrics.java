package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业关键指标汇总实体类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@ToString
@TableName("TBL_ENTERPRISE_KEY_METRICS")
@Schema(name="EnterpriseKeyMetrics", description="企业关键指标汇总")
public class EnterpriseKeyMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="指标ID")
    @TableId(value = "METRIC_ID", type = IdType.ASSIGN_ID)
    private String metricId;

    @Schema(name="企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name="指标编码")
    @TableField("METRIC_CODE")
    private String metricCode;

    @Schema(name="指标名称")
    @TableField("METRIC_NAME")
    private String metricName;

    @Schema(name="指标数值")
    @TableField("METRIC_VALUE")
    private BigDecimal metricValue;

    @Schema(name="指标文本值(如15.6亿)")
    @TableField("METRIC_VALUE_TEXT")
    private String metricValueText;

    @Schema(name="指标单位")
    @TableField("METRIC_UNIT")
    private String metricUnit;

    @Schema(name="指标类型(FINANCIAL-财务,OPERATIONAL-运营,HR-人力)")
    @TableField("METRIC_TYPE")
    private String metricType;

    @Schema(name="计算日期")
    @TableField("CALCULATION_DATE")
    private LocalDate calculationDate;

    @Schema(name="数据来源")
    @TableField("DATA_SOURCE")
    private String dataSource;

    @Schema(name="是否启用(Y/N)")
    @TableField("IS_ACTIVE")
    private String isActive;

    @Schema(name="创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(name="创建人")
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    private String createUser;

    @Schema(name="更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(name="更新人")
    @TableField(value = "UPDATE_USER", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;
}
