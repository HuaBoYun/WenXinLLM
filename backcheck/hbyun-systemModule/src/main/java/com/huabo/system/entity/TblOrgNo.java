package com.huabo.system.entity;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@TableName("TBL_ORG_NO")
@Schema(name="TblOrgNo")
public class TblOrgNo implements java.io.Serializable {

	private static final long serialVersionUID = 7985409817430444480L;
	@TableField("ORGID")
	@Schema(name="所属公司主键")
	private BigDecimal orgid;
	@TableId(value="NOID",type = IdType.INPUT)
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name="主键")
	private BigDecimal noid;
	@TableField("NOCODE")
	@Schema(name="编号前缀")
	private String nocode;
	@TableField("NOSEPARTOR")
	@Schema(name="分割符")
	private String noSepartor;
	@TableField("NONUMBER")
	@Schema(name="编号后缀数字")
	private Integer noNumber;
	@TableField("ISUSEDEFAULT")
	@Schema(name="是否使用默认编号规则")
	private Integer isusedefault;
	@TableField("NOSUFFIX")
	private Integer noSuffix;
	@TableField("NEWNUMBER")
	private Integer newnumber;

	@Transient
	private String noname;

	@Transient
	private TblOrgNoId id;
	@Transient
	private TblOrganization tblOrganization;
	@Transient
	private TblAutonoInfo tblAutonoInfo;

	public TblOrgNo() {
	}

	public TblOrgNo(TblOrgNoId id) {
		this.id = id;
	}

	public TblOrgNo(TblOrgNoId id, TblOrganization tblOrganization,
                    TblAutonoInfo tblAutonoInfo) {
		this.id = id;
		this.tblOrganization = tblOrganization;
		this.tblAutonoInfo = tblAutonoInfo;
	}

}