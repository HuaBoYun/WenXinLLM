package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * TblYyUserOrder entity. @author MyEclipse Persistence Tools
 * 用户下单表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_USER_ORDER")
@Schema(name="TblYyUserOrder对象", description="")
public class TblYyUserOrder implements java.io.Serializable {

	private static final long serialVersionUID = 6836682288847775081L;
	// Fields
	@TableId(value="ORDERID",type = IdType.INPUT)
	private BigDecimal orderid; //订单主键ID
	private TblStaff tblStaff;  //下单用户
	private TblOrganization tblOrganization; // 隶属组织
	@TableField("ORDERCODE")
	private String ordercode;  // 订单随机码
	@TableField("ORDERNO")
	private String orderno;    // 订单编号
	@TableField("STATUS")
	private Integer status;    // 订单状态  1. 已创建，未付款   2. 已付款  3. 已到账，存入数据库  4.已完成，
	@TableField("ORDERMONEY")
	private Double ordermoney; // 交易金额
	@TableField("PAYDATE")
	private Date paydate;      // 支付到账时间
	@TableField("CREATEDATE")
	private Date createdate;   // 创建时间

	@TableField("ORGID")
	private BigDecimal orgid;
	@TableField("STAFFID")
	private BigDecimal staffid;
	private String staffName;



}