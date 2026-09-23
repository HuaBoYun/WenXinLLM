package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 固定资产折旧查询参数
 * @author system
 * @since 2026-01-22
 */
@Data
@ApiModel(value = "固定资产折旧查询参数")
public class FixedAssetDepreciationQueryParam {

    @ApiModelProperty(value = "资产编码")
    private String assetCode;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "资产类别ID")
    private String categoryId;

    @ApiModelProperty(value = "开始期间(YYYY-MM)")
    private String startPeriod;

    @ApiModelProperty(value = "结束期间(YYYY-MM)")
    private String endPeriod;

    @ApiModelProperty(value = "折旧方法")
    private String depreciationMethod;

    @ApiModelProperty(value = "折旧状态")
    private String status;

    @ApiModelProperty(value = "部门ID")
    private String deptId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = 10;
}

