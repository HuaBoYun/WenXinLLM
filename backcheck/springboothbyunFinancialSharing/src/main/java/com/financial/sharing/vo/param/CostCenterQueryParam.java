package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 成本中心查询参数
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("成本中心查询参数")
public class CostCenterQueryParam extends PageableParam {

    @ApiModelProperty("成本中心编码")
    private String centerCode;

    @ApiModelProperty("成本中心名称")
    private String centerName;

    @ApiModelProperty("中心类型: 1-成本中心, 2-利润中心, 3-投资中心")
    private Integer centerType;

    @ApiModelProperty("上级中心ID")
    private String parentCenterId;

    @ApiModelProperty("负责人ID")
    private String managerId;

    @ApiModelProperty("是否启用: 1-启用, 0-停用")
    private Integer isEnabled;

    @ApiModelProperty("账簿ID")
    @NotNull(message = "账簿ID不能为空")
    private String bookId;

    @ApiModelProperty("租户ID")
    @NotNull(message = "租户ID不能为空")
    private String tenantId;
}
