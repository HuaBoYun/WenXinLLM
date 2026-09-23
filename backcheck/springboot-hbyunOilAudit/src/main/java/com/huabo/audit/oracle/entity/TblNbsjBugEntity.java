package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_BUG")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjBugEntity {
	
	@TableId(value = "bugid", type= IdType.AUTO)
	@Schema(name = "主键ID")
	private BigDecimal bugid;
	
	@TableField(value = "bugnumber")
	@Schema(name = "缺陷编号")
	private String bugnumber;
	
	@TableField(value = "bugdescripte")
	@Schema(name = "缺陷描述")
	private String bugdescripte;
	
	@TableField(value = "discovertime")
	@Schema(name = "发现日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date discovertime;
	
	@TableField(value = "discoverperson")
	@Schema(name = "发现人")
	private String discoverperson;
	
	@TableField(value = "BUGPROPERTY")
	@Schema(name = "缺陷性质")
	private String bugproperty;
	
	@TableField(value = "bugsource")
	@Schema(name = "是否财务相关")
	private String bugsource;
	
	@TableField(value = "bugdepartment")
	@Schema(name = "缺陷部门")
	private String bugdepartment;
	
	@TableField(value = "needreform")
	@Schema(name = "是否需要整改")
	private String needreform;
	
	@TableField(value = "bugreformstatus")
	@Schema
	private String bugreformstatus;
	
	@TableField(value = "projectname")
	@Schema
	private String projectname;
	
	@TableField(value = "memo")
	@Schema
	private String memo;
	
	@TableField(value = "PROJECTID")
	@Schema(name = "关联项目Id")
	private Integer projectId;
	
	@TableField(value = "fatherbugid")
	@Schema
	private BigDecimal fatherbugid;
	
	@TableField(value = "bugbysystem")
	@Schema
	private String bugbysystem;
	
	@TableField(value = "tblInnerrules")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set tblInnerrules;
	
	@TableField(value = "tblAttachments")
	@Schema(name="附件",hidden=true)
	@IgnoreSwaggerParameter
	private Set tblAttachments;
	
	@TableField(value = "tblOuterrules")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set tblOuterrules;
	
	@TableField(value = "tblTracingresponsibilities")
	@Schema(name="整改追踪",hidden=true)
	@IgnoreSwaggerParameter
	private Set tblTracingresponsibilities;
	
	@TableField(value = "tblReforms")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set tblReforms;
	
	@TableField(value = "children")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set<TblNbsjBugEntity> children;
	
	@TableField(value = "tblproblemTargets")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set<TblAssessTarget> tblproblemTargets;
	
	@TableField(value = "businessDescription")
	@Schema(name = "业务描述")
	private String businessDescription;
	
	@TableField(value = "relatedProject")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblNbsjProject relatedProject;
	
	@TableField(value = "")
	@Schema(hidden=true)
	private String bugcrilevel;
	
	 @TableField("BUSINESSTYPE")
	 @Schema(name = "业务单元")
	 private String businessType;
	 
	@TableField(value = "")
	@Schema
	private Integer bugcriid;
	
	@TableField(value = "")
	@Schema(hidden=true)
	private String orgname;
	
	@TableField(value = "RESONFORNOREFORM")
	@Schema
	private String resonfornoreform;
	
	
}
