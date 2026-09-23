package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TblAcquisitionRecord")
@Schema(name="数据采集执行采集记录")
public class TblAcquisitionRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Schema(name= "记录ID")
	@TableId(value ="RECORDID",type = IdType.INPUT)
	private String recordId;//记录ID
	@Schema(name= "采集年限")
    @TableField("RECORDYEAR")
	private String recordYear;//采集开始时间
	@Schema(name= "是否关联主合同")
    @TableField("RECORDSTART")
	private Date recordStart;//采集开始时间
	@Schema(name= "采集结束时间")
    @TableField("RECORDEND")
	private Date recordEnd;//采集结束时间
	@Schema(name= "是否关联主合同")
    @TableField("采集用户Id")
	private BigDecimal staffId;//采集用户
	@Schema(name= "采集组织")
    @TableField("ORGID")
	private BigDecimal orgId;//采集组织
	@Schema(name= "采集耗时")
    @TableField("RECORDTIME")
	private String recordTime;//采集耗时
	@Schema(name= "采集类型")
    @TableField("RETYPE")
	private Integer reType;//采集类型
	
	@Schema(name= "采集ip")
    @TableField("RECORDIP")
	private String recordIp;
	@Schema(name= "采集备注")
    @TableField("RECORDMEMO")
	private String recordMemo;
	
	@Transient
	@TableField("所属公司")
	private String orgName;
	@Transient
	@TableField("采集人真实姓名")
	private String staffName;
	
}
