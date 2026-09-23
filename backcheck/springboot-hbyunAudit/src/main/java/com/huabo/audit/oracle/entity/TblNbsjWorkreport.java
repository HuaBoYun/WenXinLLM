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

import javax.persistence.Column;
import javax.persistence.Id;

@TableName("TBL_NBSJ_WORKREPORT")
@Data
@Schema(name="实体类")
public class TblNbsjWorkreport {
	
//	private static final long serialVersionUID = -1470174547811550823L;

	@Id
	@TableId(value = "REPORTID", type= IdType.INPUT)
	@Schema(name = "主键")
	private BigDecimal reportid;

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblReporttempleEntity tblReporttemple;
	
	@TableField(value = "REPORTNAME")
	@Schema(name = "日志名称")
	private String reportname;
	
	@TableField(value = "REPORTTIME")
	@Schema(name = "日志时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date reporttime;
	
	@TableField(value = "REPORTTYPE")
	@Schema(name = "报告类型")
	private String reporttype;
	
	@TableField(value = "REPORTMODE")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private String reportmode;

	@TableField(exist = false)
	@Schema(name="报告人实体",hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff reporter;

	@TableField(exist = false)
	@Schema(name="报告部门实体",hidden=true)
	@IgnoreSwaggerParameter
	private TblOrganization reportdepartment;
	
	@TableField(value = "reportstatus")
	@Schema(name = "状态")
	private String reportstatus;
	
	@TableField(value = "reportfile")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private String reportfile;
	
	@TableField(value = "memo")
	@Schema(name = "备注")
	private String memo;
	
	@TableField(value = "itemtype")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private String itemtype;
	
	@TableField(value = "repdesc")
	@Schema
	private String repdesc;

	/*@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblNbsjProject projectId;*/

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblOrganization tblOrganization;

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff fhStaff;

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff zqyjStaff;
	
	@TableField(value = "yjdes")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private String yjdes;

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set<TblAttachment> tblAttachments;
	
	
//	@TableField(exist = false)
//	@Schema
//	private String reportnamestatus;
//
//	@TableField(exist = false)
//	@Schema
//	private String reporttypestatus;
//
//	@TableField(exist = false)
//	@Schema
//	private String reportmodestatus;
//
//	@TableField(exist = false)
//	@Schema
//	private String reporttimestatus;
	
	@TableField(value = "REPORTERID")
	@Schema
	private BigDecimal reporterid;
	
	@TableField(value = "REPORTDEPARTMENTID")
	@Schema
	private BigDecimal reportdepartmentid;
	
	@TableField(value = "PROJECTID")
	private BigDecimal projectid;
	
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




	@TableField("CREATESTAFFID")
	@Column(name = "CREATESTAFFID")
	private BigDecimal createstaffid;


}
