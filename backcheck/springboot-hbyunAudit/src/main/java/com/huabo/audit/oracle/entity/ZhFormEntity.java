package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_ZH_FORM")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class ZhFormEntity {
	
	@TableId(value = "formid", type= IdType.INPUT)
	@Schema
	private BigDecimal formid;
	
	@TableField(value = "formName")
	@Schema
	private String formName;

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff selfStaff;

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff otherStaff;
	
	@TableField(value = "selfDate")
	@Schema
	private Date selfDate;
	
	@TableField(value = "otherDate")
	@Schema
	private Date otherDate;
	
	@TableField(value = "status")
	@Schema
	private Integer status;
	
	@TableField(value = "formType")
	@Schema
	private Integer formType;

	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblNbsjProject project;
	
//	public ZhFormEntity(BigDecimal formid, String formName, TblStaff selfStaff, TblStaff otherStaff, Date selfDate,
//			Date otherDate, Integer status, Integer formType, TblNbsjProject project) {
//		super();
//		this.formid = formid;
//		this.formName = formName;
//		this.selfStaff = selfStaff;
//		this.otherStaff = otherStaff;
//		this.selfDate = selfDate;
//		this.otherDate = otherDate;
//		this.status = status;
//		this.formType = formType;
//		this.project = project;
//	}

}
