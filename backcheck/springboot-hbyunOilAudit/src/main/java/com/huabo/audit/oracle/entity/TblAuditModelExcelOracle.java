package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审计-excel导入记录
 */
@Schema(name="TblAuditModelExcelOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_AUDIT_MODEL_EXCEL")
public class TblAuditModelExcelOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@Schema(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	private BigDecimal id;

	/**
	 * 数据库关联ID
	 */
	@Column(name = "DATABASEID")
	@Schema(name = "数据库关联ID")
	private BigDecimal dataBaseId;

	@Transient
	@Schema(name="数据库连接地址",hidden=true)
	private String dataBaseConnectionAddress;

	@Transient
	@Schema(name="数据库名称",hidden=true)
	private String dataBaseUsers;

	/**
	 * EXCEL文件ID
	 */
	@Column(name = "EXCELID")
	@Schema(name = "EXCEL文件ID")
	private Integer excelId;

	@Transient
	@Schema(name="EXCEL文件名称",hidden=true)
	private String excelName;

	/**
	 * 失败异常信息
	 */
	@Column(name = "ERRORMSG")
	@Schema(name="失败异常信息",hidden=true)
	private String errorMsg;

	/**
	 * 状态 1-进行中 2-已完成 3-失败
	 */
	@Column(name = "STATE")
	@Schema(name="状态 1-进行中 2-已完成 3-失败",hidden=true)
	private Integer state;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private Integer creator;

	/**
	 * 工作单位
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private Integer workUnit;

	/**
	 * 所属集团
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;

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

	public static TblAuditModelExcelOracle ofId(BigDecimal id) {
		TblAuditModelExcelOracle tblAuditModelExcelOracle = new TblAuditModelExcelOracle();
		tblAuditModelExcelOracle.setId(id);
		return tblAuditModelExcelOracle;
	}

	public static TblAuditModelExcelOracle ofState(BigDecimal id, Integer state, String errorMsg) {
		TblAuditModelExcelOracle tblAuditModelExcelOracle = new TblAuditModelExcelOracle();
		tblAuditModelExcelOracle.setId(id);
		tblAuditModelExcelOracle.setState(state);
		tblAuditModelExcelOracle.setErrorMsg(errorMsg);
		return tblAuditModelExcelOracle;
	}
}