package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 费用项目保存参数
 */
@Data
@ApiModel("费用项目保存参数")
public class TblExpenseItemSaveParam {

    @ApiModelProperty("费用项目ID")
    private String itemId;

    @ApiModelProperty(value = "费用项目编码", required = true)
    @NotBlank(message = "费用项目编码不能为空")
    private String itemCode;

    @ApiModelProperty(value = "费用项目名称", required = true)
    @NotBlank(message = "费用项目名称不能为空")
    private String itemName;

    @ApiModelProperty("费用类型")
    private String itemType;

    @ApiModelProperty("费用项目描述")
    private String itemDescription;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("父级ID")
    private String parentId;

    @ApiModelProperty("排序号")
    private Integer sortOrder;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;

    @ApiModelProperty("备注")
    private String remark;
}

