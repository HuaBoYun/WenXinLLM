package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AUTONO_INFO")
@Schema(name="TblAutonoInfo")
public class TblAutonoInfo implements Serializable {
	private static final long serialVersionUID = -1674121086567254504L;
	@TableId(value="NOID",type = IdType.INPUT)
	private BigDecimal noid;//主键Id
	@TableField("NONAME")
	private String noname;//自动编号的名称
	@TableField("NODEFAULTCODE")
	private String nodefaultcode;//自动编号前面的编码
	@TableField("NODEFAULTSEPARTOR")
	private String nodefaultSepartor;//自动编号中间的默认分隔符
	@TableField("NODEFAULTNUMBER")
	private Integer nodefaultNumber;//自动编号后面的类型 1代表 01 ， 2 代表 0.1，
	//private Set<TblAutonoInfo> tblAutonoInfos = new HashSet<TblAutonoInfo>(0);
	//private Set<TblOrgNo> tblOrgNos = new HashSet<TblOrgNo>(0);
	@TableField("PARENTID")
	private BigDecimal patentid;

	public TblAutonoInfo() {
	}

	public TblAutonoInfo(TblAutonoInfo tblAutonoInfo, String noname,
                         String nodefaultcode,
                         String nodefaultSepartor, Integer nodefaultNumber) {
		this.noname = noname;
		this.nodefaultcode = nodefaultcode;
		//this.tblAutonoInfos = tblAutonoInfos;
		//this.tblOrgNos = tblOrgNos;
		this.nodefaultSepartor = nodefaultSepartor;
		this.nodefaultNumber = nodefaultNumber;
	}

	public BigDecimal getNoid() {
		return this.noid;
	}

	public void setNoid(BigDecimal noid) {
		this.noid = noid;
	}


	public String getNoname() {
		return this.noname;
	}

	public void setNoname(String noname) {
		this.noname = noname;
	}

	public String getNodefaultcode() {
		return this.nodefaultcode;
	}

	public void setNodefaultcode(String nodefaultcode) {
		this.nodefaultcode = nodefaultcode;
	}

	 

	public String getNodefaultSepartor() {
		return nodefaultSepartor;
	}

	public void setNodefaultSepartor(String nodefaultSepartor) {
		this.nodefaultSepartor = nodefaultSepartor;
	}

	public Integer getNodefaultNumber() {
		return nodefaultNumber;
	}

	public void setNodefaultNumber(Integer nodefaultNumber) {
		this.nodefaultNumber = nodefaultNumber;
	}

	public BigDecimal getPatentid() {
		return patentid;
	}

	public void setPatentid(BigDecimal patentid) {
		this.patentid = patentid;
	}

//	public Set<TblAutonoInfo> getTblAutonoInfos() {
//		return tblAutonoInfos;
//	}
//
//	public void setTblAutonoInfos(Set<TblAutonoInfo> tblAutonoInfos) {
//		this.tblAutonoInfos = tblAutonoInfos;
//	}

//	public Set<TblOrgNo> getTblOrgNos() {
//		return tblOrgNos;
//	}
//
//	public void setTblOrgNos(Set<TblOrgNo> tblOrgNos) {
//		this.tblOrgNos = tblOrgNos;
//	}
	
}