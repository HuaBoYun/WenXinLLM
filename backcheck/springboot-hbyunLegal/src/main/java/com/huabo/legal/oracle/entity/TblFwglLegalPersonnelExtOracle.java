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
 * 法务人员扩展表
 */
@Schema(name="TblFwglLegalPersonnelExtOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_legal_personnel_ext")
public class TblFwglLegalPersonnelExtOracle implements Serializable {

	@Id
	@Column(name = "PERSONNELEXTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "法务人员扩展ID")
	private Long personnelExtId;

	@Column(name = "STARTTIME")
	@Schema(name = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@Column(name = "ENDTIME")
	@Schema(name = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	@Column(name = "WORKUNITEXT")
	@Schema(name = "工作单位（扩展）")
	private String workUnitExt;

	@Column(name = "POSITION")
	@Schema(name = "职务")
	private String position;

	@Column(name = "WORKACHIEVEMENT")
	@Schema(name = "工作成果")
	private String workAchievement;

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

	public static TblFwglLegalPersonnelExtOracle ofId(Long id) {
		TblFwglLegalPersonnelExtOracle tblFwglLegalPersonnelExtMySql = new TblFwglLegalPersonnelExtOracle();
		tblFwglLegalPersonnelExtMySql.setPersonnelExtId(id);
		return tblFwglLegalPersonnelExtMySql;
	}
}
