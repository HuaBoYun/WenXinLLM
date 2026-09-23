package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_ARCHIVE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjArchiveEntity {

//	private static final long serialVersionUID = 1L;
	
	
	@TableId(value = "outrulid", type= IdType.INPUT)
	@Schema
	private BigDecimal arcid;
	
	@TableField(value = "level")
	@Schema
	private Integer level;
	
	@TableField(exist = false)
	@Schema
	private TblStaff objTblStaff;
	
	@TableField(value = "acrtime")
	@Schema
	private Date acrtime;
	
	@TableField(value = "menucode")
	@Schema
	private String menucode;

	@TableField(exist = false)
	@Schema
	private TblNbsjProject objTblnbsjProject;
	
	@TableField(value = "endtime")
	@Schema
	private Date endtime;
	
	@TableField(value = "price")
	@Schema
	private Integer price;
	
	
	@TableField(value = "WORKTIME")
	@Schema
	private Integer worktime;
}
