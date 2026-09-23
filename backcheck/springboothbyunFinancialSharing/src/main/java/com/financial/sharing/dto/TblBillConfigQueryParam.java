package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 单据配置查询参数
 */
@Data
@ApiModel("单据配置查询参数")
public class TblBillConfigQueryParam {

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 10;

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("配置编码")
    private String configCode;

    @ApiModelProperty("单据类型")
    private String billType;

    @ApiModelProperty("单据类型编码")
    private String billTypeCode;

    @ApiModelProperty("单据类型名称")
    private String billTypeName;

    @ApiModelProperty("单据编码")
    private String billCode;

    @ApiModelProperty("单据名称")
    private String billName;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;
}

