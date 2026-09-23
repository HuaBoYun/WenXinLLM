package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 课题管理表
 */
@Schema(name="TblFwglSubjectManagementOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_subject_management")
public class TblFwglSubjectManagementOracle implements Serializable {

	@Id
	@Column(name = "SUBJECTMANAGEMENTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "课题管理ID")
	private Long subjectManagementId;

	@Column(name = "SUBJECTMANAGEMENTNAME")
	@Schema(name = "课题名称")
	private String subjectManagementName;

	@Column(name = "TYPE")
	@NotNull(message = "type课题类型 1-开题报告 2-结题报告，不能为空")
	@Schema(name = "课题类型 1-开题报告 2-结题报告")
	private Integer type;

	@Column(name = "CREATIONUNIT")
	@Schema(name = "创建单位")
	private String creationUnit;

	@Column(name = "REGISTERTIME")
	@Schema(name = "登记时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;

	@Column(name = "BACKGROUND")
	@Schema(name = "课题背景和意义")
	private String background;

	@Column(name = "TARGET")
	@Schema(name = "课题研究目标")
	private String target;

	@Column(name = "CONTENT")
	@Schema(name = "课题主要内容")
	private String content;

	@Column(name = "PRELIMINARYOUTLINE")
	@Schema(name = "课题初步提纲")
	private String preliminaryOutline;

	@Column(name = "PROPOSEDMETHOD")
	@Schema(name = "拟取的研究方法和手段")
	private String proposedMethod;

	@Column(name = "STUDYSCHEDULING")
	@Schema(name = "研究工作进度安排")
	private String studyScheduling;

	@Column(name = "STUDYPERSONNELARRANGE")
	@Schema(name = "研究工作人员安排")
	private String studyPersonnelArrange;

	@Column(name = "SUPPORTISSUES")
	@Schema(name = "需总部统筹支持事项")
	private String supportIssues;

	@Column(name = "SUBJECTOUTCOME")
	@Schema(name = "课题结果")
	private String subjectOutcome;

	@Column(name = "OTHER")
	@Schema(name = "其他")
	private String other;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Transient
	@Schema(name="创建人",hidden=true)
	private String creatorName;

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

	public static TblFwglSubjectManagementOracle ofId(Long id) {
		TblFwglSubjectManagementOracle tblFwglSubjectManagementMySql = new TblFwglSubjectManagementOracle();
		tblFwglSubjectManagementMySql.setSubjectManagementId(id);
		return tblFwglSubjectManagementMySql;
	}
}
