package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="审计计划项目入参")
public class TblNbsjPlanProjectVo extends BaseVo{

    @Schema(name = "计划项目名称")
    private String projectname;
    
    @Schema(name = "工作目标")
    private String targetname;

    @Schema(name = "所属审计计划主键")
    private BigDecimal planId;
}
