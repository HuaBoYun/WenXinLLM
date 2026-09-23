package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 账龄分析结果VO
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArAgingAnalysisVO", description = "账龄分析结果")
public class ArAgingAnalysisVO {

    @ApiModelProperty(value = "分析日期")
    private LocalDate analysisDate;

    @ApiModelProperty(value = "应收总额")
    private BigDecimal totalReceivableAmount;

    @ApiModelProperty(value = "逾期总额")
    private BigDecimal totalOverdueAmount;

    @ApiModelProperty(value = "账龄区间汇总")
    private List<AgingRangeSummary> agingRangeSummaries;

    @ApiModelProperty(value = "账龄明细列表")
    private List<AgingDetail> agingDetails;

    /**
     * 账龄区间汇总
     */
    @Data
    @ApiModel(value = "AgingRangeSummary", description = "账龄区间汇总")
    public static class AgingRangeSummary {
        @ApiModelProperty(value = "账龄区间")
        private String agingRange;
        @ApiModelProperty(value = "金额")
        private BigDecimal amount;
        @ApiModelProperty(value = "占比")
        private BigDecimal percentage;
        @ApiModelProperty(value = "单据数量")
        private Integer count;
    }

    /**
     * 账龄明细
     */
    @Data
    @ApiModel(value = "AgingDetail", description = "账龄明细")
    public static class AgingDetail {
        @ApiModelProperty(value = "客户ID")
        private String customerId;
        @ApiModelProperty(value = "客户名称")
        private String customerName;
        @ApiModelProperty(value = "应收单号")
        private String documentNo;
        @ApiModelProperty(value = "应收金额")
        private BigDecimal receivableAmount;
        @ApiModelProperty(value = "剩余金额")
        private BigDecimal remainingAmount;
        @ApiModelProperty(value = "到期日期")
        private LocalDate dueDate;
        @ApiModelProperty(value = "账龄天数")
        private Integer agingDays;
        @ApiModelProperty(value = "账龄区间")
        private String agingRange;
        @ApiModelProperty(value = "风险等级")
        private Integer riskLevel;
        @ApiModelProperty(value = "风险等级名称")
        private String riskLevelName;
    }
}

