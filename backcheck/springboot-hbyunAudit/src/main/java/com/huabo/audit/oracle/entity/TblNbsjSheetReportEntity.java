package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "TBL_NBSJ_SHEET_REPORT")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjSheetReportEntity {
	
	@Schema(name = "主键")
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@TableId("reportid")
	@TableField("reportid")
	@Column(name = "reportid")
	private BigDecimal reportid;
	@Transient
	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblNbsjSheetEntity tblNbsjSheet;
	
	@TableField(value = "reportcontent")
	@Column(name = "reportcontent")
	@Schema
	private String reportConcent;
	
	@TableField(value = "reportorgids")
	@Column(name = "reportorgids")
	@Schema
	private String sjdeptIds;
	
	@TableField(value = "reportorgname")
	@Schema(hidden=true)
	private String sjdeptNames;
	@Column(name = "SHEETID")
	@TableField(value = "SHEETID")
	@Schema
	private BigDecimal sheetId;
	

}
