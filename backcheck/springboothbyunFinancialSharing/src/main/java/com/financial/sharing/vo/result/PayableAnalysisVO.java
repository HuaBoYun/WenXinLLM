package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 应付分析VO
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付分析VO")
public class PayableAnalysisVO {

    @ApiModelProperty("总应付金额")
    private BigDecimal totalPayable;

    @ApiModelProperty("已付金额")
    private BigDecimal totalPaid;

    @ApiModelProperty("未付金额")
    private BigDecimal totalUnpaid;

    @ApiModelProperty("逾期金额")
    private BigDecimal overdueAmount;

    @ApiModelProperty("逾期笔数")
    private Integer overdueCount;

    @ApiModelProperty("供应商数量")
    private Integer supplierCount;

    @ApiModelProperty("单据数量")
    private Integer documentCount;

    @ApiModelProperty("平均账期天数")
    private BigDecimal avgPaymentDays;

    @ApiModelProperty("账龄分析")
    private List<AgingAnalysisItem> agingAnalysis;

    @ApiModelProperty("供应商分布")
    private List<SupplierDistributionItem> supplierDistribution;

    @ApiModelProperty("趋势数据")
    private List<TrendItem> trendData;

    /**
     * 账龄分析项
     */
    @Data
    @ApiModel("账龄分析项")
    public static class AgingAnalysisItem {
        @ApiModelProperty("账龄区间")
        private String agingRange;
        @ApiModelProperty("金额")
        private BigDecimal amount;
        @ApiModelProperty("笔数")
        private Integer count;
        @ApiModelProperty("占比")
        private BigDecimal percentage;
    }

    /**
     * 供应商分布项
     */
    @Data
    @ApiModel("供应商分布项")
    public static class SupplierDistributionItem {
        @ApiModelProperty("供应商ID")
        private String supplierId;
        @ApiModelProperty("供应商名称")
        private String supplierName;
        @ApiModelProperty("应付金额")
        private BigDecimal payableAmount;
        @ApiModelProperty("占比")
        private BigDecimal percentage;
    }

    /**
     * 趋势项
     */
    @Data
    @ApiModel("趋势项")
    public static class TrendItem {
        @ApiModelProperty("时间(月份)")
        private String period;
        @ApiModelProperty("应付金额")
        private BigDecimal payableAmount;
        @ApiModelProperty("付款金额")
        private BigDecimal paymentAmount;
    }
}

