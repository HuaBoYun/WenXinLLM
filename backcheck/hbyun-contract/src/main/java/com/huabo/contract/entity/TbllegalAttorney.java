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

/**
 * 代理律师
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_LEGAL_ATTORNEY")
@Schema(name="代理律师实体类")
public class TbllegalAttorney implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@TableId(value = "ID" , type = IdType.INPUT)
    private BigDecimal id;
	
	@TableField("ISATTORNEY")//是否外聘律师：1是，2否
    private BigDecimal isattorney;
	
	@Schema(name = "代理人ID")
	@TableField("ATTORNEYSTAFFID")
	private BigDecimal attorneystaffid;
	
	@Schema(name = "代理人")
	@TableField("ATTORNEY")
	private String attorney;
	
	@Schema(name = "联系方式")
	@TableField("ATTORNEYPHONT")
	private String attorneyphont;
	
	@Schema(name = "纠纷ID")
	@TableField("DISPUTEID")
	private BigDecimal disputeid;

	@Schema(name = "协商ID")
	@TableField("NEGOTIATIONID")
	private BigDecimal negotiationid;

	@Schema(name = "诉讼ID")
	@TableField("LAWSUITID")
	private BigDecimal lawsuitid;

	@Schema(name = "律师事务所")
	@TableField("LAWFIRM")
	private String lawFirm;

	@Schema(name = "仲裁ID")
	@TableField("ARBITRATIONID")
	private BigDecimal arbitrationid;
	
	@Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("CREATETIME")
    private Date createtime;
	
	@Schema(name = "创建人")
	@TableField("CREATESTAFFID")
	private BigDecimal createstaffid;
	
}
