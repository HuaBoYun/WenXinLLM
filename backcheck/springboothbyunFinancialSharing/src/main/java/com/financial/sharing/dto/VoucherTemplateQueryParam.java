package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 凭证模板查询参数
 *
 * @author system
 * @since 2024-12-07
 */
@Data
@ApiModel("凭证模板查询参数")
public class VoucherTemplateQueryParam {

    @ApiModelProperty(value = "页码", example = "1")
    @Min(value = 1, message = "页码最小值为1")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页条数", example = "10")
    @Min(value = 1, message = "每页条数最小值为1")
    @Max(value = 100, message = "每页条数最大值为100")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "偏移量", hidden = true)
    private Integer offset = 0;

    @ApiModelProperty("关键字搜索（模板编码、模板名称、描述）")
    private String keyword;

    @ApiModelProperty("模板类型")
    private String templateType;

    @ApiModelProperty("模板状态")
    private String status;

    @ApiModelProperty("账簿ID")
    private Long bookId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("创建人ID")
    private Long creatorId;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;
}