package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预付款查询参数
 * @author system
 * @since 2025-01-13
 */
@Data
@ApiModel("预付款查询参数")
public class PrepaymentQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页条数")
    private Integer pageSize = 20;

    @ApiModelProperty("预付款单号(模糊查询)")
    private String prepaymentNo;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("预付状态(0:未冲销,1:部分冲销,2:全部冲销)")
    private Integer prepaymentStatus;
}
