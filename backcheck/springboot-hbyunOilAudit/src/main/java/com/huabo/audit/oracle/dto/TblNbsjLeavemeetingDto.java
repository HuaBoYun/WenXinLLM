package com.huabo.audit.oracle.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Transient;

import com.huabo.audit.oracle.entity.TblNbsjLeavemeetingEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="离场纪要保存对象")
public class TblNbsjLeavemeetingDto {
	private TblNbsjLeavemeetingEntity tblNbsjLeavemeetingEntity;
	
	@Schema(name = "ID")
	@Transient
	private BigDecimal leaveid;
	
	@Schema(name = "离场纪要编号")
	@Transient
	private String leavecoed;
	
	@Schema(name = "离场纪要名称")
	@Transient
	private String leavename;
	
	@Schema
	@Transient
	private Date creatrtime;
	
	@Schema(name = "内容")
	@Transient
	private String content;
	
	@Schema
	@Transient
	private String status;
	
	@Schema
	@Transient
	private String createstaffid;
	
}
