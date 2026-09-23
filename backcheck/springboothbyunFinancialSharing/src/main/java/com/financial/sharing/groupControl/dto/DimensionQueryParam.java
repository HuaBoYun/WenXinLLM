package com.financial.sharing.groupControl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 维度查询参数
 * 
 * @author 华博云开发团队
 * @since 2026-01-30
 */
@Data
@Schema(name = "维度查询参数")
public class DimensionQueryParam {

    @Schema(name = "维度ID")
    private String dimensionId;

    @Schema(name = "维度编码")
    private String dimensionCode;

    @Schema(name = "维度名称")
    private String dimensionName;

    @Schema(name = "维度类型")
    private String dimensionType;

    @Schema(name = "维度分类")
    private String dimensionCategory;

    @Schema(name = "状态")
    private String status;

    @Schema(name = "租户ID")
    private String tenantId;

    @Schema(name = "页码")
    private Integer pageNumber = 1;

    @Schema(name = "每页大小")
    private Integer pageSize = 10;
}

