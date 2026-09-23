package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考核指标实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("assessment_indicator")
@Schema(name="AssessmentIndicator对象", description="考核指标表")
public class AssessmentIndicator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Long id;

    @Schema(name = "指标编码")
    private String indicatorCode;

    @Schema(name = "指标名称")
    private String indicatorName;

    @Schema(name = "父指标ID")
    private Long parentId;

    @Schema(name = "指标类型：1-定量，2-定性")
    private Integer indicatorType;

    @Schema(name = "权重")
    private BigDecimal weight;

    @Schema(name = "计算方法")
    private String calculationMethod;

    @Schema(name = "数据来源")
    private String dataSource;

    @Schema(name = "单位")
    private String unit;

    @Schema(name = "目标值")
    private BigDecimal targetValue;

    @Schema(name = "是否启用：1-启用，0-禁用")
    private Integer isActive;

    @Schema(name = "排序")
    private Integer sortOrder;

    // @Schema(name = "备注")
    // private String remarks;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(name = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(name = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy = 1L;

    @Schema(name = "更新人ID")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy = 1L;

    // 非数据库字段
    @Schema(name = "子指标列表")
    @TableField(exist = false)
    private List<AssessmentIndicator> children;

    @Schema(name = "父指标名称")
    @TableField(exist = false)
    private String parentName;
}
