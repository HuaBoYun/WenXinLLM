package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 法律服务-工作报告表/服务登记(此表名实体类名称与oracle数据库不一致 注意)
 */
@Schema(name="TblFwglLawServiceWorkReportOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_service_work_report")
public class TblFwglLawServiceWorkReportOracle implements Serializable {

	@Id
	@Column(name = "WORKREPORTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "工作报告ID")
	private Long workReportId;

	@Column(name = "BUSINESSPREMISESNAME")
	@Schema(name = "事务所名称(常年法律服务)")
	private String businessPremisesName;

	@Column(name = "REPORTTIME")
	@Schema(name = "报告日期(常年法律服务)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reportTime;

	@Column(name = "ISCOLLABORATION")
	@Schema(name = "有无合作(专项法律服务)")
	private Integer isCollaboration;

	@Column(name = "SERVICEPROJECT")
	@Schema(name = "服务项目(专项法律服务)")
	private String serviceProject;

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

	private static final long serialVersionUID = 1L;

	public static TblFwglLawServiceWorkReportOracle ofId(Long id) {
		TblFwglLawServiceWorkReportOracle tblFwglLawServiceWorkReportMySql = new TblFwglLawServiceWorkReportOracle();
		tblFwglLawServiceWorkReportMySql.setWorkReportId(id);
		return tblFwglLawServiceWorkReportMySql;
	}
}
