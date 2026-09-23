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

@TableName("TBL_NBSJ_WORKREPORT")
@Data
@Schema(name="实体类")
public class TblNbsjWorkReportEntity {
	
//	private static final long serialVersionUID = -1470174547811550823L;
	
	@TableId(value = "REPORTID", type= IdType.AUTO)
	@Schema
	private BigDecimal reportid;
	
	@TableField(value = "tblReporttemple")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblReporttempleEntity tblReporttemple;
	
	@TableField(value = "REPORTNAME")
	@Schema
	private String reportname;
	
	@TableField(value = "REPORTTIME")
	@Schema
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date reporttime;
	
	@TableField(value = "REPORTTYPE")
	@Schema
	private String reporttype;
	
	@TableField(value = "REPORTMODE")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private String reportmode;
	
	@TableField(value = "")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff reporter;
	
	@TableField(value = "REPORTDEPARTMENT")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblOrganization reportdepartment;
	
	@TableField(value = "reportstatus")
	@Schema
	private String reportstatus;
	
	@TableField(value = "reportfile")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private String reportfile;
	
	@TableField(value = "memo")
	@Schema
	private String memo;
	
	@TableField(value = "itemtype")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private String itemtype;
	
	@TableField(value = "repdesc")
	@Schema
	private String repdesc;
	
	@TableField(value = "projectId")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblNbsjProject projectId;
	
	@TableField(value = "tblOrganization")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblOrganization tblOrganization;
	
	@TableField(value = "fhStaff")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff fhStaff;
	
	@TableField(value = "zqyjStaff")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff zqyjStaff;
	
	@TableField(value = "yjdes")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private String yjdes;
	
	@TableField(value = "tblAttachments")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set<TblAttachment> tblAttachments;
	
	
	@TableField(value = "reportnamestatus")
	@Schema
	private String reportnamestatus;
	
	@TableField(value = "reporttypestatus")
	@Schema
	private String reporttypestatus;
	
	@TableField(value = "reportmodestatus")
	@Schema
	private String reportmodestatus;
	
	@TableField(value = "reporttimestatus")
	@Schema
	private String reporttimestatus;
	
	@TableField(value = "REPORTER")
	@Schema
	private Integer reporterid;
	
	@TableField(value = "REPORTDEPARTMENT")
	@Schema
	private Integer reportdepartmentid;
	
	@TableField(value = "PROJECTID")
	@Schema(hidden=true)
	private Integer _projectid;

}
