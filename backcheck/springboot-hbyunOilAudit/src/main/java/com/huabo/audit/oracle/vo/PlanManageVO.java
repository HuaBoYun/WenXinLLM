package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlanManageVO extends BaseVo {
    @Schema(name = "计划编号")
    private String plancode;
    @Schema(name = "计划年度")
    private String planYear;
    @Schema(name = "计划名称")
    private String planname;
    @Schema(name = "起始时间")
    private String pstartDates;
    @Schema(name = "终止时间")
    private String pendDates;
    @Schema(name = "计划负责人id")
    private String pmids;
    @Schema(name = "token")
    private String token;
    @Schema(name = "orgid")
    private String orgid;
    @Schema(name = "查询框代码")
    private String choiceSearch;
    @Schema(name = "t")
    private String t;
    @Schema(name = "计划id")
    private String  planId;
}
