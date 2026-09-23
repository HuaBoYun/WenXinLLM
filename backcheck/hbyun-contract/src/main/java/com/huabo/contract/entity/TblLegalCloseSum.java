package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_LEGAL_CLOSESUM")
@Schema(name="结案总结实体类")
public class TblLegalCloseSum implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId(value = "ID" ,type = IdType.INPUT)
    private BigDecimal id;
	
	@Schema(name = "案件简介")
	@TableField("CASEMEMO")
	private String casememo;
	
	@Schema(name = "判决结果")
	@TableField("SENTENRES")
	private String sentenres;
	
	@Schema(name = "经验总结")
	@TableField("EXPERSUM")
	private String expersum;
	
	//=====
	
	@Schema(name = "诉讼过程")
	@TableField("LITIGATIONID")
	private BigDecimal litigationid;
	
	@Schema(name = "仲裁过程")
	@TableField("ARBITRAID")
	private BigDecimal arbitraid;
	
	@Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("CREATETIME")
    private Date createtime;
	
	@Schema(name = "创建人")
	@TableField("CREATESTAFFID")
	private BigDecimal createstaffid;
}
