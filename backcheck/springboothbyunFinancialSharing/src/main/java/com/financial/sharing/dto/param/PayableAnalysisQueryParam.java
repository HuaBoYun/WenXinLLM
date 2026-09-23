package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 应付分析查询参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付分析查询参数")
public class PayableAnalysisQueryParam {

    @ApiModelProperty("开始日期")
    private LocalDate startDate;

    @ApiModelProperty("结束日期")
    private LocalDate endDate;

    @ApiModelProperty("分析维度(1=供应商 2=时间 3=业务类型 4=账龄)")
    private Integer dimension;

    @ApiModelProperty("供应商ID列表")
    private List<String> supplierIds;

    @ApiModelProperty("供应商分类")
    private Integer supplierCategory;

    @ApiModelProperty("业务类型")
    private Integer businessType;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("账龄区间(如:0-30,31-60,61-90,90+)")
    private String agingRange;

    @ApiModelProperty("分组维度(supplier/category/month/businessType)")
    private String groupBy;

    @ApiModelProperty("排序字段")
    private String orderBy;

    @ApiModelProperty("排序方式(asc/desc)")
    private String orderDirection;

    @ApiModelProperty("Top N")
    private Integer topN;

    @ApiModelProperty("预测月数(用于现金流预测)")
    private Integer months;

    @ApiModelProperty("预测模型(1=历史平均 2=趋势分析 3=季节性调整)")
    private Integer forecastModel;
}

