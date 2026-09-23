package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计模板查询对象")

public class TblGkZgQuestionVo {
		
	@Schema(name = "项目编号")
	private String PROJECTCODE;
	
	@Schema(name = "项目名称")
	private String PRJOECTNAME;
	
	@Schema(name = "审计单位")
	private String AUDITORGNAME;
	
	@Schema(name = "被审计单位")
	private String ORGNAME;
	
	@Schema(hidden=true)
	private String ORGID;

}
