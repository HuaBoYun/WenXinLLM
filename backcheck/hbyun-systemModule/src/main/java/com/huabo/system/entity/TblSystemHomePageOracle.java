package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统首页配置
 */
@Schema(name = "TblSystemHomePageOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_SYSTEM_HOME_PAGE")
public class TblSystemHomePageOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@TableId(value = "ID",type = IdType.INPUT)
	@Schema(name="主键ID")
	private BigDecimal id;

	/**
	 * 首页名称
	 */
	@TableField(value = "HOMENAME")
	@Schema(name="首页名称")
	private String homeName;

	/**
	 * 首页对应路径
	 */
	@TableField(value = "HOMEPATH")
	@Schema(name="首页对应路径")
	private String homePath;

//	@TableField(value = "MODULETEXT")
//	@Schema(name="模块页面文字")
//	private String moduleText;
//
//	@TableField(value = "LOGOPICTURE")
//	@Schema(name="模块页面logo")
//	private String logoPicture;

	/**
	 * 状态
	 */
	@TableField(value = "STATE")
	@Schema(name="状态", hidden = true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@TableField(value = "CREATOR")
	@Schema(name="创建人ID", hidden = true)
	private BigDecimal creator;

	/**
	 * 工作单位ID
	 */
	@TableField(value = "WORKUNIT")
	@Schema(name="工作单位ID", hidden = true)
	private BigDecimal workUnit;

	/**
	 * 所属集团ID
	 */
	@TableField(value = "BELONGGROUP")
	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;

	/**
	 * 创建时间
	 */
	@TableField(value = "CREATEDTIME")
	@Schema(name="创建时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "UPDATEDTIME")
	@Schema(name="更新时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblSystemHomePageOracle ofId(BigDecimal id) {
		TblSystemHomePageOracle tblSystemHomePageOracle = new TblSystemHomePageOracle();
		tblSystemHomePageOracle.setId(id);
		return tblSystemHomePageOracle;
	}

	public static TblSystemHomePageOracle ofStateBelongGroup(Integer state, BigDecimal belongGroup) {
		TblSystemHomePageOracle tblSystemHomePageOracle = new TblSystemHomePageOracle();
		tblSystemHomePageOracle.setState(state);
		tblSystemHomePageOracle.setBelongGroup(belongGroup);
		return tblSystemHomePageOracle;
	}

	public static TblSystemHomePageOracle ofState(BigDecimal id, Integer state) {
		TblSystemHomePageOracle tblSystemHomePageOracle = new TblSystemHomePageOracle();
		tblSystemHomePageOracle.setId(id);
		tblSystemHomePageOracle.setState(state);
		return tblSystemHomePageOracle;
	}
}