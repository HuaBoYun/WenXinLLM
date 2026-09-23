package com.huabo.audit.oracle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ProjectStatusVo
 * @Description 项目任务分配查询反参
 * @Author ZiYao
 * @Date 2022/4/12 19:19
 * @Version 1.0
 */
@Data
@Schema(name="项目任务分配查询反参")
public class ProjectTaskEntity {
    @Schema(name = "项目ID")
    private BigDecimal projectId;

    @Schema(name = "项目编号")
    private String projectCode;

    @Schema(name = "项目名称")
    private String projectName;

    @Schema(name = "项目对象")
    private String projectObj;

    @Schema(name = "开始时间")
    private Date startDate;

    @Schema(name = "结束时间")
    private Date endDate;

    @Schema(name = "状态:1启动 0未启动 2实施 3完成 4归档")
    private Integer status;
}
