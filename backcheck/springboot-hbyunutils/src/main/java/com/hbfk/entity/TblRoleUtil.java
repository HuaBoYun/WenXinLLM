package com.hbfk.entity;

import java.math.BigDecimal;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 角色表管理
rid:主键ID,自动增长；
rname:角色名称；
rdesc:角色描述；
rstatus:角色状态，是否启用
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
@Data
public class TblRoleUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String DEL_YES = "1";
	public static final String DEL_NO = "0";
	
	private BigDecimal rid;
	
	private String rname;
	
	private String rdesc;
	
	private String rstatus;
	
	private BigDecimal companyid;

}
