package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统首页授权表
 */
@Schema(name = "TblSystemHomeAuthorizationOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_SYSTEM_HOME_AUTHORIZATION")
public class TblSystemHomeAuthorizationOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@Schema(name="主键ID")
	@TableId(value = "ID" , type = IdType.INPUT)
	private BigDecimal id;

	/**
	 * 系统首页配置ID
	 */
	@TableField(value = "HOMEPAGEID")
	@Schema(name="系统首页配置ID")
	private BigDecimal homePageId;

	/**
	 * 所属集团ID
	 */
	@TableField(value = "BELONGGROUP")
	@Schema(name="所属集团ID")
	private BigDecimal belongGroup;

	private static final long serialVersionUID = 1L;

	public static TblSystemHomeAuthorizationOracle ofId(BigDecimal id) {
		TblSystemHomeAuthorizationOracle tblSystemHomeAuthorizationOracle = new TblSystemHomeAuthorizationOracle();
		tblSystemHomeAuthorizationOracle.setId(id);
		return tblSystemHomeAuthorizationOracle;
	}

	public static TblSystemHomeAuthorizationOracle ofHomePageId(BigDecimal homePageId) {
		TblSystemHomeAuthorizationOracle homeAuthorizationOracle = new TblSystemHomeAuthorizationOracle();
		homeAuthorizationOracle.setHomePageId(homePageId);
		return homeAuthorizationOracle;
	}
}