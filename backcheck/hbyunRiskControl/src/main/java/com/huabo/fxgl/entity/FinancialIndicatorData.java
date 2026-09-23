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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 财务指标数据实体类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@ToString
@TableName("TBL_FINANCIAL_INDICATOR_DATA")
@Schema(name="FinancialIndicatorData", description="财务指标数据")
public class FinancialIndicatorData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="数据ID")
    @TableId(value = "DATA_ID", type = IdType.ASSIGN_ID)
    private String dataId;

    @Schema(name="企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name="指标ID")
    @TableField("INDICATOR_ID")
    private String indicatorId;

    @Schema(name="指标值")
    @TableField("INDICATOR_VALUE")
    private BigDecimal indicatorValue;

    @Schema(name="计算期间(MONTHLY/QUARTERLY/YEARLY)")
    @TableField("CALCULATION_PERIOD")
    private String calculationPeriod;

    @Schema(name="期间开始日期")
    @TableField("PERIOD_START_DATE")
    private Date periodStartDate;

    @Schema(name="期间结束日期")
    @TableField("PERIOD_END_DATE")
    private Date periodEndDate;

    @Schema(name="数据来源")
    @TableField("DATA_SOURCE")
    private String dataSource;

    @Schema(name="计算时间")
    @TableField("CALCULATION_TIME")
    private Date calculationTime;

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
    @Schema(name="指标编码")
    @TableField(exist = false)
    private String indicatorCode;

    @Schema(name="指标名称")
    @TableField(exist = false)
    private String indicatorName;

    @Schema(name="指标类型")
    @TableField(exist = false)
    private String indicatorType;

    @Schema(name="单位")
    @TableField(exist = false)
    private String unit;

    @Schema(name="企业名称")
    @TableField(exist = false)
    private String enterpriseName;

    @Schema(name="同比增长率")
    @TableField(exist = false)
    private BigDecimal yearOverYearGrowth;

    @Schema(name="环比增长率")
    @TableField(exist = false)
    private BigDecimal monthOverMonthGrowth;
}
