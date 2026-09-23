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
import com.huabo.central.enterprises.audit.vo.result.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-综合管理-会议管理
 */
@Schema(name="TblCeaConferenceMgtOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_CONFERENCE_MGT")
public class TblCeaConferenceMgtOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@Schema(name = "主键ID")
	@GeneratedValue(generator = "JDBC")
	private Long id;

	/**
	 * 会议名称
	 */
	@Column(name = "CONFERENCENAME")
	@Schema(name = "会议名称")
	private String conferenceName;

	/**
	 * 会议主持人
	 */
	@Column(name = "CONFERENCECOMPERE")
	@Schema(name = "会议主持人")
	private Long conferenceCompere;

	@Transient
	@Schema(name = "会议主持人名称")
	private String conferenceCompereName;

	/**
	 * 参会人员
	 */
	@Column(name = "ATTENDEES")
	@Schema(name = "参会人员")
	private String attendees;

	@Transient
	@Schema(name="参会人员名称 多个逗号隔开",hidden=true)
	private List<UserInfo> attendeesName;

	/**
	 * 会议地点
	 */
	@Column(name = "CONFERENCEPLACE")
	@Schema(name = "会议地点")
	private String conferencePlace;

	/**
	 * 会议开始时间
	 */
	@Column(name = "CONFERENCETIMESTART")
	@Schema(name = "会议开始时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date conferenceTimeStart;

	/**
	 * 会议结束时间
	 */
	@Column(name = "CONFERENCETIMEEND")
	@Schema(name = "会议结束时间 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date conferenceTimeEnd;

	/**
	 * 会议要求
	 */
	@Column(name = "CONFERENCEREQUIRE")
	@Schema(name = "会议要求")
	private String conferenceRequire;

	/**
	 * 会议内容
	 */
	@Column(name = "CONFERENCECONTENT")
	@Schema(name = "会议内容")
	private String conferenceContent;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Schema(name="接收下发人ID")
	@Column(name = "RECIPIENT")
	private Long recipient;

	@Transient
	@Schema(name="接收下发人名称")
	private String recipientName;

	@Schema(name="办理状态 0-未办理 1-已办理")
	@Column(name = "HANDLESTATE")
	private Integer handleState;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态 1-未下发 2-已下发 3-已提交",hidden=true)
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

	@Transient
	@Schema(name="是否提交：1-已提交")
	private Integer flagSubmit;

	private static final long serialVersionUID = 1L;

	public static TblCeaConferenceMgtOracle ofId(Long id) {
		TblCeaConferenceMgtOracle tblCeaConferenceMgtOracle = new TblCeaConferenceMgtOracle();
		tblCeaConferenceMgtOracle.setId(id);
		return tblCeaConferenceMgtOracle;
	}
}