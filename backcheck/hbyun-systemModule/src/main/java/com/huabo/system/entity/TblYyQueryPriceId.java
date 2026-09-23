package com.huabo.system.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TblYyQueryPriceId entity. @author MyEclipse Persistence Tools
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_QUERY_PRICE")
@Schema(name="TblYyQueryPrice对象", description="")
public class TblYyQueryPriceId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4898647194982541226L;
	@TableField("PRICEID")
	@Schema(name="价格表主键")
	private BigDecimal priceid;
	@TableField("记录表主键")
	@Schema(name="修改人")
	private BigDecimal recordid;
	@TableField("PRICEMONEY")
	@Schema(name="花费金额")
	private Double priceMoney;

	// Constructors

	/** default constructor */
	public TblYyQueryPriceId() {
	}

	/** full constructor */
	public TblYyQueryPriceId(BigDecimal priceid, BigDecimal recordid, Double priceMoney) {
		this.priceid = priceid;
		this.recordid = recordid;
		this.priceMoney = priceMoney;
	}

	// Property accessors

	public BigDecimal getPriceid() {
		return this.priceid;
	}

	public void setPriceid(BigDecimal priceid) {
		this.priceid = priceid;
	}

	public BigDecimal getRecordid() {
		return this.recordid;
	}

	public void setRecordid(BigDecimal recordid) {
		this.recordid = recordid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblYyQueryPriceId))
			return false;
		TblYyQueryPriceId castOther = (TblYyQueryPriceId) other;

		return ((this.getPriceid() == castOther.getPriceid()) || (this
				.getPriceid() != null && castOther.getPriceid() != null && this
				.getPriceid().equals(castOther.getPriceid())))
				&& ((this.getRecordid() == castOther.getRecordid()) || (this
						.getRecordid() != null
						&& castOther.getRecordid() != null && this
						.getRecordid().equals(castOther.getRecordid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getPriceid() == null ? 0 : this.getPriceid().hashCode());
		result = 37 * result
				+ (getRecordid() == null ? 0 : this.getRecordid().hashCode());
		return result;
	}

	public Double getPriceMoney() {
		return priceMoney;
	}

	public void setPriceMoney(Double priceMoney) {
		this.priceMoney = priceMoney;
	}

}