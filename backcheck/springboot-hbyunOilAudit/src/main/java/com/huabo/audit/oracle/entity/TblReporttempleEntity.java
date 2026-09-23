package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_REPORTTEMPLE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblReporttempleEntity {

	@TableId(value = "outrulid", type= IdType.AUTO)
	@Schema
	private BigDecimal reporttempid;
	
	@TableField(value = "rulename")
	@Schema
	private String templenumber;
	
	@TableField(value = "rulename")
	@Schema
	private String templename;
	
	@TableField(value = "rulename")
	@Schema
	private Date createdtime;
	
	@TableField(value = "rulename")
	@Schema
	private String creater;
	
	@TableField(value = "rulename")
	@Schema
	private String createddepartment;
	
	@TableField(value = "rulename")
	@Schema
	private Set tblReports;
	
}
