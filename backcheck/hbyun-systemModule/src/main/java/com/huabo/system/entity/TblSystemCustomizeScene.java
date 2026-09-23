package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
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

@Schema(name = "TblSystemCustomizeScene")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_SYSTEM_CUSTOMIZE_SCENE")
public class TblSystemCustomizeScene implements Serializable {

	@TableId(value = "ID", type = IdType.INPUT)
	@Schema(name="主键ID")
	private BigDecimal id;

	@NotBlank(message = "moduleType 所属模块 不能为空")
	@Column(name = "MODULETYPE")
	@TableField(value = "MODULETYPE")
	@Schema(name="所属模块,智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx 法务-fwgl", required = true)
	private String moduleType;

	@NotNull(message = "parentCatalogueId 目录ID（父类ID） 不能为空")
	@Column(name = "PARENTCATALOGUEID")
	@TableField(value = "PARENTCATALOGUEID")
	@Schema(name="目录ID（父类ID）", required = true)
	private BigDecimal parentCatalogueId;

	@NotNull(message = "catalogueId 目录ID（目录最下级ID） 不能为空")
	@Column(name = "CATALOGUEID")
	@TableField(value = "CATALOGUEID")
	@Schema(name="目录ID（目录最下级ID）", required = true)
	private BigDecimal catalogueId;

	@Transient
	@TableField(exist = false)
	@Schema(name="目录ID（目录最下级ID）名称")
	private String catalogueName;

	@NotNull(message = "sceneCode 场景唯一编码 不能为空")
	@Schema(name="场景唯一编码", required = true)
	@Column(name = "SCENECODE")
	@TableField(value = "SCENECODE")
	private String sceneCode;

	@Column(name = "STATE")
	@TableField(value = "STATE")
	@Schema(name="状态 0-未启用 1-启用", hidden = true)
	private Integer state;

	@Column(name = "CREATOR")
	@TableField(value = "CREATOR")
	@Schema(name="创建人ID", hidden = true)
	private BigDecimal creator;

	@Column(name = "WORKUNIT")
	@TableField(value = "WORKUNIT")
	@Schema(name="工作单位ID", hidden = true)
	private BigDecimal workUnit;

	@Column(name = "BELONGGROUP")
	@TableField(value = "BELONGGROUP")
	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;

	@Column(name = "CREATEDTIME")
	@TableField(value = "CREATEDTIME")
	@Schema(name="创建时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Column(name = "UPDATEDTIME")
	@TableField(value = "UPDATEDTIME")
	@Schema(name="更新时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Column(name = "SORT")
	@TableField(value = "SORT")
	@Schema(name="排序")
	private Integer sort;

	private static final long serialVersionUID = 1L;

	public static TblSystemCustomizeScene ofId(BigDecimal id) {
		TblSystemCustomizeScene tblSystemCustomizeScene = new TblSystemCustomizeScene();
		tblSystemCustomizeScene.setId(id);
		return tblSystemCustomizeScene;
	}

	public static TblSystemCustomizeScene of(BigDecimal parentCatalogueId, BigDecimal catalogueId) {
		TblSystemCustomizeScene tblSystemCustomizeScene = new TblSystemCustomizeScene();
		tblSystemCustomizeScene.setCatalogueId(catalogueId);
		tblSystemCustomizeScene.setParentCatalogueId(parentCatalogueId);
		return tblSystemCustomizeScene;
	}

	public static TblSystemCustomizeScene ofSceneCode(String sceneCode) {
		TblSystemCustomizeScene tblSystemCustomizeScene = new TblSystemCustomizeScene();
		tblSystemCustomizeScene.setSceneCode(sceneCode);
		return tblSystemCustomizeScene;
	}
}