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
 * 活动管理表
 */
@Schema(name="TblFwglActivityManagementOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_activity_management")
public class TblFwglActivityManagementOracle implements Serializable {

	@Id
	@Column(name = "ACTIVITYMANAGEMENTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "活动管理ID")
	private Long activityManagementId;

	//	@Column(name = "UNITNAME")
	//	@Schema(name = "单位名称")
	//	private String unitName;

	@Column(name = "REGISTERTIME")
	@Schema(name = "登记时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;

	@Column(name = "UNPLANNED")
	@Schema(name = "计划外")
	private String unplanned;

	@Column(name = "PLANNED")
	@Schema(name = "计划内")
	private String planned;

	@Column(name = "ACTIVITYTOPIC")
	@Schema(name = "活动主题")
	private String activityTopic;

	@Column(name = "ACTIVITYCONTENT")
	@Schema(name = "活动内容简介")
	private String activityContent;

	@Column(name = "ACTIVITYMANAGEMENTCREATOR")
	@Schema(name = "创建人")
	private String activityManagementCreator;

	@Column(name = "ACTIVITYMANAGEMENTCREATEDTIME")
	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityManagementCreatedTime;

	@Column(name = "SUBSIDIARYOFFICIALACCOUNTLINK")
	@Schema(name = "子公司公众号链接")
	private String subsidiaryOfficialAccountLink;

	@Column(name = "FIRMWEBSITELINK")
	@Schema(name = "公司官网链接")
	private String firmWebsiteLink;

	@Column(name = "SITEPHOTO")
	@Schema(name = "现场照片")
	private String sitePhoto;

	@Column(name = "ACTIVITYFILEIDS")
	@Schema(name = "上传活动附 上传文件ids 多个逗号隔开")
	private String activityFileIds;

	@Column(name = "ACTIVITYOTHERFILEIDS")
	@Schema(name = "上传活动附件-其他 上传文件ids 多个逗号隔开")
	private String activityOtherFileIds;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Transient
	@Schema(name="工作单位名称",hidden=true)
	private String workUnitName;

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

	@Schema(name = "知会人员，多选用逗号隔开")
	@Column(name = "INFORMPERSONNEL")
	private String informPersonnel;

	@Transient
	@Schema(name = "知会人员名称，多选用逗号隔开")
	private String informPersonnelName;

	private static final long serialVersionUID = 1L;

	public static TblFwglActivityManagementOracle ofId(Long id) {
		TblFwglActivityManagementOracle tblFwglActivityManagementOracle = new TblFwglActivityManagementOracle();
		tblFwglActivityManagementOracle.setActivityManagementId(id);
		return tblFwglActivityManagementOracle;
	}
}
