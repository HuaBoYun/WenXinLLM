package com.huabo.audit.oracle.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Transient;

import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计通知书保存对象")
public class TblNbsjAdvicenoteDto {
	
	private TblNbsjAdvicenoteEntity tblNbsjAdvicenoteEntity;
	
	@Schema(name = "ID")
	@Transient
	private BigDecimal adviceid;
	
	@Schema
	@Transient
	private Date creatrtime;
	
	@Schema(name = "审计通知书编号")
	@Transient
	private String advicecoed;
	
	@Schema(name = "审计通知书名称")
	@Transient
	private String advicename;
	
	@Schema(name = "内容")
	@Transient
	private String content;
	
	@Schema
	@Transient
	private String des;
	
	@Schema
	@Transient
	private Integer status;
	
	@Schema
	@Transient
	private String createstaffid;
	
	
}
