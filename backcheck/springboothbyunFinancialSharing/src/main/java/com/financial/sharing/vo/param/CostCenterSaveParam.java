package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 成本中心保存参数
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("成本中心保存参数")
public class CostCenterSaveParam {

    @ApiModelProperty("成本中心ID，新增时为空")
    private String centerId;

    @ApiModelProperty(value = "成本中心编码", required = true)
    @NotBlank(message = "成本中心编码不能为空")
    private String centerCode;

    @ApiModelProperty(value = "成本中心名称", required = true)
    @NotBlank(message = "成本中心名称不能为空")
    private String centerName;

    @ApiModelProperty(value = "中心类型: 1-成本中心, 2-利润中心, 3-投资中心", required = true)
    @NotNull(message = "中心类型不能为空")
    private Integer centerType;

    @ApiModelProperty("上级中心ID")
    private String parentCenterId;

    @ApiModelProperty("上级中心名称")
    private String parentCenterName;

    @ApiModelProperty("中心级次")
    private Integer centerLevel;

    @ApiModelProperty("是否末级: 1-是, 0-否")
    private Integer isLeaf;

    @ApiModelProperty("负责人ID")
    private String managerId;

    @ApiModelProperty("负责人名称")
    private String managerName;

    @ApiModelProperty("成本分摊方法: 1-直接分摊, 2-阶梯分摊, 3-比例分摊")
    private Integer costAllocationMethod;

    @ApiModelProperty("是否启用: 1-启用, 0-停用")
    private Integer isEnabled;

    @ApiModelProperty("账簿ID")
    private String bookId;

    @ApiModelProperty("租户ID")
    private String tenantId;

    @ApiModelProperty("成本中心描述")
    private String centerDesc;
}
