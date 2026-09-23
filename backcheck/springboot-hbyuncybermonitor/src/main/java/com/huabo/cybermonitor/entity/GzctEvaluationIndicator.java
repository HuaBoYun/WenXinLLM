package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考核评价指标实体
 *
 * @author system
 * @date 2026-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_EVALUATION_INDICATOR")
public class GzctEvaluationIndicator extends Model<GzctEvaluationIndicator> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 指标名称
     */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /**
     * 指标类型 (政治素质/经营业绩/管理能力/廉洁自律)
     */
    @TableField("INDICATOR_TYPE")
    private String indicatorType;

    /**
     * 权重
     */
    @TableField("WEIGHT")
    private BigDecimal weight;

    /**
     * 评分标准
     */
    @TableField("SCORE_STANDARD")
    private String scoreStandard;

    /**
     * 最高分
     */
    @TableField("MAX_SCORE")
    private BigDecimal maxScore;

    /**
     * 最低分
     */
    @TableField("MIN_SCORE")
    private BigDecimal minScore;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 状态 (启用/停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 排序
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
