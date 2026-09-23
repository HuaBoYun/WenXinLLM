package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TblYyOrgDeposit entity. @author MyEclipse Persistence Tools
 * 用户购买望远镜存款表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_ORG_DEPOSIT")
@Schema(name="TblYyOrgDeposit对象", description="")
public class TblYyOrgDeposit implements java.io.Serializable {

	private static final long serialVersionUID = -7428773810836250078L;

	@TableId(value="DEPOSITID",type = IdType.INPUT)
	private BigDecimal depositid; //存款信息主键

	private TblOrganization tblOrganization; //隶属组织
	@TableField("TOTALMONEY")
	private Double totalmoney; //总金额
	@TableField("TOTALPAYMONEY")
	private Double totalpaymoney; // 总花费金额
	@TableField("LASTDATE")
	private Date lastdate; //最后一次充值到账时间


	public TblYyOrgDeposit() {
	}

	public TblYyOrgDeposit(TblOrganization tblOrganization, Double totalmoney,
                           Double totalpaymoney, Date lastdate) {
		this.tblOrganization = tblOrganization;
		this.totalmoney = totalmoney;
		this.totalpaymoney = totalpaymoney;
		this.lastdate = lastdate;
	}

	public BigDecimal getDepositid() {
		return this.depositid;
	}

	public void setDepositid(BigDecimal depositid) {
		this.depositid = depositid;
	}

	public TblOrganization getTblOrganization() {
		return this.tblOrganization;
	}

	public void setTblOrganization(TblOrganization tblOrganization) {
		this.tblOrganization = tblOrganization;
	}

	public Double getTotalmoney() {
		if(this.totalmoney == null){
			return 0.0;
		}
		return this.totalmoney;
	}

	public void setTotalmoney(Double totalmoney) {
		this.totalmoney = totalmoney;
	}

	public Double getTotalpaymoney() {
		if(this.totalpaymoney == null){
			return 0.0;
		}
		return this.totalpaymoney;
	}

	public void setTotalpaymoney(Double totalpaymoney) {
		this.totalpaymoney = totalpaymoney;
	}

	public Date getLastdate() {
		return lastdate;
	}

	public void setLastdate(Date lastdate) {
		this.lastdate = lastdate;
	}
}