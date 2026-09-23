package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName("TBL_NBSJ_STAT_TYPE")
@Data
@Schema(name="实体类")
public class TblNbsjStatType {
	@TableId(value = "TYPEID", type= IdType.AUTO)
	@Schema
	private Integer typeId;
	
	@TableField(value = "AUDITTYPE")
	@Schema
	private String auditType;
	
	@TableField(value = "STATUS")
	@Schema
	private Integer status;
	
	@TableField(value = "VERSION")
	@Schema
	private Integer version;
	
	@TableField(value = "ORGID")
	@Schema
	private Integer orgid;
}
