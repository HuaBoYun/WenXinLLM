package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 应收分析查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel("应收分析查询参数")
public class ArAnalysisQueryParam {

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty("结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty("客户ID")
    private String customerId;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("分析类型：receivable-应收分析, receipt-收款分析, aging-账龄分析, overdue-逾期分析")
    private String analysisType;

    @ApiModelProperty("统计维度：day-按天, week-按周, month-按月, quarter-按季度, year-按年")
    private String dimension;

    @ApiModelProperty("排名数量")
    private Integer topN;

    @ApiModelProperty("是否包含已核销")
    private Boolean includeWrittenOff;

    @ApiModelProperty("是否包含坏账")
    private Boolean includeBadDebt;

    @ApiModelProperty("导出格式：excel, pdf")
    private String exportFormat;
}

