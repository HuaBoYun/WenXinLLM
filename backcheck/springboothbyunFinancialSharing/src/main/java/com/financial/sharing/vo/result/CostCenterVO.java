package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 成本中心视图对象
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("成本中心视图对象")
public class CostCenterVO {

    @ApiModelProperty("成本中心ID")
    private String centerId;

    @ApiModelProperty("成本中心编码")
    private String centerCode;

    @ApiModelProperty("成本中心名称")
    private String centerName;

    @ApiModelProperty("中心类型: 1-成本中心, 2-利润中心, 3-投资中心")
    private Integer centerType;

    @ApiModelProperty("中心类型名称")
    private String centerTypeName;

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

    @ApiModelProperty("负责人姓名")
    private String managerName;

    @ApiModelProperty("成本分摊方法: 1-直接分摊, 2-阶梯分摊, 3-比例分摊")
    private Integer costAllocationMethod;

    @ApiModelProperty("成本分摊方法名称")
    private String allocationMethodName;

    @ApiModelProperty("是否启用: 1-启用, 0-停用")
    private Integer isEnabled;

    @ApiModelProperty("账簿ID")
    private String bookId;

    @ApiModelProperty("成本中心描述")
    private String centerDesc;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("更新人")
    private String updater;

    @ApiModelProperty("版本号")
    private Integer version;
}
