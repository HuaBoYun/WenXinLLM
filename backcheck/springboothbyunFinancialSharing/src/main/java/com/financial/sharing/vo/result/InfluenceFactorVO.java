package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 影响因素返回对象
 * @author system
 * @since 2024-12-19
 */
@Data
@ApiModel(value = "InfluenceFactorVO", description = "影响因素返回对象")
public class InfluenceFactorVO {

    @ApiModelProperty(value = "影响因素ID")
    private Long factorId;

    @ApiModelProperty(value = "影响因素编码")
    private String factorCode;

    @ApiModelProperty(value = "影响因素名称")
    private String factorName;

    @ApiModelProperty(value = "影响因素类型(1固定2比率3公式4手工)")
    private Integer factorType;

    @ApiModelProperty(value = "影响因素类型名称")
    private String factorTypeName;

    @ApiModelProperty(value = "数据类型(STRING/NUMBER/BOOLEAN)")
    private String dataType;

    @ApiModelProperty(value = "数据类型名称")
    private String dataTypeName;

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

    @ApiModelProperty(value = "优先级名称")
    private String priorityLevelName;

    @ApiModelProperty(value = "因素分类")
    private String factorCategory;

    @ApiModelProperty(value = "计算方法")
    private String calculationMethod;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "因素描述")
    private String description;

    @ApiModelProperty(value = "是否启用(0否1是)")
    private Integer isEnabled;

    @ApiModelProperty(value = "是否启用名称")
    private String isEnabledName;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "更新人")
    private Long updater;
}
