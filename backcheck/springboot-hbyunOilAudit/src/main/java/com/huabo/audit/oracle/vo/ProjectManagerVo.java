package com.huabo.audit.oracle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ProjectStatusVo
 * @Description 项目管理查询入参
 * @Author ZiYao
 * @Date 2022/4/12 19:19
 * @Version 1.0
 */
@Data
@Schema(name="项目管理查询入参")
public class ProjectManagerVo extends BaseVo {
    @Schema(name = "项目名称")
    private String projectName;

    @Schema(name = "项目经理id")
    private Integer projectManagerId;

    @Schema(name = "项目来源")
    private String projectSources;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "搜索-开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @Schema(name = "搜索-结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @Schema(name="组织id",hidden=true)
    private BigDecimal orgId;

    @Schema(name = "用户登录的token")
    private String token;

    @Schema(name="登陆人Id",hidden=true)
    private BigDecimal staffId;
}
