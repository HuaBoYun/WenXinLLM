package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量更新状态参数
 *
 * @author system
 * @since 2024-12-19
 */
@Data
@ApiModel("批量更新状态参数")
public class BatchUpdateStatusParam {

    @ApiModelProperty(value = "汇率ID列表", required = true)
    @NotEmpty(message = "汇率ID列表不能为空")
    private List<Long> rateIds;

    @ApiModelProperty(value = "启用状态", required = true)
    @NotNull(message = "启用状态不能为空")
    private Integer isEnabled;
}