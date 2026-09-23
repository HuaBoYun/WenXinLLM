package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义表映射
 */
@Schema(name = "TblSystemCustomizeTable")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_SYSTEM_CUSTOMIZE_TABLE")
public class TblSystemCustomizeTable implements Serializable {
	/**
	 * 主键ID
	 */
	@TableId(value = "ID",type = IdType.INPUT )
	@Schema(name="主键ID")
	private BigDecimal id;

	/**
	 * 所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx
	 */
	@TableField(value ="MODULETYPE")
	@Schema(name="所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx")
	private String moduleType;

	/**
	 * 目录ID（父类ID）
	 */
	@TableField(value ="PARENTCATALOGUEID")
	@Schema(name="目录ID（父类ID）")
	private BigDecimal parentCatalogueId;

	/**
	 * 目录ID（目录最下级ID）
	 */
	@TableField(value ="CATALOGUEID")
	@Schema(name="目录ID（目录最下级ID）")
	private BigDecimal catalogueId;

	/**
	 * 表名
	 */
	@TableField(value ="TABLENAME")
	@Schema(name="表名")
	private String tableName;

	/**
	 * 状态
	 */
	@TableField(value ="STATE")
	@Schema(name="状态")
	private Integer state;

	/**
	 * 创建人ID
	 */
	@TableField(value ="CREATOR")
	@Schema(name="创建人ID")
	private BigDecimal creator;

	/**
	 * 工作单位ID
	 */
	@TableField(value ="WORKUNIT")
	@Schema(name="工作单位ID")
	private BigDecimal workUnit;

	/**
	 * 所属集团ID
	 */
	@TableField(value ="BELONGGROUP")
	@Schema(name="所属集团ID")
	private BigDecimal belongGroup;

	/**
	 * 创建时间
	 */
	@TableField(value ="CREATEDTIME")
	@Schema(name="创建时间")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@TableField(value ="UPDATEDTIME")
	@Schema(name="更新时间")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;
}