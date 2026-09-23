package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 余额查询参数
 *
 * @author system
 * @since 2024-12-07
 */
@Data
@ApiModel("余额查询参数")
public class BalanceQueryParam {

    @ApiModelProperty(value = "页码", example = "1")
    @Min(value = 1, message = "页码最小值为1")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页条数", example = "10")
    @Min(value = 1, message = "每页条数最小值为1")
    @Max(value = 100, message = "每页条数最大值为100")
    private Integer pageSize = 10;

    @ApiModelProperty("科目编码")
    private String accountCode;

    @ApiModelProperty("科目名称")
    private String accountName;

    @ApiModelProperty("会计期间")
    private String period;

    @ApiModelProperty("币种代码")
    private String currencyCode;

    @ApiModelProperty("账簿ID")
    private Long bookId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("余额类型：BEGINNING-期初, PERIOD-本期发生, ENDING-期末")
    private String balanceType;

    @ApiModelProperty("调整类型")
    private String adjustmentType;

    @ApiModelProperty("调整状态")
    private String status;

    @ApiModelProperty("是否包含下级科目")
    private Boolean includeSubAccounts = false;

    @ApiModelProperty("科目级次")
    private Integer accountLevel;

    @ApiModelProperty("科目类型：ASSET-资产, LIABILITY-负债, EQUITY-权益, REVENUE-收入, EXPENSE-费用")
    private String accountType;

    @ApiModelProperty("开始金额")
    private java.math.BigDecimal startAmount;

    @ApiModelProperty("结束金额")
    private java.math.BigDecimal endAmount;

    @ApiModelProperty("排序字段")
    private String sortField = "ACCOUNT_CODE";

    @ApiModelProperty("排序方向：ASC-升序, DESC-降序")
    private String sortOrder = "ASC";
}