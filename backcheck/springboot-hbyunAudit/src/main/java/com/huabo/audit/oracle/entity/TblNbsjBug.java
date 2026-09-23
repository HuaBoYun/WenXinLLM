package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.vo.param.fieldActivationVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Table(name = "TBL_NBSJ_BUG")
@Data
@Schema(name="缺陷实体类")
@Accessors(chain = true)
public class TblNbsjBug extends FlexibleFieldEntity {
	

	@Schema(name = "主键")
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@TableId("bugid")
	@TableField("bugid")
	@Column(name = "bugid")
	private BigDecimal bugid;
	
	@TableField(value = "bugnumber")
	@Column(name = "bugnumber")
	@Schema(name = "缺陷编号")
	private String bugnumber;
	
	@TableField(value = "bugdescripte")
	@Column(name = "bugdescripte")
	@Schema(name = "缺陷描述")
	private String bugdescripte;
	
	@TableField(value = "discovertime")
	@Column(name = "discovertime")
	@Schema(name = "发现日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date discovertime;
	
	@TableField(value = "discoverperson")
	@Column(name = "discoverperson")
	@Schema(name = "发现人")
	private String discoverperson;
	
	@TableField(value = "BUGPROPERTY")
	@Column(name = "BUGPROPERTY")
	@Schema(name = "缺陷性质")
	private String bugproperty;
	
	@TableField(value = "bugsource")
	@Column(name = "bugsource")
	@Schema(name = "是否财务相关")
	private String bugsource;
	
	@TableField(value = "bugdepartment")
	@Column(name = "bugdepartment")
	@Schema(name = "缺陷部门|创建公司")
	private String bugdepartment;
	
	@TableField(value = "needreform")
	@Column(name = "needreform")
	@Schema(name = "是否需要整改")
	private String needreform;
	
	@TableField(value = "bugreformstatus")
	@Column(name = "bugreformstatus")
	@Schema
	private String bugreformstatus;
	
	@TableField(value = "projectname")
	@Column(name = "projectname")
	@Schema
	private String projectname;
	
	@TableField(value = "memo")
	@Column(name = "memo")
	@Schema
	private String memo;
	
	@TableField(value = "PROJECTID")
	@Column(name = "PROJECTID")
	@Schema(name = "关联项目Id")
	private BigDecimal projectId;
	
	@TableField(value = "fatherbugid")
	@Column(name = "fatherbugid")
	@Schema
	private BigDecimal fatherbugid;
	
	@TableField(value = "bugbysystem")
	@Column(name = "bugbysystem")
	@Schema
	private String bugbysystem;
	
	@Transient
	@TableField(exist = false)
	@Column(name = "tblInnerrules")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set tblInnerrules;
	
	@Transient
	@TableField(exist = false)
	@Column(name = "tblAttachments")
	@Schema(name="附件",hidden=true)
	@IgnoreSwaggerParameter
	private Set tblAttachments;
	
	@Transient
	@TableField(exist = false)
	@Column(name = "tblOuterrules")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set tblOuterrules;
	
	@Transient
	@TableField(exist = false)
	@Schema(name="整改追踪",hidden=true)
	@IgnoreSwaggerParameter
	private Set tblTracingresponsibilities;
	
	@Transient
	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set tblReforms;
	
	@Transient
	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set<TblNbsjBug> children;
	
	@Transient
	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set<TblAssessTarget> tblproblemTargets;
	
	@TableField(value = "businessDescription")
	@Column(name = "businessDescription")
	@Schema(name = "业务描述")
	private String businessDescription;
	
	@Transient
	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblNbsjProject relatedProject;
	
	@Transient
	@TableField(exist = false)
	@Schema(hidden=true)
	private String bugcrilevel;
	
	 @TableField("BUSINESSTYPE")
	 @Column(name = "BUSINESSTYPE")
	 @Schema(name = "业务单元")
	 private String businessType;
	@Transient
	@TableField(exist = false)
	@Schema
	private BigDecimal bugcriid;
	@Transient
	@TableField(exist = false)
	@Schema(hidden=true)
	private String orgname;
	
	@TableField(value = "RESONFORNOREFORM")
	@Column(name = "RESONFORNOREFORM")
	@Schema
	private String resonfornoreform;
	
	
	//------中核新增
	 @Schema(name="缺陷名称")
	    @TableField("DEFECTSNAME")
	    private String defectsname;
	
	 @Schema(name="涉及金额(万元)")
	    @TableField("AMOUNT")
	    private BigDecimal amount ;
	    
	    @Schema(name="原因分析")
	    @TableField("CAUSEANALYSIS")
	    private String causeanalysis;
	    
	    @Schema(name="缺陷类别")
	    @TableField("DEFECTCATEGORY")
	    private String defectcategory ;
	    
	    @Schema(name="缺陷种类")
	    @TableField("DEFECTTYPE")
	    private String defecttype ;
		
	    @Schema(name="是否涉诉")
	    @TableField("LITIGATION")
	    private String litigation ;
	    
	    @Schema(name="是否境外")
	    @TableField("OVERSEAS")
	    private String overseas ;
	    
	    @Schema(name="创建人")
	    @TableField("CREATESTAFFID")
	    private BigDecimal createstaffid;
	    
	    @TableField("CREATETIME")
	 	@Schema(name = "创建时间")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	   	@DateTimeFormat(pattern = "yyyy-MM-dd")
	     private Date createtime;

	    @Schema(name="关联公司")
	    @TableField("UNIT")
	    private BigDecimal unit;
	    
	    @Schema(name="审批状态")
	    @TableField("STATUS")
	    private Integer status;
	 
	    @Schema(name = "密级主键")
	    @TableField("SECRECTLEVELID")
	    @Column(name = "SECRECTLEVELID")
	    private BigDecimal secrectLevelId;
	    
	    @Schema(name = "知悉范围 多个逗号分隔")
	    @TableField("STAFFSCOPEIDS")
	    @Column(name = "STAFFSCOPEIDS")
	    private String staffScopeIds;
	    
	    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
	    @TableField("STAFFSCOPENAMES")
	    @Column(name = "STAFFSCOPENAMES")
	    private String staffScopeNames;
	    @Schema(name = "所属部门")
	    @TableField("LINKDEPTID")
	    @Column(name = "LINKDEPTID")
	  private BigDecimal linkdeptid;
	     
	    
}
