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

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.central.enterprises.audit.vo.result.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 央企内审-综合管理-会议申请
 */
@Schema(name="TblCeaConferenceApplyOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_CEA_CONFERENCE_APPLY")
public class TblCeaConferenceApplyOracle implements Serializable {

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@Schema(name = "主键ID")
	@GeneratedValue(generator = "JDBC")
	private Long id;

	@Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
	
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
	 * 参会人员 多个逗号隔开
	 */
	@Column(name = "ATTENDEES")
	@Schema(name = "参会人员 多个逗号隔开")
	private String attendees;

	@Transient
	@Schema(name="参会人员名称 多个逗号隔开",hidden=true)
	private List<UserInfo> attendeesName;

	/**
	 * 会议开始时间
	 */
	@Column(name = "CONFERENCETIMESTART")
	@Schema(name = "会议开始时间 格式：yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date conferenceTimeStart;

	/**
	 * 会议结束时间
	 */
	@Column(name = "CONFERENCETIMEEND")
	@Schema(name = "会议结束时间 格式：yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date conferenceTimeEnd;

	/**
	 * 会议地点
	 */
	@Column(name = "CONFERENCEPLACE")
	@Schema(name = "会议地点")
	private String conferencePlace;

	/**
	 * 会议内容
	 */
	@Column(name = "CONFERENCECONTENT")
	@Schema(name = "会议内容")
	private String conferenceContent;

	/**
	 * 会议要求
	 */
	@Column(name = "CONFERENCEREQUIRE")
	@Schema(name = "会议要求")
	private String conferenceRequire;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

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

	@Column(name = "ATTENDEESNUM")
	@Schema(name = "参会人员数量")
	private String attendeesNum;

	private static final long serialVersionUID = 1L;

	public static TblCeaConferenceApplyOracle ofId(Long id) {
		TblCeaConferenceApplyOracle ceaConferenceApplyOracle = new TblCeaConferenceApplyOracle();
		ceaConferenceApplyOracle.setId(id);
		return ceaConferenceApplyOracle;
	}
}