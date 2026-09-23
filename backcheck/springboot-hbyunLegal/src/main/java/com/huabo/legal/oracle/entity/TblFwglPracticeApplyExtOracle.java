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
 * 执业申请-简历表
 */
@Schema(name="TblFwglPracticeApplyExt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_practice_apply_ext")
public class TblFwglPracticeApplyExtOracle implements Serializable {

	@Id
	@Column(name = "PRACTICEAPPLYEXTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "执业申请-简历ID")
	private Long practiceApplyExtId;

	@Column(name = "STARETIME")
	@Schema(name = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date stareTime;

	@Column(name = "ENDTIME")
	@Schema(name = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	@Column(name = "OLDWORKUNIT")
	@Schema(name = "在何地何部门(学习)工作")
	private String oldWorkUnit;

	@Column(name = "POSITION")
	@Schema(name = "职务")
	private String position;

	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;

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

	public static TblFwglPracticeApplyExtOracle ofId(Long id) {
		TblFwglPracticeApplyExtOracle tblFwglPracticeApplyExtMySql = new TblFwglPracticeApplyExtOracle();
		tblFwglPracticeApplyExtMySql.setPracticeApplyExtId(id);
		return tblFwglPracticeApplyExtMySql;
	}
}
