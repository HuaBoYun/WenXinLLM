package com.huabo.legal.mysql.entity;

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
 * 法律服务-工作记录表
 */
@Schema(name="TblFwglLawServiceWorkRecordMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_law_service_work_record")
public class TblFwglLawServiceWorkRecordMySql implements Serializable {

	@Id
	@Column(name = "WORKRECORDID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "工作记录ID")
	private Integer workRecordId;

	@Column(name = "LAWSERVICENAME")
	@Schema(name = "法律服务名称")
	private String lawServiceName;

	@Column(name = "SERVICEPROJECTTYPE")
	@Schema(name = "服务项目类型")
	private String serviceProjectType;

	@Column(name = "MONEY")
	@Schema(name = "金额")
	private Double money;

	@Column(name = "UNDERTAKELAWOFFICE")
	@Schema(name = "承办律所")
	private String undertakeLawOffice;

	@Column(name = "HOSTLAWOFFICE")
	@Schema(name = "主办律师")
	private String hostLawOffice;

	@Column(name = "SERVICEEFFECTIVENESSOFEVALUATE")
	@Schema(name = "服务效果及评价")
	private String serviceEffectivenessOfEvaluate;

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

	private static final long serialVersionUID = 1L;

	public static TblFwglLawServiceWorkRecordMySql ofId(Integer id) {
		TblFwglLawServiceWorkRecordMySql tblFwglLawServiceWorkRecordMySql = new TblFwglLawServiceWorkRecordMySql();
		tblFwglLawServiceWorkRecordMySql.setWorkRecordId(id);
		return tblFwglLawServiceWorkRecordMySql;
	}
}
