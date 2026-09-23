package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;

/**
 * TblFlowUserRigth entity. @author MyEclipse Persistence Tools
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FLOW_USER_RIGTH")
@Schema(name="TblFlowUserRigth")
public class TblFlowUserRigth implements java.io.Serializable {

	// Fields
	@Transient
	private TblFlowUserRigthId id;
	@Transient
	private TblStaff tblStaff;
	@Transient
	private TblFlow tblFlow;

	// Constructors

	/** default constructor */
	public TblFlowUserRigth() {
	}

	/** minimal constructor */
	public TblFlowUserRigth(TblFlowUserRigthId id) {
		this.id = id;
	}

	/** full constructor */
	public TblFlowUserRigth(TblFlowUserRigthId id, TblStaff tblStaff,
                            TblFlow tblFlow) {
		this.id = id;
		this.tblStaff = tblStaff;
		this.tblFlow = tblFlow;
	}

	// Property accessors

	public TblFlowUserRigthId getId() {
		return this.id;
	}

	public void setId(TblFlowUserRigthId id) {
		this.id = id;
	}

	public TblStaff getTblStaff() {
		return this.tblStaff;
	}

	public void setTblStaff(TblStaff tblStaff) {
		this.tblStaff = tblStaff;
	}

	public TblFlow getTblFlow() {
		return this.tblFlow;
	}

	public void setTblFlow(TblFlow tblFlow) {
		this.tblFlow = tblFlow;
	}

}