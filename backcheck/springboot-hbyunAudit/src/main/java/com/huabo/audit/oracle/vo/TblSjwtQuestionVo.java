package com.huabo.audit.oracle.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="整改问题分析查询")

public class TblSjwtQuestionVo {
		
	@Schema(name = "项目编号")
	private String projectCode;




	@Schema(name = "项目名称")
	private String prjoectName;
	
	@Schema(name = "审计单位")
	private String auditOrgName;
	
	@Schema(name = "被审计单位")
	private String orgname;
	
	@Schema(hidden=true)
	private String ORGID;

	@Schema(name = "密级主键")
	@TableField("SECRECTLEVELID")
	private BigDecimal secrectLevelId;

	@Schema(name = "知悉范围 多个逗号分隔")
	@TableField("STAFFSCOPEIDS")
	private String staffScopeIds;

	@Schema(name = "知悉访问人员姓名 多个逗号分隔")
	@TableField("STAFFSCOPENAMES")
	private String staffScopeNames;

}
