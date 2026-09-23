package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="项目执行一览列表查询入参")
public class TblnbsjProjectZXYLVo extends BaseVo{
	
	@Schema(name = "计划编号")
	private String planNumber;
	
	@Schema(name = "计划名称")
	private String planName;
	
	@Schema(name = "项目名称")
    private String prjoectName;
	
	@Schema(name = "项目编号")
	private String projectCode;
	
	@Schema(name = "计划年度")
	private String planYear;
	
	@Schema(name="orgid",hidden=true)
    private Integer orgId;
	
}