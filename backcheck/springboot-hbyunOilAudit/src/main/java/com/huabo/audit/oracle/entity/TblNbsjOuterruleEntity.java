package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_OUTERRULE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjOuterruleEntity {

//private static final long serialVersionUID = 1L;
	
	@TableId(value = "outrulid", type= IdType.AUTO)
	@Schema
	private BigDecimal outrulid;
	
	@TableField(value = "rulecode")
	@Schema(name = "文件编号")
	private String rulecode;
	
	@TableField(value = "rulename")
	@Schema
	private String rulename;
	
	@TableField(value = "publishorg")
	@Schema
	private String publishorg;
	
	@TableField(value = "publishdate")
	@Schema
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date publishdate;
	
	@TableField(value = "rulenumber")
	@Schema
	private String rulenumber;
	
	@TableField(value = "memo")
	@Schema
	private String memo;
	
	@TableField(value = "outruletype")
	@Schema
	private String outruletype;
	
	@TableField(value = "effectivelevel")
	@Schema
	private String effectivelevel;
	
	@TableField(value = "timeliness")
	@Schema
	private String timeliness;
	
	@TableField(value = "takeeffecttime")
	@Schema
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date takeeffecttime;
	
	@TableField(value = "summaryinfo")
	@Schema
	private String summaryinfo;
	
	@TableField(value = "bodyinfo")
	@Schema
	private String bodyinfo;
	
	@TableField(value = "enteringperson")
	@Schema
	private String enteringperson;
	
	@TableField(value = "enteringtime")
	@Schema
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date enteringtime;
	
	@TableField(value = "belongcompany")
	@Schema
	private String belongCompany;
	
	@TableField(value = "tblorganization")
	@Schema(hidden=true)
	private TblOrganization tblOrganization;
	
	@TableField("")
	@Schema(hidden=true)
    private String orgname;
	
}
