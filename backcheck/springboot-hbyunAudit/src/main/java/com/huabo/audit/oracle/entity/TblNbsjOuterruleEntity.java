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

import javax.persistence.Column;

@TableName("TBL_NBSJ_OUTERRULE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjOuterruleEntity extends FlexibleFieldEntity {

//private static final long serialVersionUID = 1L;
	
	@TableId(value = "outrulid", type= IdType.INPUT)
	@Schema
	private BigDecimal outrulid;
	
	@TableField(value = "createorgid")
	@Schema(name = "所属公司")
	private BigDecimal createorgid;
	
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

	@TableField(exist = false)
	@Schema(hidden=true)
	private TblOrganization tblOrganization;

	@TableField(exist = false)
	@Schema(hidden=true)
    private String orgname;

	@Schema(name = "密级主键")
	@TableField("SECRECTLEVELID")
	private BigDecimal secrectLevelId;

	@Schema(name = "知悉范围 多个逗号分隔")
	@TableField("STAFFSCOPEIDS")
	private String staffScopeIds;

	@Schema(name = "知悉访问人员姓名 多个逗号分隔")
	@TableField("STAFFSCOPENAMES")
	private String staffScopeNames;

	@Schema(name = "创建人 用户表外键")
	@Column(name = "CREATESTAFFID")
	@TableField("CREATESTAFFID")
	private BigDecimal createstaffid;

	@Schema(name = "制度类型")
	@Column(name = "SYSTEMTYPE")
	@TableField("SYSTEMTYPE")
	private BigDecimal systemType;
	
}
