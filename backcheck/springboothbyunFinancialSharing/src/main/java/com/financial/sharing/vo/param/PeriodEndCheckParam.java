package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 期末处理检查参数
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
@ApiModel("期末处理检查参数")
public class PeriodEndCheckParam {

    @ApiModelProperty(value = "会计期间", required = true, example = "2024-12")
    private String accountingPeriod;

    @ApiModelProperty(value = "账簿ID", required = true)
    private Long bookId;

    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;

    @ApiModelProperty(value = "检查项ID列表")
    private List<String> checkItemIds;

    @ApiModelProperty(value = "是否强制执行检查", example = "false")
    private Boolean forceCheck = false;

    @ApiModelProperty(value = "检查项类型", example = "ALL")
    private String checkType = "ALL"; // ALL, SPECIFIC

    @ApiModelProperty(value = "检查项ID", example = "CHECK_001")
    private String checkItemId;
}