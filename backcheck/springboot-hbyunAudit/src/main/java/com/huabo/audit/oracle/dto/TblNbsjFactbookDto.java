package com.huabo.audit.oracle.dto;

import java.util.Date;

import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.TblNbsjFactbookEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="事实确认书保存对象")
public class TblNbsjFactbookDto {
	
	private TblNbsjFactbookEntity tblNbsjFactbookEntity;
	
	@Schema(name = "ID")
	@Transient
	private Integer factid;
	
	@Schema(name = "确认书编号")
	@Transient
	private String factcode;
	
	@Schema(name = "确认书名称")
	@Transient
	private String factname;
	
	@Schema
	@Transient
	private Date createtime;
	
	@Schema
	@Transient
	private Date updatetime;
	
	@Schema
	@Transient
	private Integer status;
	
	@Schema
	@Transient
	private String describe;
	
	@Schema
	@Transient
	private String projectid;
	
	@Schema
	@Transient
	private String createstaffid;
	
	
	
	
}
