package com.huabo.compliance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblOrgNoId implements java.io.Serializable {

	private static final long serialVersionUID = 5992771239847223876L;
	private BigDecimal orgid;
	private BigDecimal noid;
	private String nocode;
	private Integer isusedefault;
	private String noSepartor;
	private Integer noNumber;
	private Integer noSuffix;


}