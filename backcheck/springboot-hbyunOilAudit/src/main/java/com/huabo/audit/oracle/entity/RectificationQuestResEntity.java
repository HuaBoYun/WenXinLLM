package com.huabo.audit.oracle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 描述: 审计项目
 * author: ziyao
 * date: 2022-04-12
 */
@Data
@Schema(name="整改问题分页反参")
public class RectificationQuestResEntity {

    @Schema(name = "审计项目ID")
    private Integer projectId;

    @Schema(name = "项目编码")
    private String projectCode;

    @Schema(name = "审计项目名称")
    private String projectName;

    @Schema(name = "审计单位")
    private String auditUnit;

    @Schema(name = "被审计单位")
    private String auditedUnit;

    @Schema(name = "计划年份")
    private String planYear;

    @Schema(name = "发现问题数量")
    private Integer WTZS;

    @Schema(name = "发现问题数量")
    private Integer FQZGS;

    @Schema(name = "发现问题数量")
    private Integer YZG;

    @Schema(name = "发现问题数量")
    private Integer WZG;
}
