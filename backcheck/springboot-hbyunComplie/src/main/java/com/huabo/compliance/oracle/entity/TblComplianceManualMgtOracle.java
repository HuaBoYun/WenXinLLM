package com.huabo.compliance.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 合规-手册管理
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_COMPLIANCE_MANUAL_MGT")
public class TblComplianceManualMgtOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private Integer id;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	/**
	 * 手册名称
	 */
	@Column(name = "MANUALNAME")
	@Schema(name = "手册名称")
	private String manualName;

	/**
	 * 版本号
	 */
	@Column(name = "VERSIONNUMBER")
	@Schema(name = "版本号")
	private String versionNumber;

	/**
	 * 经办人
	 */
	@Column(name = "TRANSACTOR")
	@Schema(name = "经办人")
	private Integer transactor;

	@Transient
	@Schema(name="经办人名称",hidden=true)
	private String transactorName;

	@Column(name = "CONTENT")
	@Schema(name = "富文本框")
	private String content;

	/**
	 * 部门负责人
	 */
	@Column(name = "DEPARTMENTHEAD")
	@Schema(name = "部门负责人")
	private Integer departmentHead;

	@Transient
	@Schema(name="部门负责人名称",hidden=true)
	private String departmentHeadName;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private Integer creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

	/**
	 * 工作单位
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private Integer workUnit;

	/**
	 * 所属集团
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;

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

	public static TblComplianceManualMgtOracle ofId(Integer id) {
		TblComplianceManualMgtOracle tblComplianceManualMgtOracle = new TblComplianceManualMgtOracle();
		tblComplianceManualMgtOracle.setId(id);
		return tblComplianceManualMgtOracle;
	}
}