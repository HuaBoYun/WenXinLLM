package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 应收统计结果VO
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArReceivableStatisticsVO", description = "应收统计结果")
public class ArReceivableStatisticsVO {

    @ApiModelProperty(value = "应收总额")
    private BigDecimal totalReceivableAmount;

    @ApiModelProperty(value = "已收总额")
    private BigDecimal totalReceivedAmount;

    @ApiModelProperty(value = "剩余应收总额")
    private BigDecimal totalRemainingAmount;

    @ApiModelProperty(value = "逾期总额")
    private BigDecimal totalOverdueAmount;

    @ApiModelProperty(value = "应收单据数量")
    private Integer receivableCount;

    @ApiModelProperty(value = "逾期单据数量")
    private Integer overdueCount;

    @ApiModelProperty(value = "回款率")
    private BigDecimal collectionRate;

    @ApiModelProperty(value = "逾期率")
    private BigDecimal overdueRate;

    @ApiModelProperty(value = "平均账龄天数")
    private Integer avgAgingDays;

    @ApiModelProperty(value = "按业务类型统计")
    private List<BusinessTypeStatistics> businessTypeStatistics;

    @ApiModelProperty(value = "按客户统计")
    private List<CustomerStatistics> customerStatistics;

    @ApiModelProperty(value = "按账龄区间统计")
    private Map<String, BigDecimal> agingRangeStatistics;

    /**
     * 业务类型统计
     */
    @Data
    @ApiModel(value = "BusinessTypeStatistics", description = "业务类型统计")
    public static class BusinessTypeStatistics {
        @ApiModelProperty(value = "业务类型")
        private Integer businessType;
        @ApiModelProperty(value = "业务类型名称")
        private String businessTypeName;
        @ApiModelProperty(value = "应收金额")
        private BigDecimal receivableAmount;
        @ApiModelProperty(value = "已收金额")
        private BigDecimal receivedAmount;
        @ApiModelProperty(value = "单据数量")
        private Integer count;
    }

    /**
     * 客户统计
     */
    @Data
    @ApiModel(value = "CustomerStatistics", description = "客户统计")
    public static class CustomerStatistics {
        @ApiModelProperty(value = "客户ID")
        private String customerId;
        @ApiModelProperty(value = "客户名称")
        private String customerName;
        @ApiModelProperty(value = "应收金额")
        private BigDecimal receivableAmount;
        @ApiModelProperty(value = "已收金额")
        private BigDecimal receivedAmount;
        @ApiModelProperty(value = "逾期金额")
        private BigDecimal overdueAmount;
        @ApiModelProperty(value = "单据数量")
        private Integer count;
    }
}

