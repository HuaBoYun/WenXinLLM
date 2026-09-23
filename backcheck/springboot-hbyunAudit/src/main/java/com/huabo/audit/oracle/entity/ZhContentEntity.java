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

@TableName("TBL_ZH_CONTENT")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class ZhContentEntity {

	@TableId(value = "contentid", type= IdType.INPUT)
	@Schema
	private BigDecimal contentid;
	
	@TableField(value = "numbercode")
	@Schema
	private Integer numbercode;
	
	@TableField(value = "content")
	@Schema
	private String content;
	
	@TableField(value = "type")
	@Schema
	private Integer type;
	
}
