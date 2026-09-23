package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SjjscDpVo {
    @Schema(name = "项目ID")
    private String projectId;
    @Schema(name = "审计对象")
    private String ORGIDNAMES;
    @Schema(name = "项目名称")
    private String PRJOECTNAME;
    @Schema(name = "审计开始时间")
    private String STARTDATE;
    @Schema(name = "审计类型")
    private String AUDITTYPE;
    @Schema(name = "主管部门")
    private String ORGNAME;
    @Schema(name = "整改进展情况")
    private String STATUS;
    @Schema(name = "审计来源")
    private String PROJECTSOURCE;
    @Schema(name = "审计结束时间")
    private String ENDDATE;
    @Schema(name = "已整改数量")
    private String yzg;
    @Schema(name = "已整改金额")
    private String yzj;
    @Schema(name = "未整改数量")
    private String wzg;
    @Schema(name = "整改总数")
    private String zs;
}
