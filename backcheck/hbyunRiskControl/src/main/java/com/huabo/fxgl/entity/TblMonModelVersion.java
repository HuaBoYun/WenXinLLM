package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

 
@Data
@TableName("TBL_MONMODEL_VERSION")
@Schema(name="风险监督模板-版本表")
public class TblMonModelVersion  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Schema(name = "主键")
	@TableId(value = "ID")
	@Id
	private BigDecimal id;

	@Schema(name = "关联公司")
	@TableField(value = "ORGID")
	private BigDecimal orgid;

	@Schema(name = "创建时间")
	@TableField(value = "CREATETIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createtime;

	@Schema(name = "部门关联模板版本")
	@TableField(value = "VERSION")
	private Integer version;

	@Schema(name = "创建人")
	@TableField(value = "CREATEID")
	private BigDecimal createid;
     
   
}
     

