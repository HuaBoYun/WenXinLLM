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
 * 审计-excel分析预览表数据内容
 */
@Schema(name="TblAuditModelExcelContentOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_AUDIT_MODEL_EXCEL_CONTENT")
public class TblAuditModelExcelContentOracle implements Serializable {

	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	//@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private BigDecimal id;

	/**
	 * 表名称英文
	 */
	@Column(name = "TABLENAMEEN")
	@Schema(name = "表名称英文")
	private String tableNameEn;

	/**
	 * excel关联ID
	 */
	@Column(name = "EXCELID")
	@Schema(name = "excel关联ID")
	private BigDecimal excelId;

	/**
	 * 列集合 json数组形式存储
	 */
	@Column(name = "TABLELIST")
	@Schema(name = "列集合 json数组形式存储")
	private String tableList;

	/**
	 * 列集合-值 json数组形式存储
	 */
	@Column(name = "TABLEVALUELIST")
	@Schema(name = "列集合-值 json数组形式存储")
	private String tableValueList;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
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

	public static TblAuditModelExcelContentOracle ofId(BigDecimal id) {
		TblAuditModelExcelContentOracle auditModelExcelContentOracle = new TblAuditModelExcelContentOracle();
		auditModelExcelContentOracle.setId(id);
		return auditModelExcelContentOracle;
	}
}