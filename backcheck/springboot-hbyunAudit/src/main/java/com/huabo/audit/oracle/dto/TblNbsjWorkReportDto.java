package com.huabo.audit.oracle.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.TblNbsjWorkreport;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="工作日志保存对象")
public class TblNbsjWorkReportDto {
	
	private TblNbsjWorkreport tblNbsjWorkReportEntity;
	
	@Schema(name = "ID")
	@Transient
	private BigDecimal reportid;
	
	@Schema(name = "日志名称")
	@Transient
	private String reportname;
	
	@Schema(name = "日志时间")
	@Transient
	private Date reporttime;
	
	@Schema(name = "报告类型")
	@Transient
	private String reporttype;
	
	@Schema
	@Transient
	private String reportmode;
	
	@Schema(name = "报告人")
	@Transient
	private String reporterid;
	
	@Schema(name = "备注")
	@Transient
	private String memo;
	
	@Schema
	@Transient
	private String repdesc;
	
	@Schema(name = "报告部门")
	@Transient
	private String orgid;
	
	@Schema
	@Transient
	private String reportstatus;
	
	
}
