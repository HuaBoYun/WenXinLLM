package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 批量操作参数
 * @author system
 * @since 2025-01-13
 */
@Data
@ApiModel("批量操作参数")
public class BatchOperationParam {

    @ApiModelProperty("账期ID列表")
    private List<String> termsIds;

    @ApiModelProperty("状态(0:停用,1:启用)")
    private Integer status;
}
