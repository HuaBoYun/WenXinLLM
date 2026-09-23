package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

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
 * 系统项目表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_SYSTEM_PROJECT")
public class TblSystemProjectOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@TableId(value = "ID",type = IdType.INPUT)
	@Schema(name="主键ID")
	private BigDecimal id;

	/**
	 * 图标
	 */
	@TableField(value = "ICON")
	@Schema(name="图标")
	private String icon;

	/**
	 * 名称
	 */
	@TableField(value = "PROJECTNAME")
	@Schema(name="名称")
	private String projectName;

	/**
	 * 排序
	 */
	@TableField(value = "SORT")
	@Schema(name="排序")
	private Integer sort;

	@TableField(value = "COLOR")
	@Schema(name="颜色")
	private String color;

	/**
	 * 本地项目路由
	 */
	@TableField(value = "PROJECTROUTE")
	@Schema(name="本地项目路由")
	private String projectRoute;

	/**
	 * 其他项目路由
	 */
	@TableField(value = "OTHERPROJECTROUTE")
	@Schema(name="其他项目路由")
	private String otherProjectRoute;

	/**
	 * 唯一标识
	 */
	@NotNull(message = "唯一标识 不能为空")
	@TableField(value = "UNIQUEIDENTIFICATION")
	@Schema(name="唯一标识")
	private String uniqueIdentification;

	/**
	 * 项目类型 1-本地项目 2-流程平台项目
	 */
	@TableField(value = "PROJECTTYPE")
	@Schema(name="项目类型 1-本地项目 2-流程平台项目")
	private Integer projectType;

	@TableField(value = "OTHERNO")
	@Schema(name="其他项目编码")
	private String otherNo;

	@TableField(value = "OTHERNAME")
	@Schema(name="其他项目名称")
	private String otherName;

	/**
	 * 状态 1-启用 0-禁用
	 */
	@TableField(value = "STATE")
	@Schema(name="状态 1-启用 0-禁用", hidden = true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@TableField(value = "CREATOR")
	@Schema(name="创建人ID", hidden = true)
	private BigDecimal creator;

	@TableField(exist = false)
	@Schema(name="创建人名称", hidden = true)
	private String creatorName;

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="创建时间", hidden = true)
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "UPDATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="更新时间", hidden = true)
	private Date updatedTime;

	@TableField(exist = false)
	@Schema(name = "此接口特殊处理，token需要参数接受")
	private String token;

	private static final long serialVersionUID = 1L;

	public static TblSystemProjectOracle ofId(BigDecimal id) {
		TblSystemProjectOracle tblSystemProjectOracle = new TblSystemProjectOracle();
		tblSystemProjectOracle.setId(id);
		return tblSystemProjectOracle;
	}

	public static TblSystemProjectOracle ofUniqueIdentification(String uniqueIdentification, Integer projectType, BigDecimal belongGroup) {
		TblSystemProjectOracle tblSystemProjectOracle = new TblSystemProjectOracle();
		tblSystemProjectOracle.setUniqueIdentification(uniqueIdentification);
		tblSystemProjectOracle.setBelongGroup(belongGroup);
		tblSystemProjectOracle.setProjectType(projectType);
		return tblSystemProjectOracle;
	}

	public static TblSystemProjectOracle ofUniqueIdentification(String uniqueIdentification) {
		TblSystemProjectOracle tblSystemProjectOracle = new TblSystemProjectOracle();
		tblSystemProjectOracle.setUniqueIdentification(uniqueIdentification);
		tblSystemProjectOracle.setProjectType(2);
		return tblSystemProjectOracle;
	}
}