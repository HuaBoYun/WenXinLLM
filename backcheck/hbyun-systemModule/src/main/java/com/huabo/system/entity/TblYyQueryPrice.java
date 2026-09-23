package com.huabo.system.entity;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * TblYyQueryPrice entity. @author MyEclipse Persistence Tools
 * 用户查询记录 与 接口中间关系表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_QUERY_PRICE")
@Schema(name="TblYyQueryPrice对象", description="")
public class TblYyQueryPrice implements java.io.Serializable {


	private static final long serialVersionUID = 4429474155929596869L;
	@Transient
	private TblYyQueryPriceId id;
	@Transient
	private Tblyyprice tblYyPrice;
	@Transient
	private TblYyUserQuery tblYyUserQuery;

}