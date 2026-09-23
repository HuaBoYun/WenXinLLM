package com.huabo.compliance.oracle.entity;

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
 * 合规-合规风险
 */
@Schema(name="TblComplianceRiskOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_COMPLIANCE_RISK")
public class TblComplianceRiskOracle implements Serializable {
	/**
	 * id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Schema(name = "id")
	private Integer id;

	/**
	 * 事件编号
	 */
	@Column(name = "RISKNUMBER")
	@Schema(name = "事件编号")
	private String riskNumber;

	/**
	 * 事件名称
	 */
	@Column(name = "RISKNAME")
	@Schema(name = "事件名称")
	private String riskName;

	/**
	 * 部门
	 */
	@Column(name = "DEPARTMENT")
	@Schema(name = "部门")
	private Integer department;

	@Transient
	@Schema(name="部门名称",hidden=true)
	private String departmentName;

	/**
	 * 发生日期
	 */
	@Column(name = "FINDTIME")
	@Schema(name = "发生日期 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date findTime;

	/**
	 * 发现日期
	 */
	@Column(name = "DISCOVERYTIME")
	@Schema(name = "发现日期 格式：yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date discoveryTime;

	/**
	 * 风险事件类别 1-重大 2-非重大
	 */
	@Column(name = "RISKTYPE")
	@Schema(name = "风险事件类别 1-重大 2-非重大")
	private Integer riskType;

	/**
	 * 上传文件ids 多个逗号隔开
	 */
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "REMARK")
	@Schema(name = "事件说明")
	private String remark;

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

	@Transient
	@Schema(name="工作单位名称",hidden=true)
	private String workUnitName;

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

	public static TblComplianceRiskOracle ofId(Integer id) {
		TblComplianceRiskOracle tblComplianceRiskOracle = new TblComplianceRiskOracle();
		tblComplianceRiskOracle.setId(id);
		return tblComplianceRiskOracle;
	}
}