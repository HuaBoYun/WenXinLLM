package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 应收单据审核参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArReceivableAuditParam", description = "应收单据审核参数")
public class ArReceivableAuditParam {

    @ApiModelProperty(value = "应收单据ID列表", required = true)
    @NotNull(message = "应收单据ID列表不能为空")
    private List<String> receivableIds;

    @ApiModelProperty(value = "审核操作(1通过 2拒绝)", required = true)
    @NotNull(message = "审核操作不能为空")
    private Integer auditAction;

    @ApiModelProperty(value = "审核意见")
    @Size(max = 1000, message = "审核意见长度不能超过1000个字符")
    private String auditComments;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}

