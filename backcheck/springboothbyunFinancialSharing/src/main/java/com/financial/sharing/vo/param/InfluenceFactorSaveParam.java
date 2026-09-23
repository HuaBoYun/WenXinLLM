package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 影响因素保存参数
 * @author system
 * @since 2024-12-19
 */
@Data
@ApiModel(value = "InfluenceFactorSaveParam", description = "影响因素保存参数")
public class InfluenceFactorSaveParam {

    @ApiModelProperty(value = "影响因素ID(新增时为空)")
    private Long factorId;

    @ApiModelProperty(value = "影响因素编码", required = true)
    @NotBlank(message = "影响因素编码不能为空")
    @Size(max = 50, message = "影响因素编码长度不能超过50个字符")
    private String factorCode;

    @ApiModelProperty(value = "影响因素名称", required = true)
    @NotBlank(message = "影响因素名称不能为空")
    @Size(max = 200, message = "影响因素名称长度不能超过200个字符")
    private String factorName;

    @ApiModelProperty(value = "影响因素类型(1固定2比率3公式4手工)", required = true)
    @NotNull(message = "影响因素类型不能为空")
    private Integer factorType;

    @ApiModelProperty(value = "数据类型(STRING/NUMBER/BOOLEAN)")
    private String dataType;

    @ApiModelProperty(value = "因素值")
    private String factorValue;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @ApiModelProperty(value = "计算公式")
    private String calculationFormula;

    @ApiModelProperty(value = "影响范围")
    private String affectScope;

    @ApiModelProperty(value = "优先级")
    private Integer priorityLevel;

    @ApiModelProperty(value = "因素描述")
    private String description;

    @ApiModelProperty(value = "是否启用(0否1是)")
    private Integer isEnabled;

    @ApiModelProperty(value = "排序号")
    private Integer sortOrder;

    @ApiModelProperty(value = "账簿ID", required = true)
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
