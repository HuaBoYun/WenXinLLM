package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考核明细实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("assessment_detail")
@Schema(name="AssessmentDetail对象", description="考核明细表")
public class AssessmentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "考核记录ID")
    private Long assessmentId;

    @Schema(name = "指标ID")
    private Long indicatorId;

    @Schema(name = "实际值")
    private BigDecimal actualValue;

    @Schema(name = "目标值")
    private BigDecimal targetValue;

    @Schema(name = "得分")
    private BigDecimal score;

    @Schema(name = "权重")
    private BigDecimal weight;

    @Schema(name = "加权得分")
    private BigDecimal weightedScore;

    @Schema(name = "数据来源")
    private String dataSource;

    @Schema(name = "计算公式")
    private String calculationFormula;

    @Schema(name = "备注")
    private String remarks;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(name = "创建人ID")
    private Long createBy = 1L;

    // 非数据库字段
    @Schema(name = "指标编码")
    @TableField(exist = false)
    private String indicatorCode;

    @Schema(name = "指标名称")
    @TableField(exist = false)
    private String indicatorName;

    @Schema(name = "指标类型")
    @TableField(exist = false)
    private Integer indicatorType;

    @Schema(name = "单位")
    @TableField(exist = false)
    private String unit;
}
