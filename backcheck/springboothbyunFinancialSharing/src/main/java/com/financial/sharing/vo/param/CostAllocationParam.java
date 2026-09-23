package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 成本分摊参数
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("成本分摊参数")
public class CostAllocationParam {

    @ApiModelProperty(value = "分摊期间", required = true)
    @NotBlank(message = "分摊期间不能为空")
    private String allocationPeriod;

    @ApiModelProperty(value = "源成本中心ID", required = true)
    @NotNull(message = "源成本中心不能为空")
    private String sourceCostCenterId;

    @ApiModelProperty(value = "目标成本中心ID列表", required = true)
    @NotEmpty(message = "目标成本中心不能为空")
    private List<String> targetCostCenterIds;

    @ApiModelProperty(value = "分摊方法", required = true)
    @NotBlank(message = "分摊方法不能为空")
    private String allocationMethod;

    @ApiModelProperty(value = "分摊基础", required = true)
    @NotNull(message = "分摊基础不能为空")
    private Map<String, BigDecimal> allocationBasis;

    @ApiModelProperty(value = "分摊总额", required = true)
    @NotNull(message = "分摊总额不能为空")
    private BigDecimal totalAmount;

    @ApiModelProperty("是否生成凭证")
    private Boolean isGenerateVoucher;

    @ApiModelProperty("账簿ID")
    private String bookId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("租户ID")
    private String tenantId;
}
