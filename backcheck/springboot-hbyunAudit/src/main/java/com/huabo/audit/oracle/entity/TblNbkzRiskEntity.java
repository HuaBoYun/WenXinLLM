package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_RISK")
@Data
@Schema(name="风险发现实体类")
@Accessors(chain = true)
public class TblNbkzRiskEntity {
	
//	private static final long serialVersionUID = 1L;
	
	@TableId(value = "riskid", type= IdType.INPUT)
	@Schema(name = "主键")
	private BigDecimal riskid;
	
	@TableField(value = "riskname")
	@Schema(name = "风险名称")
	private String riskname;
	
	@TableField(value = "risknumber")
	@Schema(name = "风险编号")
	private String risknumber;
	
	@TableField(value = "riskdes")
	@Schema(name = "风险描述")
	private String riskdes;
	
	@TableField(value = "occureddate")
	@Schema(name = "发现日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private Date occureddate; 
	
	@TableField(value = "discovereddate")
	@Schema(name = "发生日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private Date discovereddate;
	
	@TableField(value = "losseventcategory")
	@Schema
	private String losseventcategory;
	
	@TableField(value = "riskeventdescription")
	@Schema
	private String riskeventdescription;
	
	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblOrganization tblOrganiDem;
	
	@TableField(exist = false)
	@Schema(name="关联附件集合",hidden=true)
	@IgnoreSwaggerParameter
	private Set tblAttachments;
	
 
	@TableField(value = "createDate")
	@Schema(name="创建时间",hidden=true)
	private Date createDate;

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff tblStaff;
	
	@TableField(value = "stype")
	@Schema
	private String stype;
	
	@TableField(value = "sysOrgid")
	@Schema(name = "相关部门ids")
	private String sysOrgid;
	
	@TableField(exist = false)
	@Schema(hidden=true)
	private String sysOrgName;
	
	@TableField(value = "createorid")
	@Schema(name = "发现人id")
	private String createorid;
	
	@TableField(value = "ORGID")
	@Schema(name = "责任部门id")
	private BigDecimal orgid;
	
	
}
