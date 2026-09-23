package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计取证单书列表查询入参")
public class TblNbsjCertificateVo extends BaseVo{
	@Schema(name = "编号")
    private Integer projectId;

    @Schema(name = "所属组织")
    private BigDecimal orgid;
    
    @Schema(name = "项目名称")
    private String projectName;
    
    @Schema(name = "审计事项")
    private String auditMatter;
    
    @Schema(name = "审计事项摘要")
    private String auditAbstract;
    
    @Schema(name = "关联底稿ID")
    private String sheetid;
}