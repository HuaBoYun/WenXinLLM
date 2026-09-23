package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="项目任务分配查询入参")
public class TblnbsjProjectRwfpVo extends BaseVo{
	
	@Schema(name = "项目名称")
    private String prjoectName;
	
	@Schema(name = "项目对象")
	private String auditedObject;
	
	@Schema(name="orgid",hidden=true)
    private BigDecimal orgId;
	
	@Schema(hidden=true)
    private BigDecimal pmId;
	
	@Schema(hidden=true)
    private BigDecimal staffId;
	
}