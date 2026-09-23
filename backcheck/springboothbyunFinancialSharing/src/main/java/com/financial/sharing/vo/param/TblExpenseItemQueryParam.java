package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 费用项目查询参数
 */
@Data
@ApiModel("费用项目查询参数")
public class TblExpenseItemQueryParam {

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 10;

    @ApiModelProperty("费用项目编码")
    private String itemCode;

    @ApiModelProperty("费用项目名称")
    private String itemName;

    @ApiModelProperty("费用类型")
    private String itemType;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("父级ID")
    private String parentId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;
}

