package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账期查询参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("账期查询参数")
public class PaymentTermsQueryParam {

    @ApiModelProperty(value = "当前页码(前端传递page)")
    private Integer page;

    @ApiModelProperty(value = "每页大小(前端传递size)")
    private Integer size;

    @ApiModelProperty("当前页码(实际使用)")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小(实际使用)")
    private Integer pageSize = 15;

    @ApiModelProperty("账期编码")
    private String termsCode;

    @ApiModelProperty("账期名称(模糊查询)")
    private String termsName;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("状态(0:停用,1:启用)")
    private Integer status;

    @ApiModelProperty("关键字(编码/名称)")
    private String keyword;

    @ApiModelProperty("token")
    private String token;

    /**
     * 获取实际使用的页码,如果page不为空则使用page,否则使用pageNo
     */
    public Integer getPageNo() {
        return page != null ? page : (pageNo != null ? pageNo : 1);
    }

    /**
     * 获取实际使用的每页大小,如果size不为空则使用size,否则使用pageSize
     */
    public Integer getPageSize() {
        return size != null ? size : (pageSize != null ? pageSize : 15);
    }
}

