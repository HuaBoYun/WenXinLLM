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
 * 经营事项审核表
 */
@Schema(name="TblFwglOperateMatterAuditOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_operate_matter_audit")
public class TblFwglOperateMatterAuditOracle implements Serializable {

	@Id
	@Column(name = "OPERATEMATTERAUDITID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "经营事项审核ID")
	private Long operateMatterAuditId;

	@Column(name = "OPERATEMATTERNAME")
	@Schema(name = "经营事项名称")
	private String operateMatterName;

	@Column(name = "DRAFTTIME")
	@Schema(name = "起草时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date draftTime;

	@Column(name = "OPERATECREATOR")
	@Schema(name = "创建人(列表)")
	private String operateCreator;

	@Column(name = "OPERATECREATEDTIME")
	@Schema(name = "创建时间(列表)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date operateCreatedTime;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "STATE")
	@Schema(name = "状态")
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name = "创建人")
	private String creator;

	@Column(name = "WORKUNIT")
	@Schema(name = "工作单位")
	private String workUnit;

	@Column(name = "BELONGGROUP")
	@Schema(name = "所属集团")
	private String belongGroup;

	@Column(name = "CREATEDTIME")
	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Column(name = "UPDATEDTIME")
	@Schema(name = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblFwglOperateMatterAuditOracle ofId(Long id) {
		TblFwglOperateMatterAuditOracle tblFwglOperateMatterAuditMySql = new TblFwglOperateMatterAuditOracle();
		tblFwglOperateMatterAuditMySql.setOperateMatterAuditId(id);
		return tblFwglOperateMatterAuditMySql;

	}
}
