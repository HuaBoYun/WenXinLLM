package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内生-综合管理-周报
 */
@Schema(name="TblCeaWeekly")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_WEEKLY")
public class TblCeaWeekly implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 人员ID
	 */
	@Column(name = "STAFFID")
	@Schema(name = "人员ID")
	private Long staffId;

	@Transient
	@Schema(name = "人员名称")
	private String staffName;

	/**
	 * 本周工作完成情况
	 */
	@Column(name = "THISWEEKCOMPLETE")
	@Schema(name = "本周工作完成情况")
	private String thisWeekComplete;

	/**
	 * 下周重点工作安排
	 */
	@Column(name = "NEXTWEEKCOMPLETE")
	@Schema(name = "下周重点工作安排")
	private String nextWeekComplete;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "REPORTWORKUNIT")
	@Schema(name = "上报部门")
	private Long reportWorkUnit;

	@Transient
	@Schema(name = "上报部门明名称")
	private String reportWorkUnitName;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人ID",hidden=true)
	private Long creator;

	@Transient
	@Schema(name="创建人ID名称",hidden=true)
	private String creatorName;

	/**
	 * 工作单位ID
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位ID",hidden=true)
	private Long workUnit;

	/**
	 * 所属集团ID
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团ID",hidden=true)
	private Long belongGroup;

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

	@Schema(name="是否汇总 1-是 0-否")
	@Column(name = "FLAGSUMMARY")
	private Integer flagSummary;

	@Transient
	@Schema(name="汇总周报IDS")
	private List<Long> ids;

	@Transient
	@Schema(name="汇总周报IDS信息")
	private List<TblCeaWeekly> infoIds;

	private static final long serialVersionUID = 1L;

	public static TblCeaWeekly ofId(Long id) {
		TblCeaWeekly tblCeaWeekly = new TblCeaWeekly();
		tblCeaWeekly.setId(id);
		return tblCeaWeekly;
	}
}