package com.huabo.audit.oracle.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lyz
 * @description
 */
@Data
public class AuditPlanListSearchVO {
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
