package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 供应商查询参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("供应商查询参数")
public class SupplierQueryParam {

    @ApiModelProperty("当前页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 15;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("供应商名称(模糊查询)")
    private String supplierName;

    @ApiModelProperty("供应商分类(1:原材料,2:设备,3:服务,4:其他)")
    private Integer supplierCategory;

    @ApiModelProperty("供应商状态(1:正常,2:暂停,3:黑名单)")
    private Integer supplierStatus;

    @ApiModelProperty("信用等级(AAA,AA,A,BBB,BB,B)")
    private String creditLevel;

    @ApiModelProperty("统一社会信用代码")
    private String creditCode;

    @ApiModelProperty("联系人")
    private String contactPerson;

    @ApiModelProperty("关键字(编码/名称/联系人)")
    private String keyword;
}

