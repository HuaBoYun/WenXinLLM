package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 固定资产卡片保存参数
 * @author system
 * @since 2026-01-21
 */
@Data
@ApiModel(value = "FixedAssetCardSaveParam", description = "固定资产卡片保存参数")
public class FixedAssetCardSaveParam {

    @ApiModelProperty(value = "资产ID（更新时必填）")
    private String assetId;

    @ApiModelProperty(value = "资产编码", required = true)
    @NotBlank(message = "资产编码不能为空")
    private String assetCode;

    @ApiModelProperty(value = "资产名称", required = true)
    @NotBlank(message = "资产名称不能为空")
    private String assetName;

    @ApiModelProperty(value = "资产类别ID", required = true)
    @NotBlank(message = "资产类别ID不能为空")
    private String categoryId;

    @ApiModelProperty(value = "资产原值", required = true)
    @NotNull(message = "资产原值不能为空")
    private BigDecimal originalValue;

    @ApiModelProperty(value = "残值")
    private BigDecimal residualValue;

    @ApiModelProperty(value = "折旧方法", required = true)
    @NotBlank(message = "折旧方法不能为空")
    private String depreciationMethod;

    @ApiModelProperty(value = "使用年限(月)", required = true)
    @NotNull(message = "使用年限不能为空")
    private Integer usefulLife;

    @ApiModelProperty(value = "使用部门ID", required = true)
    @NotBlank(message = "使用部门ID不能为空")
    private String deptId;

    @ApiModelProperty(value = "责任人")
    private String responsiblePerson;

    @ApiModelProperty(value = "购置日期", required = true)
    @NotNull(message = "购置日期不能为空")
    private LocalDate purchaseDate;

    @ApiModelProperty(value = "开始折旧日期", required = true)
    @NotNull(message = "开始折旧日期不能为空")
    private LocalDate startDepreciationDate;

    @ApiModelProperty(value = "资产状态")
    private String status;

    @ApiModelProperty(value = "规格型号")
    private String specification;

    @ApiModelProperty(value = "生产厂商")
    private String manufacturer;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "存放地点")
    private String location;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "操作人ID")
    private String operatorId;
}

