package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计模板查询对象")

public class TblNbsjTempleteVo {
		
	@Schema(name = "模板名称")
	private String templeteName;

	@Schema(name = "模板状态(启用:1、禁用:-1)")
	private Integer status;
	
	@Schema(name = "//模板类型：审计模板：0；    指引模板：1；    审计经验：2")
    private String tempType;//模板类型：审计模板：0；    指引模板：1；    审计经验：2
	
	@Schema(name = "审计类型")
	private String templeteType;
	
}
