package com.huabo.cybermonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 三表比对查询VO
 * @author system
 * @date 2025-01-01
 */
@Data
@Schema(name = "ThreeTableCompareQueryVO", description = "三表比对查询参数")
public class ThreeTableCompareQueryVO {

    @Schema(description = "企业名称（模糊查询）")
    private String companyName;

    @Schema(description = "差异状态(NONE/DIFF/ONLY_PROPERTY/ONLY_BUSI)")
    private String diffStatus;

    @Schema(description = "股东类型(ENTERPRISE/INDIVIDUAL/GOVERNMENT/INSTITUTION/FUND)")
    private String shareholderType;

    @Schema(description = "当前页码，默认1")
    private Integer pageNumber = 1;

    @Schema(description = "每页条数，默认20")
    private Integer pageSize = 20;
}
