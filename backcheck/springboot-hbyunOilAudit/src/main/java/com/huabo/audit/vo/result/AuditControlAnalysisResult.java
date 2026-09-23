package com.huabo.audit.vo.result;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuditControlAnalysisResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Schema(name="项目年度")
	private Integer xmnd;
	
	@Schema(name="项目数量")
	private Integer itemCount;
	
	
	@Schema(name="工作问题底稿数量")
	private Integer workSheetQuCount;
	
	@Schema(name="工作复核底稿数量")
	private Integer workSheetFhCount;
	
	@Schema(name="工作方案数量")
	private Integer workPlanCount;
	
	@Schema(name="实施方案次数")
	private Integer implSuperCount;
	
	@Schema(name="督导方案次数")
	private Integer aoverCount;
	
	@Schema(name="员工主键")
	private BigDecimal staffId;
	
	@Schema(name="员工姓名")
	private String staffName;
	
	
}
