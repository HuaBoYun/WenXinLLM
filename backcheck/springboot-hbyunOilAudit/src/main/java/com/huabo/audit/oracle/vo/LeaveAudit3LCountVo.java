package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Schema(name="三级单位离任审计 - 统计")

public class LeaveAudit3LCountVo {
		
	@Schema(name = "三级级单位离任 -  项目名称=二级机构+所属三级机构离任经济责任审计")
	private String PROJECTTWO;
	
	@Schema(name = "单位")
	private String NAMETHREE;
	
	@Schema(name = "个数")
	private Integer COUNTS;

	@Schema(name = "应审个数")
	private Integer YSSL;
	
	@Schema(name = "已审个数")
	private Integer SJSL;
	
	
	@Schema(name = "未审个数")
	private Integer WSSL;
	
	@Schema(name = "不需审个数")
	private Integer BSSL;

}
