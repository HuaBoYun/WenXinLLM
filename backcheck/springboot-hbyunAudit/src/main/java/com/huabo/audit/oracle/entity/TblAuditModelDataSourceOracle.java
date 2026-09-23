package com.huabo.audit.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 审计-数据源管理
 */
@Schema(name="TblAuditModelDataSourceOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_AUDIT_MODEL_DATA_SOURCE")
public class TblAuditModelDataSourceOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	//@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private BigDecimal id;

	/**
	 * 数据库类型Oracle Mysql SqlServer
	 */
	@Column(name = "DATABASETYPE")
	@Schema(name = "数据库类型Oracle Mysql SqlServer DM8")
	private String dataBaseType;

	/**
	 * 数据库连接地址
	 */
	@Column(name = "DATABASECONNECTIONADDRESS")
	@Schema(name = "数据库连接地址")
	private String dataBaseConnectionAddress;

	/**
	 * 数据库用户
	 */
	@Column(name = "DATABASEUSERS")
	@Schema(name = "数据库用户")
	private String dataBaseUsers;

	/**
	 * 数据库密码
	 */
	@Column(name = "DATABASEPASSWORD")
	@Schema(name = "数据库密码")
	private String dataBasePassWord;

	/**
	 * 数据库所属
	 */
	@Column(name = "DATABASEOWNERSHIP")
	@Schema(name = "数据库所属")
	private String dataBaseOwnership;

	/**
	 * 创建类型 1-数据源管理 2-excel导入默认导入
	 */
	@Column(name = "CREATETYPE")
	@NotNull(message = "创建类型 1-数据源管理 2-excel导入默认导入  不能为空")
	@Schema(name = "创建类型 1-数据源管理 2-excel导入默认导入")
	private Integer createType;

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

	public static TblAuditModelDataSourceOracle ofId(BigDecimal id) {
		TblAuditModelDataSourceOracle tblAuditModelDataSourceOracle = new TblAuditModelDataSourceOracle();
		tblAuditModelDataSourceOracle.setId(id);
		return tblAuditModelDataSourceOracle;
	}

	public static TblAuditModelDataSourceOracle ofDataBaseUsers(String dataBaseUsers) {
		TblAuditModelDataSourceOracle tblAuditModelDataSourceOracle = new TblAuditModelDataSourceOracle();
		tblAuditModelDataSourceOracle.setDataBaseUsers(dataBaseUsers);
		return tblAuditModelDataSourceOracle;
	}
}