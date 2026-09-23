package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 财务指标定义实体类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@ToString
@TableName("TBL_FINANCIAL_INDICATOR_DEF")
@Schema(name="FinancialIndicatorDef", description="财务指标定义")
public class FinancialIndicatorDef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="指标ID")
    @TableId(value = "INDICATOR_ID", type = IdType.ASSIGN_ID)
    private String indicatorId;

    @Schema(name="指标编码")
    @TableField("INDICATOR_CODE")
    private String indicatorCode;

    @Schema(name="指标名称")
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    @Schema(name="指标类型(PROFITABILITY/ASSET_QUALITY/SOLVENCY/GROWTH/SUPPLEMENT)")
    @TableField("INDICATOR_TYPE")
    private String indicatorType;

    @Schema(name="计算公式")
    @TableField("CALCULATION_FORMULA")
    private String calculationFormula;

    @Schema(name="单位")
    @TableField("UNIT")
    private String unit;

    @Schema(name="指标说明")
    @TableField("DESCRIPTION")
    private String description;

    @Schema(name="排序")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @Schema(name="状态")
    @TableField("STATUS")
    private String status;

    @Schema(name="创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name="更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    // 扩展字段
    @Schema(name="指标类型名称")
    @TableField(exist = false)
    private String indicatorTypeName;
}
