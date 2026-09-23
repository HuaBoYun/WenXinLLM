package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

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
 * 系统项目授权
 */
@Schema(name = "TblSystemProjectAuthOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_SYSTEM_PROJECT_AUTH")
@TableName(value = "TBL_SYSTEM_PROJECT_AUTH")
public class TblSystemProjectAuthOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@TableId(value="ID",type = IdType.INPUT)
	@Schema(name="主键ID", hidden = true)
	private BigDecimal id;

	/**
	 * 关联集团ID
	 */
	@TableField(value = "AUTHORGID")
	@Schema(name="关联集团ID")
	private BigDecimal authOrgId;

	/**
	 * 关联项目ID
	 */
	@TableField(value = "PROJECTID")
	@Schema(name="关联项目ID")
	private BigDecimal projectId;

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

	public static TblSystemProjectAuthOracle ofId(BigDecimal id) {
		TblSystemProjectAuthOracle tblSystemProjectAuth = new TblSystemProjectAuthOracle();
		tblSystemProjectAuth.setId(id);
		return tblSystemProjectAuth;
	}

	public static TblSystemProjectAuthOracle ofDelete(BigDecimal projectId, BigDecimal belongGroup) {
		TblSystemProjectAuthOracle systemProjectAuth = new TblSystemProjectAuthOracle();
		systemProjectAuth.setProjectId(projectId);
		systemProjectAuth.setBelongGroup(belongGroup);
		return systemProjectAuth;
	}

	public static TblSystemProjectAuthOracle ofDelete(BigDecimal projectId) {
		TblSystemProjectAuthOracle systemProjectAuth = new TblSystemProjectAuthOracle();
		systemProjectAuth.setProjectId(projectId);
		return systemProjectAuth;
	}
}