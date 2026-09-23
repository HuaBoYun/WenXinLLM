package com.huabo.system.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="TblOrgNoId")
public class TblOrgNoId implements java.io.Serializable {

	private static final long serialVersionUID = 5992771239847223876L;
	@TableId("ORGID")
	@Schema(name="所属公司主键")
	private BigDecimal orgid;
	@TableId("NOID")
	@Schema(name="编号主键")
	private BigDecimal noid;
	@TableField("NOCODE")
	@Schema(name="编号编码")
	private String nocode;
	@TableField("ISUSEDEFAULT")
	@Schema(name="是否使用默认生成")
	private Integer isusedefault;
	@TableField("NOSEPARTOR")
	@Schema(name="分割符")
	private String noSepartor;
	@TableField("NONUMBER")
	@Schema(name="编号后缀 数字")
	private Integer noNumber;
	@TableField("NOSUFFIX")
	@Schema(name="编号前缀 规则")
	private Integer noSuffix;
	
	public TblOrgNoId() {
	}

	public TblOrgNoId(BigDecimal orgid, BigDecimal noid, String nocode,
                      Integer isusedefault, String noSepartor, Integer noNumber) {
		this.orgid = orgid;
		this.noid = noid;
		this.nocode = nocode;
		this.isusedefault = isusedefault;
		this.noSepartor = noSepartor;
		this.noNumber = noNumber;
	}

	public BigDecimal getOrgid() {
		return this.orgid;
	}

	public void setOrgid(BigDecimal orgid) {
		this.orgid = orgid;
	}

	public BigDecimal getNoid() {
		return this.noid;
	}

	public void setNoid(BigDecimal noid) {
		this.noid = noid;
	}

	public String getNocode() {
		return this.nocode;
	}

	public void setNocode(String nocode) {
		this.nocode = nocode;
	}

	public Integer getIsusedefault() {
		return this.isusedefault;
	}

	public void setIsusedefault(Integer isusedefault) {
		this.isusedefault = isusedefault;
	}

	public String getNoSepartor() {
		return noSepartor;
	}

	public void setNoSepartor(String noSepartor) {
		this.noSepartor = noSepartor;
	}

	public Integer getNoNumber() {
		return noNumber;
	}

	public void setNoNumber(Integer noNumber) {
		this.noNumber = noNumber;
	}
	

	public Integer getNoSuffix() {
		return noSuffix;
	}

	public void setNoSuffix(Integer noSuffix) {
		this.noSuffix = noSuffix;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblOrgNoId))
			return false;
		TblOrgNoId castOther = (TblOrgNoId) other;

		return ((this.getOrgid() == castOther.getOrgid()) || (this.getOrgid() != null
				&& castOther.getOrgid() != null && this.getOrgid().equals(
				castOther.getOrgid())))
				&& ((this.getNoid() == castOther.getNoid()) || (this.getNoid() != null
						&& castOther.getNoid() != null && this.getNoid()
						.equals(castOther.getNoid())))
				&& ((this.getNocode() == castOther.getNocode()) || (this
						.getNocode() != null && castOther.getNocode() != null && this
						.getNocode().equals(castOther.getNocode())))
				&& ((this.getIsusedefault() == castOther.getIsusedefault()) || (this
						.getIsusedefault() != null
						&& castOther.getIsusedefault() != null && this
						.getIsusedefault().equals(castOther.getIsusedefault())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrgid() == null ? 0 : this.getOrgid().hashCode());
		result = 37 * result
				+ (getNoid() == null ? 0 : this.getNoid().hashCode());
		result = 37 * result
				+ (getNocode() == null ? 0 : this.getNocode().hashCode());
		result = 37
				* result
				+ (getIsusedefault() == null ? 0 : this.getIsusedefault()
						.hashCode());
		return result;
	}

}