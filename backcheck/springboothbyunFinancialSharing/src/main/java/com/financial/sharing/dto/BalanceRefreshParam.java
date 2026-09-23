package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 余额刷新参数
 *
 * @author system
 * @since 2024-12-07
 */
@Data
@ApiModel("余额刷新参数")
public class BalanceRefreshParam {

    @ApiModelProperty(value = "账簿ID", required = true)
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    @ApiModelProperty(value = "会计期间", required = true)
    @NotBlank(message = "会计期间不能为空")
    private String accountingPeriod;

    @ApiModelProperty("科目编码列表（为空表示刷新所有科目）")
    private List<String> accountCodes;

    @ApiModelProperty("币种代码（为空表示刷新所有币种）")
    private String currencyCode;

    @ApiModelProperty("是否强制刷新（忽略缓存）")
    private Boolean forceRefresh = false;

    @ApiModelProperty("是否异步执行")
    private Boolean async = true;

    @ApiModelProperty("刷新范围：ALL-全部, CHANGED-只刷新有变动的")
    private String refreshScope = "ALL";

    @ApiModelProperty("备注")
    private String remark;
}