package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计模板查询对象")

public class TblGkProjectVo {
		
	@Schema(name = "项目编号")
	private String PROJECTCODE;
	
	@Schema(name = "项目名称")
	private String PRJOECTNAME;
	
	@Schema(name = "审计单位")
	private String AUDITORGNAME;
	
	@Schema(name = "被审计单位")
	private String ORGNAME;
	
	@Schema(name = "被审计单位")
	private String REALNAME;
	
	@Schema(name = "项目经理")
	private String PMNAME;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name = "开始时间")
	private Date starttime;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name = "结束时间")
	private Date endtime;
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
