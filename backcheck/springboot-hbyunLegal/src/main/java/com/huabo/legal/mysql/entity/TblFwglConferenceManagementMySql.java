package com.huabo.legal.mysql.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 会议管理
 */
@Schema(name="TblFwglConferenceManagementMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_conference_management")
public class TblFwglConferenceManagementMySql implements Serializable {

	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "CONFERENCEID")
	@Schema(name = "会议ID")
	private Integer conferenceId;

	@Column(name = "CONFERENCENAME")
	@NotBlank(message = "会议名称，不能为空")
	@Schema(name = "会议名称")
	private String conferenceName;

	@Column(name = "COMPERE")
	@Schema(name = "会议主持人")
	private String compere;

	@Column(name = "CONFERENCETIME")
	@Schema(name = "会议时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date conferenceTime;

	@Column(name = "CONFERENCECREATOR")
	@Schema(name = "创建人(列表)")
	private String conferenceCreator;

	@Column(name = "CONFERENCECREATEDTIME")
	@Schema(name = "创建时间(列表)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date conferenceCreatedTime;

	@Column(name = "CONTENT")
	@Schema(name = "会议内容")
	private String content;
	
	@Column(name = "PARTICIPANTS")
	@Schema(name = "会议参与者ID 多个参与者用逗号隔开")
	private String participants;
	
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "STATE")
	@Schema(name = "状态")
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

	public static TblFwglConferenceManagementMySql ofId(Integer id) {
		TblFwglConferenceManagementMySql tblFwglConferenceManagementMySql = new TblFwglConferenceManagementMySql();
		tblFwglConferenceManagementMySql.setConferenceId(id);
		return tblFwglConferenceManagementMySql;
	}
}