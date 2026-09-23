package com.financial.sharing.dto.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预收款查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArAdvanceReceiptQueryParam", description = "预收款查询参数")
public class ArAdvanceReceiptQueryParam extends PageableParam {

    @ApiModelProperty(value = "预收款单号")
    private String advanceNo;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "预收状态(0未冲销 1部分冲销 2全部冲销)")
    private Integer advanceStatus;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "结束日期")
    private String endDate;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}

