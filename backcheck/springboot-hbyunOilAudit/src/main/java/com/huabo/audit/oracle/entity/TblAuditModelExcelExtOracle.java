package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审计-excel表数据
 */
@Schema(name="TblAuditModelExcelExtOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_AUDIT_MODEL_EXCEL_EXT")
public class TblAuditModelExcelExtOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private BigDecimal id;

	/**
	 * EXCEL关联ID
	 */
	@Column(name = "EXCELID")
	@Schema(name = "EXCEL关联ID")
	private BigDecimal excelId;

	/**
	 * 表名称英文
	 */
	@Column(name = "TABLENAMEEN")
	@Schema(name = "表名称英文")
	private String tableNameEn;

	/**
	 * 表名称
	 */
	@Column(name = "TABLENAME")
	@Schema(name = "表名称")
	private String tableName;

	/**
	 * 列名称
	 */
	@Column(name = "COLUMNNAME")
	@Schema(name = "列名称")
	private String columnName;

	/**
	 * 状态 0-未生成表 1-已生成表
	 */
	@Column(name = "STATE")
	@Schema(name="状态 0-未生成表 1-已生成表 2-生成失败",hidden=true)
	private Integer state;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private BigDecimal creator;

	/**
	 * 工作单位
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private BigDecimal workUnit;

	/**
	 * 所属集团
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private BigDecimal belongGroup;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="创建时间",hidden=true)
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="更新时间",hidden=true)
	private Date updatedTime;

	@Column(name = "ERRORMSG")
	@Schema(name="异常信息",hidden=true)
	private String errorMsg;

	private static final long serialVersionUID = 1L;

	public static TblAuditModelExcelExtOracle ofId(BigDecimal id) {
		TblAuditModelExcelExtOracle tblAuditModelExcelExtOracle = new TblAuditModelExcelExtOracle();
		tblAuditModelExcelExtOracle.setId(id);
		return tblAuditModelExcelExtOracle;
	}
}