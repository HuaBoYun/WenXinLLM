package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 固定资产卡片查询参数
 * @author system
 * @since 2026-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FixedAssetCardQueryParam", description = "固定资产卡片查询参数")
public class FixedAssetCardQueryParam extends PageableParam {

    @ApiModelProperty(value = "资产编码")
    private String assetCode;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "资产类别ID")
    private String categoryId;

    @ApiModelProperty(value = "使用部门ID")
    private String deptId;

    @ApiModelProperty(value = "资产状态")
    private String status;

    @ApiModelProperty(value = "责任人")
    private String responsiblePerson;

    @ApiModelProperty(value = "购置日期开始")
    private LocalDate purchaseDateStart;

    @ApiModelProperty(value = "购置日期结束")
    private LocalDate purchaseDateEnd;

    @ApiModelProperty(value = "折旧方法")
    private String depreciationMethod;

    @ApiModelProperty(value = "存放地点")
    private String location;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}

