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
 * 年度考核表
 */
@Schema(name="TblFwglAnnualExamineMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_annual_examine")
public class TblFwglAnnualExamineMySql implements Serializable {

	@Id
	@Column(name = "ANNUALEXAMINEID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "年度考核ID")
	private Integer annualExamineId;

	@Column(name = "ANNUALEXAMINENAME")
	@Schema(name = "考核名称")
	private String annualExamineName;

	@Column(name = "SCORETRANSACTION")
	@Schema(name = "评分事务id")
	private String scoreTransaction;

	@Column(name = "TYPE")
	@Schema(name = "考核类型 1-外部监管考核 2-子单位考核")
	private Integer type;

	@Column(name = "EXAMINETIME")
	@Schema(name = "考核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date examineTime;

	@Column(name = "ANNUALEXAMINECREATOR")
	@Schema(name = "创建人（列表）")
	private String annualExamineCreator;

	@Column(name = "ANNUALEXAMINECREATEDTIME")
	@Schema(name = "创建时间（列表）")
	private String annualExamineCreatedTime;

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

	public static TblFwglAnnualExamineMySql ofId(Integer id) {
		TblFwglAnnualExamineMySql tblFwglAnnualExamineMySql = new TblFwglAnnualExamineMySql();
		tblFwglAnnualExamineMySql.setAnnualExamineId(id);
		return tblFwglAnnualExamineMySql;
	}
}
