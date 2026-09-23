package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 制度审核-制度表
 */
@Schema(name="TblFwglInstitutionAuditExt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_institution_audit_ext")
public class TblFwglInstitutionAuditExtOracle implements Serializable {

	@Id
	@Column(name = "INSTITUTIONAUDITEXTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "制度审核-制度ID")
	private Long institutionAuditExtId;

	@Column(name = "INSTITUTIONNAME")
	@Schema(name = "制度名称")
	private String institutionName;

	@Column(name = "INSTITUTIONTYPE")
	@Schema(name = "制度分类（制度审核） 1-经营管理类-一般制度 2-经营管理类-基本制度 3-经营管理类-重要制度 4-非经营管理类")
	private Integer institutionType;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;

	private static final long serialVersionUID = 1L;

	public static TblFwglInstitutionAuditExtOracle ofId(Long id) {
		TblFwglInstitutionAuditExtOracle tblFwglInstitutionAuditExtMySql = new TblFwglInstitutionAuditExtOracle();
		tblFwglInstitutionAuditExtMySql.setInstitutionAuditExtId(id);
		return tblFwglInstitutionAuditExtMySql;
	}
}
