package com.huabo.audit.oracle.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="我的底稿保存对象")
public class TblNbsjSheetDto {

	private TblNbsjSheetEntity tblNbsjSheetEntity;
	
	@Schema(name = "ID")
	@Transient
	private BigDecimal sheetId;
	
	@Schema(name = "底稿编号")
	@Transient
	private String sheetCode;
	
	@Schema(name = "底稿名称")
	@Transient
	private String sheetName;
	
	@Schema(name = "审计目的")
	@Transient
	private String sheetTarget;
	
	@Schema(name = "审计单位")
	@Transient
	private Integer auditOrg;
	
	@Schema
	@Transient
	private Date createTime;
	
	@Schema
	@Transient
	private Date updateTime;
	
	@Schema
	@Transient
	private Integer status;
	
	@Schema
	@Transient
	private String auditDesc;
	
	@Schema
	@Transient
	private String auditCourse;
	
	@Schema
	@Transient
	private String auditDiscoverable;
	
	@Schema
	@Transient
	private String approver;
	
//	@Schema
//	@Transient
//	private String riskLevel;
	
	@Schema
	@Transient
	private String suditProcess;
	
	@Schema
	@Transient
	private Integer auditStaffId;
	
	@Schema(name = "被审计单位")
	@Transient
	private String auditedUnit;
	
	@Schema(name = "审计人员")
	@Transient
	private String auditStaff;
	
	@Schema
	@Transient
	private String riskAttrbution;
	
	@Schema
	@Transient
	private String businessAffiliation;
	
	@Schema
	@Transient
	private String riskLevel;
	
	@Schema
	@Transient
	private String quesTitle;
	
	@Schema
	@Transient
	private String targetName;
	
	@Schema
	@Transient
	private String businessType;
	
	@Schema
	@Transient
	private String createStaffId;
	
}
