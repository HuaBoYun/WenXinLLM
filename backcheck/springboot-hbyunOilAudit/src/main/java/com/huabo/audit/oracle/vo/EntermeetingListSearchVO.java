package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EntermeetingListSearchVO {
	@Schema(name = "计划年度")
    private String palnyear;
    @Schema(name = "计划名称")
    private String planname;
    @Schema(name = "计划id")
    private Integer planid;
    private Integer  sumSize  =0;
    private Integer  wssSize  =0;
    private Integer  jxSize  =0;
    private Integer wcSize =0;
}
