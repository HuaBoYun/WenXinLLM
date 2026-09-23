package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 成本归集参数
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("成本归集参数")
public class CostCollectionParam {

    @ApiModelProperty(value = "归集期间", required = true)
    @NotBlank(message = "归集期间不能为空")
    private String collectionPeriod;

    @ApiModelProperty(value = "成本中心ID列表", required = true)
    @NotEmpty(message = "成本中心不能为空")
    private List<Long> costCenterIds;

    @ApiModelProperty(value = "归集类型列表: 1-直接成本, 2-间接成本, 3-制造费用", required = true)
    @NotEmpty(message = "归集类型不能为空")
    private List<Integer> collectionTypes;

    @ApiModelProperty(value = "归集方式: 1-手工归集, 2-自动归集", required = true)
    @NotNull(message = "归集方式不能为空")
    private Integer collectionMethod;

    @ApiModelProperty("账簿ID")
    private Long bookId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("备注")
    private String remark;
}
