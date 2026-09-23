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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 登记管理表
 */
@Schema(name="TblFwglRegisterManagementMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_register_management")
public class TblFwglRegisterManagementMySql implements Serializable {

	@Id
	@Column(name = "REGISTERMANAGEMENTID")
	@Schema(name = "登记管理ID")
	@GeneratedValue(generator = "JDBC")
	private Integer registerManagementId;

	@Column(name = "TYPE")
	@NotNull(message = "type类别 1-商标 2-版权 3-专利，不能为空")
	@Schema(name = "类别 1-商标 2-版权 3-专利")
	private Integer type;

	@Column(name = "REGISTERNAME")
	@Schema(name = "名称")
	private String registerName;

	@Column(name = "CATEGORY")
	@Schema(name = "类别")
	private Integer category;

	@Column(name = "REGISTERTIME")
	@Schema(name = "注册时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;

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

	public static TblFwglRegisterManagementMySql ofId(Integer id) {
		TblFwglRegisterManagementMySql tblFwglRegisterManagementMySql = new TblFwglRegisterManagementMySql();
		tblFwglRegisterManagementMySql.setRegisterManagementId(id);
		return tblFwglRegisterManagementMySql;
	}
}
