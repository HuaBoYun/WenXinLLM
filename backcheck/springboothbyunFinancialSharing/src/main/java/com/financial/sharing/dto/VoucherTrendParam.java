package com.financial.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 凭证趋势分析参数
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@ApiModel("凭证趋势分析参数")
public class VoucherTrendParam {

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "趋势维度：DAY-按天，WEEK-按周，MONTH-按月，QUARTER-按季度，YEAR-按年")
    private String trendDimension;

    @ApiModelProperty(value = "指标类型：COUNT-凭证数量，AMOUNT-凭证金额，BALANCE-余额")
    private String metricType;

    @ApiModelProperty(value = "凭证类型ID（可选）")
    private Long voucherTypeId;

    @ApiModelProperty(value = "凭证状态（可选）")
    private Integer voucherStatus;

    @ApiModelProperty(value = "会计期间（可选）")
    private String accountingPeriod;

    @ApiModelProperty(value = "币种代码（可选）")
    private String currencyCode;
}