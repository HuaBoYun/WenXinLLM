package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName ProjectStatusVo
 * @Description 项目任务分配查询入参
 * @Author ZiYao
 * @Date 2022/4/12 19:19
 * @Version 1.0
 */
@Data
@Schema(name="项目任务分配查询入参")
public class ProjectTaskVo extends BaseVo {
    @Schema(name = "项目名称")
    private String projectName;

    @Schema(name = "项目对象")
    private String projectObj;

    @Schema(name="审批状态 1：未审批 2：审批中 3：审批驳回 4：审批通过",hidden=true)
    private Integer examineType;

    @Schema(name="组织id",hidden=true)
    private BigDecimal orgId;

    @Schema(name = "用户登录的token")
    private String token;
}
