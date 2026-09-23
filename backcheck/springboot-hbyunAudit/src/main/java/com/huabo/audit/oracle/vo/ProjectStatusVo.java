package com.huabo.audit.oracle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ProjectStatusVo
 * @Description 审计项目查询入参
 * @Author ZiYao
 * @Date 2022/4/12 19:19
 * @Version 1.0
 */
@Data
@Schema(name="审计项目查询入参")
public class ProjectStatusVo extends ProjectAuditVo {
    @Schema(name = "项目经理id")
    private Integer projectManagerId;

    @Schema(name = "搜索-开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @Schema(name = "搜索-结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @Schema(name = "状态")
    private Integer status;
}
