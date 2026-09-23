package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_YJPT_OUTERRULE")
//@TableName("TBL_NBSJ_OUTERRULE")
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
	@Schema(name = "文件名称")
	private String rulename;

	@TableField(value = "publishorg")
	@Schema(name = "发文部门")
	private String publishorg;

	@TableField(value = "publishdate")
	@Schema
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date publishdate;

	@TableField(value = "rulenumber")
	@Schema(name = "发文文号")
	private String rulenumber;

	@TableField(value = "memo")
	@Schema
	private String memo;

	@TableField(value = "outruletype")
	@Schema
	private String outruletype;

	@TableField(value = "effectivelevel")
	@Schema(name = "效力级别")
	private String effectivelevel;

	@TableField(value = "timeliness")
	@Schema(name = "时效性")
	private String timeliness;

	@TableField(value = "takeeffecttime")
	@Schema
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date takeeffecttime;

	@TableField(value = "summaryinfo")
	@Schema(name = "摘要")
	private String summaryinfo;

	@TableField(value = "bodyinfo")
	@Schema(name = "富文本框")
	private String bodyinfo;

	@TableField(value = "enteringperson")
	@Schema(name = "录入人")
	private String enteringperson;

	@TableField(value = "enteringtime")
	@Schema(name = "录入时间")
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
