package com.huabo.monitor.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * TblFlowUserRigthId entity. @author MyEclipse Persistence Tools
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="TblFlowUserRigthId")
public class TblFlowUserRigthId implements java.io.Serializable {

	// Fields

	private BigDecimal flowid;
	private BigDecimal staffid;

	// Constructors

	/** default constructor */
	public TblFlowUserRigthId() {
	}

	/** full constructor */
	public TblFlowUserRigthId(BigDecimal flowid, BigDecimal staffid) {
		this.flowid = flowid;
		this.staffid = staffid;
	}

	// Property accessors

	public BigDecimal getFlowid() {
		return this.flowid;
	}

	public void setFlowid(BigDecimal flowid) {
		this.flowid = flowid;
	}

	public BigDecimal getStaffid() {
		return this.staffid;
	}

	public void setStaffid(BigDecimal staffid) {
		this.staffid = staffid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblFlowUserRigthId))
			return false;
		TblFlowUserRigthId castOther = (TblFlowUserRigthId) other;

		return ((this.getFlowid() == castOther.getFlowid()) || (this
				.getFlowid() != null && castOther.getFlowid() != null && this
				.getFlowid().equals(castOther.getFlowid())))
				&& ((this.getStaffid() == castOther.getStaffid()) || (this
						.getStaffid() != null && castOther.getStaffid() != null && this
						.getStaffid().equals(castOther.getStaffid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getFlowid() == null ? 0 : this.getFlowid().hashCode());
		result = 37 * result
				+ (getStaffid() == null ? 0 : this.getStaffid().hashCode());
		return result;
	}

}