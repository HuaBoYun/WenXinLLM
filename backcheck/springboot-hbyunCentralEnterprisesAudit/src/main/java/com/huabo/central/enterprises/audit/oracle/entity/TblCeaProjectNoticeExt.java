package com.huabo.central.enterprises.audit.oracle.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 央企内审-项目评优-通知-下发扩展表
 */
@Schema(name="TblCeaProjectNoticeExt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_PROJECT_NOTICE_EXT")
public class TblCeaProjectNoticeExt implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 关联ID-项目通知ID
	 */
	@Column(name = "PROJECTNOTICEID")
	@Schema(name = "关联ID-项目通知ID")
	private Long projectNoticeId;

	/**
	 * 下发人ID
	 */
	@Column(name = "DISTRIBUTEID")
	@Schema(name = "下发人ID")
	private Long distributeId;

	@Transient
	@Schema(name = "下发人名称（新增编辑不用传）")
	private String distributeName;

	/**
	 * 通知标题
	 */
	@Transient
	@Schema(name = "通知标题")
	private String noticeTitle;

	/**
	 * 通知内容
	 */
	@Transient
	@Schema(name = "通知内容")
	private String noticeContent;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态 0-未处理 1-已提交",hidden=true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人ID",hidden=true)
	private Long creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
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

	private static final long serialVersionUID = 1L;

	public static TblCeaProjectNoticeExt ofId(Long id) {
		TblCeaProjectNoticeExt tblCeaProjectNoticeExt = new TblCeaProjectNoticeExt();
		tblCeaProjectNoticeExt.setId(id);
		return tblCeaProjectNoticeExt;

	}

	public static TblCeaProjectNoticeExt ofProjectNoticeId(Long projectNoticeId) {
		TblCeaProjectNoticeExt tblCeaProjectNoticeExt = new TblCeaProjectNoticeExt();
		tblCeaProjectNoticeExt.setProjectNoticeId(projectNoticeId);
		return tblCeaProjectNoticeExt;
	}
}