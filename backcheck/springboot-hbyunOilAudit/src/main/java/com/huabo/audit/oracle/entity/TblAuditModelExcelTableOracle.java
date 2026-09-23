package com.huabo.audit.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 审计-excel分析预览表数据
 */
@Schema(name="TblAuditModelExcelTableOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_AUDIT_MODEL_EXCEL_TABLE")
public class TblAuditModelExcelTableOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private BigDecimal id;

	/**
	 * 表名
	 */
	@Column(name = "TABLENAME")
	@Schema(name = "表名")
	private String tableName;

	/**
	 * 表名称英文
	 */
	@Column(name = "TABLENAMEEN")
	@Schema(name = "表名称英文")
	private String tableNameEn;

	/**
	 * 列名称
	 */
	@Column(name = "COLUMNNAME")
	@Schema(name = "列名称")
	private String columnName;

	/**
	 * 列名称英语
	 */
	@Column(name = "COLUMNNAMENE")
	@Schema(name = "列名称英语")
	private String columnNameNe;

	/**
	 * excel关联ID
	 */
	@Column(name = "EXCELID")
	@Schema(name = "excel关联ID")
	private BigDecimal excelId;

	/**
	 * 列类型
	 */
	@Column(name = "COLUMNTYPE")
	@Schema(name = "列类型")
	private String columnType;

	@Column(name = "ISSELECTED")
	@Schema(name = "是否已选 1-选 0-未选")
	private Integer isSelected;

	@Column(name = "SUBSCRIPT")
	@Schema(name="列下标",hidden=true)
	private Integer subscript;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态 0-未生成表 1-生成表 2-生成失败",hidden=true)
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
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblAuditModelExcelTableOracle ofId(BigDecimal id) {
		TblAuditModelExcelTableOracle tblAuditModelExcelTableOracle = new TblAuditModelExcelTableOracle();
		tblAuditModelExcelTableOracle.setId(id);
		return tblAuditModelExcelTableOracle;
	}
}