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

@TableName("TBL_NBSJ_RISK")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbkzRiskEntity {
	
//	private static final long serialVersionUID = 1L;
	
	@TableId(value = "riskid", type= IdType.AUTO)
	@Schema
	private BigDecimal riskid;
	
	@TableField(value = "riskname")
	@Schema
	private String riskname;
	
	@TableField(value = "risknumber")
	@Schema
	private String risknumber;
	
	@TableField(value = "riskdes")
	@Schema
	private String riskdes;
	
	@TableField(value = "occureddate")
	@Schema
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date occureddate;
	
	@TableField(value = "discovereddate")
	@Schema
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date discovereddate;
	
	@TableField(value = "losseventcategory")
	@Schema
	private String losseventcategory;
	
	@TableField(value = "riskeventdescription")
	@Schema
	private String riskeventdescription;
	
	@TableField(value = "tblOrganiDem")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblOrganization tblOrganiDem;
	
	@TableField(value = "tblAttachments")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set tblAttachments;
	
	@TableField(value = "tblproblemTargets")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set<TblAssessTarget> tblproblemTargets;
	
	@TableField(value = "createDate")
	@Schema(hidden=true)
	private Date createDate;
	
	@TableField(value = "tblStaff")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff tblStaff;
	
	@TableField(value = "stype")
	@Schema
	private String stype;
	
	@TableField(value = "sysOrgid")
	@Schema(name = "相关部门ids")
	private String sysOrgid;
	
	@TableField(value = "sysOrgName")
	@Schema(hidden=true)
	private String sysOrgName;
	
	@TableField(value = "createorid")
	@Schema(name = "发现人id")
	private String createorid;
	
	@TableField(value = "ORGID")
	@Schema(name = "责任部门id")
	private Integer orgid;
	
	
}
