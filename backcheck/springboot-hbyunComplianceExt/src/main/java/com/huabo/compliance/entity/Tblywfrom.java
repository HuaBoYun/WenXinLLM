package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YW_FROM")
@Schema(name="Tblywfrom对象")
public class Tblywfrom implements Serializable {


	private static final long serialVersionUID = 1L;

	@TableId("FROMID")
	private BigDecimal fromid;
	@TableField("FROMNUMBER")
	private String fromnumber;
	@TableField("FROMNAME")
	private String fromname;
	@TableField("MEO")
	private String meo;
	@TableField("ORGID")
	private  BigDecimal orgid;

	
	
}
