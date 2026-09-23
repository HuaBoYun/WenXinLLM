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
 * 规范管理表
 */
@Schema(name="TblFwglPlanManagementMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_plan_management")
public class TblFwglPlanManagementMySql implements Serializable {

	@Id
	@Column(name = "PLANMANAGEMENTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "规划管理ID")
	private Integer planManagementId;

	@Column(name = "TYPE")
	@Schema(name = "规划类型")
	private String type;

	@Column(name = "REGISTERTYPE")
	@Schema(name = "规划登记类型")
	private String registerType;

	@Column(name = "PLANMANAGEMENTNAME")
	@Schema(name = "规划名称")
	private String planManagementName;

	@Column(name = "PLANMANAGEMENTCREATOR")
	@Schema(name = "创建人（列表）")
	private String planManagementCreator;

	@Column(name = "PLANMANAGEMENTCREATEDTIME")
	@Schema(name = "创建时间（列表）")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planManagementCreatedTime;

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

	public static TblFwglPlanManagementMySql ofId(Integer id) {
		TblFwglPlanManagementMySql tblFwglPlanManagementMySql = new TblFwglPlanManagementMySql();
		tblFwglPlanManagementMySql.setPlanManagementId(id);
		return tblFwglPlanManagementMySql;
	}
}
