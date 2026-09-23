package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 辅助核算项目查询参数
 */
@Data
@ApiModel("辅助核算项目查询参数")
public class AuxiliaryItemQueryParam {

    @ApiModelProperty("账套ID")
    private Long bookId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("项目类型")
    private String itemType;

    @ApiModelProperty("项目编码")
    private String itemCode;

    @ApiModelProperty("项目名称")
    private String itemName;

    @ApiModelProperty("父项目ID")
    private Long parentId;

    @ApiModelProperty("是否启用")
    private Boolean enabled;

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("页大小")
    private Integer pageSize = 10;
}