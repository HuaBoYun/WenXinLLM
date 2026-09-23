package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName ProjectStatusVo
 * @Description 审计项目查询入参
 * @Author ZiYao
 * @Date 2022/4/12 19:19
 * @Version 1.0
 */
@Data
@Schema(name="项目基础参数查询入参")
public class ProjectAuditVo extends BaseVo {
    @Schema(name = "项目编号")
    private String projectCode;

    @Schema(name = "项目名称")
    private String projectName;

    @Schema(name = "审计单位")
    private String auditUnit;

    @Schema(name = "被审计单位")
    private String auditedUnit;

    @Schema(name="组织id",hidden=true)
    private BigDecimal orgId;

    @Schema(name = "用户登录的token")
    private String token;
}
