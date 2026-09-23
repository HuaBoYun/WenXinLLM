package com.huabo.fxgl.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="重大风险创建DTO")
public class TblMajorRiskbranchCreateDto {
	
	  @Schema(name = "年度")
	  private String nd;

	    @Schema(name = "季度名称")
	    private String jd;

	    @Schema(name = "创建人名称")
	    private String createname;
        
	    @Schema(name = "审批状态")
	    private BigDecimal status;
	    
	    @Schema(name = "填报单位名称")
	    private String linkOrgName;
	    
	    
	    @Schema(name="上报状态 1上报 0未上报")
	    private BigDecimal toreport;
	    

}
